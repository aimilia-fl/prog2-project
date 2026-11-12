package gr.aueb.dmst.javengers.central.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import gr.aueb.dmst.javengers.common.dto.PassengerRequest;

public class RequestHandler implements HttpHandler{

    public RequestHandler(){}
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException{

        if(!"POST".equals(exchange.getRequestMethod())){
            responseMessage(exchange, 405,"{\"error\": \"This method is not allowed. Please use POST\"}");
            return;
        }
        InputStream input = exchange.getRequestBody();
        String body = new String(input.readAllBytes(),StandardCharsets.UTF_8).trim();

        if (body.isEmpty()){
            responseMessage(exchange, 400, "{\"error\": \"The request body is empty\"}");
            return;
        }

        PassengerRequest request;

        try {
            request = objectMapper.readValue(body, PassengerRequest.class);
        } catch (JsonProcessingException e) {
            responseMessage(exchange, 400, "{\"error\": \"Wrong JSON format\"}");
            return;
        }

        if(request == null){
            responseMessage(exchange, 400,"{\"error\": \"Wrong JSON format or missing fields\"}");
            return;
        }

        if(request.getSourceFloor() == request.getDestinationFloor()){
            responseMessage(exchange, 400, "{\"error\": \"SourceFloor and DestinationFloor can't be the same. Please use your feet\"}");
            return;
        }

        if(request.getSourceFloor()<0 || request.getDestinationFloor()<0){
            responseMessage(exchange, 400, "{\"error\": \"Floors cannot be negative\"}");
                return;
        }

    }


    public void responseMessage(HttpExchange exchange, int statusCode, String response) throws IOException{
        exchange.getResponseHeaders().set("Content-Type","application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try(OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}