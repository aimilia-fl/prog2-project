package gr.aueb.dmst.javengers.central;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sun.net.httpserver.HttpServer;

import gr.aueb.dmst.javengers.common.dto.PassengerRequest;

public class TestCentralApp {

    private final int PORT = 8081;

    private String elevatorStatusJson;
    private int dispatchStatusCode;

    private HttpServer server;
    private CentralApp app;

    


    @Before
    public void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT),0);
        this.elevatorStatusJson = "{\"elevatorId\":\"A\",\"operatingState\":\"NORMAL\",\"currentFloor\":2,\"direction\":\"IDLE\",\"currentWeight\":0.0,\"nextStops\":[]}";
        this.dispatchStatusCode = 200;

        server.createContext("/api/v1/elevator/status", exchange -> {
            byte[] bytes = elevatorStatusJson.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type","application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        server.createContext("/api/v1/elevator/dispatch", exchange -> {
            byte[] bytes = "{}".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(dispatchStatusCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        server.start();
        app = new CentralApp();
    }

    @After 
    public void shutDown(){
        server.stop(0);
    }


    @Test 
    public void testAssignElevatorSuccess(){
        PassengerRequest passengerRequest = new PassengerRequest(1,200);
        Optional<String> elevator;
        elevator = app.assignElevator(passengerRequest);

        Assert.assertTrue(elevator.isPresent());
        Assert.assertEquals("A", elevator.get());
    }

    @Test 
    public void testAssignElevatorReturnsEmpty(){
        elevatorStatusJson = "{\"elevatorId\":\"A\",\"operatingState\":\"OUT_OF_SERVICE\",\"currentFloor\":2,\"direction\":\"IDLE\",\"currentWeight\":0.0,\"nextStops\":[]}";
        PassengerRequest passengerRequest = new PassengerRequest(1,189);

        Optional<String> elevator = app.assignElevator(passengerRequest);
        Assert.assertTrue(elevator.isEmpty());
    }

    @Test 
    public void testAssignElevatorOverWeight(){
        elevatorStatusJson = "{\"elevatorId\":\"A\",\"operatingState\":\"NORMAL\",\"currentFloor\":2,\"direction\":\"IDLE\",\"currentWeight\":500.0,\"nextStops\":[]}";
        PassengerRequest passengerRequest = new PassengerRequest(70,199);
        Optional<String> assigned = app.assignElevator(passengerRequest);

        Assert.assertTrue(assigned.isEmpty());
    }

    @Test
    public void testAssignElevatorWhenDispatchFails() {
        dispatchStatusCode = 500;
        PassengerRequest request = new PassengerRequest(1, 67);

        Optional<String> assigned = app.assignElevator(request);
        Assert.assertTrue(assigned.isEmpty());
    }
    
}
