package gr.aueb.dmst.javengers.central.handlers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import gr.aueb.dmst.javengers.common.dto.ElevatorResponse;


public class TestRequestHandler {

    private int port = 8080;
    private static final String path = "/api/v1/central/requests";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private HttpServer server;
    private final HttpClient client = HttpClient.newHttpClient();

    @Before
    public void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0),0);
        port = server.getAddress().getPort();
        server.createContext(path, new RequestHandler());
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

        HttpResponse<String> response = sendPostRequest(dtoBody);

        Assert.assertEquals(200,response.statusCode());
        ElevatorResponse elevatorResponse = objectMapper.readValue(response.body(),ElevatorResponse.class);
        Assert.assertNotNull(elevatorResponse);
    }

    @Test
    public void testNegativeFloor() throws Exception{

        String dtoBody = "{\"sourceFloor\": 1, \"destinationFloor\": -1}";
        HttpResponse<String> response = sendPostRequest(dtoBody);
        Assert.assertEquals(400, response.statusCode());

    }

    @Test
    public void testFloorAbove20() throws Exception{
        String dtoBody = "{\"sourceFloor\": 1, \"destinationFloor\": 21}";
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
        HttpResponse<String> response = sendPostRequest(dtoBody);

        Assert.assertEquals(400, response.statusCode());
        Assert.assertTrue(response.body().contains("Please use your feet"));
    }

    @Test
    public void testJsonFormat() throws Exception {
        HttpResponse<String> response = sendPostRequest("{wrongJSONFormat}");
        Assert.assertEquals(400,response.statusCode());

    }
}