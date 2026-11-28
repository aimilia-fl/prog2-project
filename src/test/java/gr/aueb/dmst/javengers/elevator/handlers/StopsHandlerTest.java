package gr.aueb.dmst.javengers.elevator.handlers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.aueb.dmst.javengers.common.dto.StopResponse;
import gr.aueb.dmst.javengers.common.exceptions.InvalidStopException;
import gr.aueb.dmst.javengers.elevator.model.Stop;
import gr.aueb.dmst.javengers.elevator.model.StopType;

public class StopsHandlerTest {

    private static final String path = "/api/v1/elevators/A/stops";
    private HttpServer server;
    private final HttpClient client = HttpClient.newHttpClient();
    private int port;
    private List<Stop> receivedStops;

    @Before 
    public void setUp() throws IOException {
        receivedStops = new ArrayList<>();

        StopAdder stopAdder = new StopAdder() {

            @Override
            public void addStop(Stop stop) throws InvalidStopException {

                if (stop.getTargetFloor() < 0 || stop.getTargetFloor() > 200) {
                    throw new InvalidStopException("Invalid floor number");
                }

                receivedStops.add(stop);
            }
          
        };

        server = HttpServer.create(new InetSocketAddress(0),0);
        port = server.getAddress().getPort();
        server.createContext(path, new StopsHandler("A", stopAdder));
        server.start();
    }

    @After
    public void tearDown() {
        server.stop(0);
    }

    private HttpResponse<String> sendRequest(String requestType, String body) throws Exception {
    
        String url = "http://localhost:" + port + path;
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type","application/json")
            .method(requestType, HttpRequest.BodyPublishers.ofString(body))
            .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Checks that a valid pick up request adds the stop and returns 200
     */
    @Test 
    public void testValidPickUp() throws Exception {

        String body = "{\"targetFloor\": 12, \"type\":\"PICK_UP\"}";
    
        HttpResponse<String> response = sendRequest("POST", body);

        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(1, receivedStops.size());
        Assert.assertEquals(12, receivedStops.get(0).getTargetFloor());
        Assert.assertEquals(StopType.PICK_UP, receivedStops.get(0).getStopType());
    }

    /**
     * Checks that a valid drop off request adds the stop and returns 200
     */
    @Test 
    public void testValidDropOff() throws Exception {

        String body = "{\"targetFloor\": 19, \"type\":\"DROP_OFF\"}";
        
        HttpResponse<String> response = sendRequest("POST", body);

        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(1, receivedStops.size());
        Assert.assertEquals(19, receivedStops.get(0).getTargetFloor());
        Assert.assertEquals(StopType.DROP_OFF, receivedStops.get(0).getStopType());
    }

    /**
     * Checks that a floor above the limit is rejected
     */
    @Test 
    public void testInvalidFloor() throws Exception {

        String body = "{\"targetFloor\": 202, \"type\":\"DROP_OFF\"}";

        HttpResponse<String> response = sendRequest("POST", body);

        Assert.assertEquals(400, response.statusCode());
        Assert.assertTrue(response.body().contains("Invalid floor number"));
        Assert.assertEquals(0, receivedStops.size());
    }

    /**
     * Checks that a negative floor is rejected 
     */
    @Test 
    public void testNegativeFloor() throws Exception {

        String body = "{\"targetFloor\": -1, \"type\":\"DROP_OFF\"}";

        HttpResponse<String> response = sendRequest("POST", body);

        Assert.assertEquals(400, response.statusCode());
        Assert.assertTrue(response.body().contains("Invalid floor number"));
        Assert.assertEquals(0, receivedStops.size());
    }

    /**
     * Checks that an unsupported HTTP method is rejected
     */
    @Test 
    public void testWrongMethod() throws Exception {

        HttpResponse<String> response = sendRequest("GET", "");

        Assert.assertEquals(405, response.statusCode());
        Assert.assertEquals(0, receivedStops.size());
    }

    /**
     * Checks that a request with no body is rejected
     */
    @Test 
    public void testEmptyBody() throws Exception {

        HttpResponse<String> response = sendRequest("POST","");

        Assert.assertEquals(400, response.statusCode());
        Assert.assertEquals(0, receivedStops.size());
    }

    /**
     * Checks that an invalid JSON request is rejected
     */
    @Test 
    public void testInvalidJson() throws Exception {

        HttpResponse<String> response = sendRequest("POST", "{wrong json}");

        Assert.assertEquals(400, response.statusCode());
        Assert.assertEquals(0, receivedStops.size());
    }

    /**
     * Checks that a null JSON request is rejected
     */
    @Test 
    public void testNullJson() throws Exception {

        HttpResponse<String> response = sendRequest("POST", "null");

        Assert.assertEquals(400, response.statusCode());
        Assert.assertEquals(0, receivedStops.size());
    }

    /**
     * Checks that a request without stoptype is rejected
     */
    @Test 
    public void testMissingType() throws Exception {

        String body = "{\"targetFloor\":12}";

        HttpResponse<String> response = sendRequest("POST", body);

        Assert.assertEquals(400, response.statusCode());
        Assert.assertTrue(response.body().contains("StopType is missing"));
        Assert.assertEquals(0, receivedStops.size());
    }

    /**
     * Checks that an invalid stop type is rejected
     */
    @Test 
    public void testInvalidStopType() throws Exception {

        String body = "{\"targetFloor\": 12, \"type\": \"UP\"}";

        HttpResponse<String> response = sendRequest("POST", body);

        Assert.assertEquals(400, response.statusCode());
        Assert.assertEquals(0, receivedStops.size());
    }

    /**
     * Checks that the response is JSON
     */
    @Test 
    public void testJsonResponse() throws Exception {

        String body = "{\"targetFloor\": 12, \"type\":\"DROP_OFF\"}";

        HttpResponse<String> response = sendRequest("POST",body);

        String contentType = response.headers().firstValue("Content-Type")
            .orElse("Missing");
        Assert.assertEquals("application/json", contentType);
    }

    /**
     * Checks that the response contains both the elevatorid and the status
     */
    @Test 
    public void testResponseBody() throws Exception {

        String body = "{\"targetFloor\": 12, \"type\":\"DROP_OFF\"}";

        HttpResponse<String> response = sendRequest("POST", body);

        ObjectMapper objectMapper = new ObjectMapper();
        StopResponse stopResponse = objectMapper.readValue(response.body(), StopResponse.class);

        Assert.assertEquals("A", stopResponse.getElevatorId());
        Assert.assertEquals(200, stopResponse.getStatus());
    }
}