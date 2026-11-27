package gr.aueb.dmst.javengers.elevator.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
 
import gr.aueb.dmst.javengers.common.dto.StopRequest;
import gr.aueb.dmst.javengers.common.dto.StopResponse;
import gr.aueb.dmst.javengers.common.exceptions.InvalidStopException;
import gr.aueb.dmst.javengers.elevator.model.Stop;
import gr.aueb.dmst.javengers.elevator.model.StopType;


/**
 * Handles POST requests that add a stop to the elevator
 */


public class StopsHandler implements HttpHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String elevatorId; 
    private final StopAdder stopAdder;
    

/**
 * Creates the handler
 * 
 * @param elevatorId the id of the elevator
 * @param stopAdder adds each valid stop
 */

public StopsHandler(String elevatorId, StopAdder stopAdder) {
    this.elevatorId = elevatorId;
    this.stopAdder = stopAdder;
}

@Override 
public void handle(HttpExchange exchange) throws IOException {
    if (!"POST".equals(exchange.getRequestMethod())) {
        sendError(exchange, 405, "This method is not allowed. Please use POST");
        return;
    }

    InputStream input = exchange.getRequestBody();
    String body = new String(input.readAllBytes(), StandardCharsets.UTF_8);

    StopRequest request;

    try {
        request = objectMapper.readValue(body, StopRequest.class);
    } catch(JsonProcessingException e) {
        sendError(exchange, 400, "Wrong JSON format");
        return;
    }

    if (request == null) {
        sendError(exchange, 400, "Empty request" );
        return;
    }

    StopType type = request.getType();
    if (type == null) {
        sendError(exchange, 400, "StopType is missing");
        return;
    }

    try {
        Stop stop = new Stop(request.getTargetFloor(), type);
        stopAdder.addStop(stop);
    } catch (InvalidStopException e) {
        sendError(exchange, 400, e.getMessage());
        return;
    }

    StopResponse stopResponse = new StopResponse(elevatorId, 200);
    sendResponse(exchange, 200, objectMapper.writeValueAsString(stopResponse));
}  

private void sendError(HttpExchange exchange, int code, String message) throws IOException {
    String errorJson = objectMapper.writeValueAsString(Map.of("error", message));
    sendResponse(exchange, code, errorJson);
}
 private void sendResponse(HttpExchange exchange, int code, String response) throws IOException {
     byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
     exchange.getResponseHeaders().set("Content-Type", "application/json");
      exchange.sendResponseHeaders(code, bytes.length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(bytes);
      }
    }
}
