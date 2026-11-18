package gr.aueb.dmst.javengers.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;



public class TestUserInterface{
    
    private HttpServer server;
    private static final String PATH = "/api/v1/central/requests";

    @Before
    public void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress(8080),0);
        server.createContext(PATH,new TestHandler());
        server.start();
    }

    @After
    public void shutDown(){
        server.stop(0);
    }

    @Test
    public void testSendRequestSuccess() throws Exception {
        UserInterface ui = new UserInterface(1);
        String result = ui.sendRequest(5);
        Assert.assertEquals("Assigned Elevator: Elevator-1\nGo to Elevator No: Elevator-1", result);
    }

    @Test
    public void testSendInvalidRequest() throws Exception{
        UserInterface ui = new UserInterface(1);
        String result = ui.sendRequest(-1);
        Assert.assertEquals("Please try again. Invalid Request", result);
    }

    @Test
    public void testConnectionFail(){
        server.stop(0);
        UserInterface ui = new UserInterface(1);
        String result = ui.sendRequest(19);
        Assert.assertEquals("Connection Failed", result);
    }

    private static class TestHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            if (body.contains("-")) {
                reply(exchange, 400, "{\"error\": \"Invalid Request\"}");
            } else {
                reply(exchange, 200, "{\"status\": 200, \"assignedElevator\": \"Elevator-1\"}");
            }
        }

        private void reply(HttpExchange exchange, int code, String json) throws IOException {
            byte[] data = json.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(code, data.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(data);
            }
        }
    }
}