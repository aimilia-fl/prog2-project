package gr.aueb.dmst.javengers.central.handlers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import gr.aueb.dmst.javengers.central.CentralApp;
import gr.aueb.dmst.javengers.common.dto.ElevatorResponse;
import gr.aueb.dmst.javengers.common.dto.PassengerRequest;


public class TestRequestHandler {

    private int port = 8080;
    private final String path = "/api/v1/central/requests";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private HttpServer server;
    private final HttpClient client = HttpClient.newHttpClient();
    private String assignedElevator;

    @Before
    public void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0),0);
        port = server.getAddress().getPort();
        assignedElevator = "A";

        CentralApp app = new CentralApp() {
            @Override
            public Optional<String> assignElevator(PassengerRequest request) {
                return Optional.ofNullable(assignedElevator);
            }
        };
        server.createContext(path, new RequestHandler(app));
        server.start();
    }

    @After
    public void shutDown(){
        server.stop(0);
    }

    private HttpResponse<String> sendPostRequest(String jsonBody) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + port + path))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    public void testValidPassengerRequest() throws Exception {
        String dtoBody = "{\"sourceFloor\": 1, \"destinationFloor\": 6}";
        System.out.println("Passenger Request:" );
        System.out.println(dtoBody);

        HttpResponse<String> response = sendPostRequest(dtoBody);
        System.out.println("---------------------------------------");
        System.out.println("Request Response:");
        System.out.println( response.body());
        
        Assert.assertEquals(200,response.statusCode());
        ElevatorResponse elevatorResponse = objectMapper.readValue(response.body(),ElevatorResponse.class);
        Assert.assertNotNull(elevatorResponse);
        Assert.assertEquals("A", elevatorResponse.getAssignedElevator());
        Assert.assertEquals(200, elevatorResponse.getStatus());
    }

    @Test
    public void testNegativeFloor() throws Exception{

        String dtoBody = "{\"sourceFloor\": 1, \"destinationFloor\": -1}";
        HttpResponse<String> response = sendPostRequest(dtoBody);
        Assert.assertEquals(400, response.statusCode());

    }

    @Test
    public void testFloorAbove200() throws Exception{
        String dtoBody = "{\"sourceFloor\": 1, \"destinationFloor\": 201}";
        HttpResponse<String> response = sendPostRequest(dtoBody);
        Assert.assertEquals(400, response.statusCode());
    }

    @Test
    public void testWrongMethod() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + port + path))
        .GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(405,response.statusCode());
    }

    @Test
    public void testEmptyBody() throws Exception {
        HttpResponse<String> response = sendPostRequest("");
        Assert.assertEquals(400,response.statusCode());
    }

    @Test
    public void testResponseMessage() throws Exception {
        RequestHandler handler = new RequestHandler();

        server.createContext("/test-message", exchange -> { 
            handler.responseMessage(exchange, 400, "Invalid Request");
        });

        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:"+ port + "/test-message"))
        .GET()
        .build();

        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(400,response.statusCode());
        Assert.assertEquals("Invalid Request",response.body());

    }

    @Test
    public void testNullRequest() throws Exception {
        HttpResponse<String> response = sendPostRequest("null");
        Assert.assertEquals(400, response.statusCode());
    }

    @Test
    public void testSameSourceDestinationFloor() throws Exception {
        String dtoBody = "{\"sourceFloor\": 3, \"destinationFloor\": 3}";
        System.out.println("Passenger Request:");
        System.out.println(dtoBody);

        HttpResponse<String> response = sendPostRequest(dtoBody);
        System.out.println("---------------------------------------");
        System.out.println("Request Response:");
        System.out.println( response.body());

        Assert.assertEquals(400, response.statusCode());
        Assert.assertTrue(response.body().contains("Please use your feet"));
    }

    @Test
    public void testJsonFormat() throws Exception {
        HttpResponse<String> response = sendPostRequest("{wrongJSONFormat}");
        Assert.assertEquals(400,response.statusCode());
    }

    @Test
    public void testNotAvailabeElevators() throws Exception {
        assignedElevator = null;
        String dtoBody = "{\"sourceFloor\": 1, \"destinationFloor\": 6}";
        System.out.println("Passenger Request:");
        System.out.println(dtoBody);

        HttpResponse<String> response = sendPostRequest(dtoBody);
        System.out.println("---------------------------------------");
        System.out.println("Request Response:");
        System.out.println( response.body());

        Assert.assertEquals(503,response.statusCode());
    }
}