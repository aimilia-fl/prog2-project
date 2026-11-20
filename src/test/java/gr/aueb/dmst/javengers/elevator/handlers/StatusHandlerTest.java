package gr.aueb.dmst.javengers.elevator.handlers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.aueb.dmst.javengers.common.dto.ElevatorStatus;

public class StatusHandlerTest {
    
    private static final String path = "/api/v1/elevators/A/status";
    private HttpServer server;
    private final HttpClient client = HttpClient.newHttpClient();
    private int port;

    private ElevatorStatus status;



    @Before 
    public void setUp() throws IOException {
        status = new ElevatorStatus();
        status.setCurrentFloor(5);

        Supplier<ElevatorStatus> statusSupplier = new Supplier<ElevatorStatus>() {
            @Override
            public ElevatorStatus get() {
                return status;
            }
        };

        server = HttpServer.create(new InetSocketAddress(0),0);
        port = server.getAddress().getPort();
        server.createContext(path, new StatusHandler(statusSupplier));
        server.start();
    }

    @After
    public void tearDown() {
        server.stop(0);
    }

    public HttpResponse<String> sendRequest(String requestType ) throws Exception {
        
        String url = "http://localhost:" + port + path;
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .method(requestType, HttpRequest.BodyPublishers.noBody())
        .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Checks that GET returns correctly the elevator status
     */
    @Test
    public void testValidGetRequest() throws Exception {

        HttpResponse<String> response = sendRequest("GET");

        Assert.assertEquals(200, response.statusCode());
        Assert.assertTrue(response.body().contains("\"currentFloor\":5"));
    }

    /**
     * Checks that unsupported http method is not allowed
     */
    @Test 
    public void testWrongMethod() throws Exception {

        HttpResponse<String> response = sendRequest("POST");

        Assert.assertEquals(405, response.statusCode());
    }

    /**
     * Handles null status
     */
    @Test
    public void testNullStatus() throws Exception {

        status = null;

        HttpResponse<String> response = sendRequest("GET");

        Assert.assertEquals(500, response.statusCode());
    }

    /**
     * Checks that the response is Json
     */
    @Test 
    public void testJsonResponse() throws Exception {

        HttpResponse<String> response = sendRequest("GET");

        String contentType = response.headers().firstValue("Content-Type").orElse("Missing");
        Assert.assertEquals("application/json", contentType);
    }

    /**
     * Converts Json to ElevatorStatus 
     */
    @Test 
    public void testReadElevatorStatusFromJson() throws Exception {

        HttpResponse<String> response = sendRequest("GET");

        ObjectMapper objectMapper = new ObjectMapper();
        ElevatorStatus elevatorStatus = objectMapper.readValue(response.body(), ElevatorStatus.class);

        Assert.assertEquals(5, elevatorStatus.getCurrentFloor());
    }
    
    /**
     * Checks if the new floor is returned
     */

    @Test
    public void testNewFloor() throws Exception {

        status.setCurrentFloor(10);
        HttpResponse<String> response = sendRequest("GET");

        Assert.assertTrue(response.body().contains("\"currentFloor\":10"));

    }
}
