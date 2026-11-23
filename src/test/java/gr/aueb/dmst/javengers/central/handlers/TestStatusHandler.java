package gr.aueb.dmst.javengers.central.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import gr.aueb.dmst.javengers.common.dto.ElevatorStatus;
import gr.aueb.dmst.javengers.elevator.model.Direction;
import gr.aueb.dmst.javengers.elevator.model.DoorState;
import gr.aueb.dmst.javengers.elevator.model.OperatingState;

public class TestStatusHandler {
    
    private final String path = "/api/v1/elevators/A/status";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private HttpServer mockServer;
    private StatusHandler statusHandler;

    @Before 
    public void setUp() {
        statusHandler = new StatusHandler();
    }

    @After
    public void shutDown() {
        if (mockServer != null) {
            mockServer.stop(0);
        }
    }

    private String startMockServer(int statusCode, String responseBody) throws IOException {

        mockServer = HttpServer.create(new InetSocketAddress(0), 0 );

        mockServer.createContext(path, exchange -> {
            byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8); 
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) { 
                os.write(bytes);
            }
        });

        mockServer.start();
        int port = mockServer.getAddress().getPort();
        return "http://localhost:" + port + path;
    }

    @Test 
    public void testValidElevatorStatus() throws Exception {

        ElevatorStatus expected = new ElevatorStatus(
            "A", OperatingState.values()[0],4,Direction.values()[0],
            DoorState.values()[0],73, Arrays.asList(6)
        );

        String jsonBody = objectMapper.writeValueAsString(expected);
        String url = startMockServer(200, jsonBody);
        System.out.println("GET " + url);

        ElevatorStatus actual = statusHandler.getElevatorStatus(url);

        System.out.println("-------------------------");
        System.out.println(actual);

        Assert.assertNotNull(actual);
        Assert.assertEquals("A", actual.getElevatorId());
        Assert.assertEquals(expected.getOperatingState(), actual.getOperatingState());
        Assert.assertEquals(4, actual.getCurrentFloor());
        Assert.assertEquals(expected.getDirection(), actual.getDirection());
        Assert.assertEquals(expected.getDoorState(), actual.getDoorState());
        Assert.assertEquals(73, actual.getCurrentWeight(), 0.001);
        Assert.assertEquals(Arrays.asList(6), actual.getNextStops());
    }

    @Test
    public void testHttpErrorReturnsNull() throws Exception {
        String url = startMockServer(500, "{\"error\": \"Server Error\"}");
        ElevatorStatus actual = statusHandler.getElevatorStatus(url);
        Assert.assertNull(actual);
    }

    @Test 
    public void testEmptyJsonBodyReturnsNull() throws Exception {
        String url = startMockServer(200, "");
        ElevatorStatus actual = statusHandler.getElevatorStatus(url);
        Assert.assertNull(actual);
    }

    @Test 
    public void testInvalidJsonFormatReturnsNull() throws Exception {
        String url = startMockServer(200, "{wrongJSONFormat}");
        System.out.println("GET " + url);
        ElevatorStatus actual = statusHandler.getElevatorStatus(url);
        System.out.println("------------------");
        System.out.println("Result: " + actual);
        Assert.assertNull(actual);
    }

    @Test 
    public void testConnectionRefusedReturnsNull() {
        String wrongUrl = "http://localhost:59999" + path;
        ElevatorStatus actual = statusHandler.getElevatorStatus(wrongUrl);
        Assert.assertNull(actual);
    }
}
