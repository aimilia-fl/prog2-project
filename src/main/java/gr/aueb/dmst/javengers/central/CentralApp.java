package gr.aueb.dmst.javengers.central;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import gr.aueb.dmst.javengers.central.handlers.RequestHandler;
import gr.aueb.dmst.javengers.common.dto.ElevatorStatus;
import gr.aueb.dmst.javengers.common.dto.PassengerRequest;

public class CentralApp {

    private static final int PORT = 8080;
    private static final int ELEVATOR_COUNT = 20;
    private static final int ELEVATOR_PORT = 8081;

    private final Map<String,String> elevatorEndpoints;
    private final ObjectMapper objectMapper;
    private final HttpClient client;
    
    public CentralApp(){
        this.elevatorEndpoints = createElevatorEndpoints(ELEVATOR_COUNT);
        this.objectMapper = new ObjectMapper();
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();

    }

    private ElevatorStatus fetchElevatorStatus(String url){
        
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url + "/api/v1/central/requests"))
            .header("Content-Type","application/json")
            .GET()
            .build();

            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
            
            if(!(response.statusCode() == 200)){
                return null;
            }
            return objectMapper.readValue(response.body(), ElevatorStatus.class);
        } catch (IOException e){
            return null;
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            return null;
        }
        
    }

    private boolean sendToElevator(String baseUrl, PassengerRequest passengerRequest){
        
        try {
            String json = objectMapper.writeValueAsString(passengerRequest);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/v1/elevator/dispatch"))
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            return (response.statusCode() == 200);

        } catch (Exception e) {
            return false;
        }
    }
    public static void main(String[] args) throws IOException {
    
        CentralApp app = new CentralApp();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT),0);
        server.createContext("/api/v1/central/requests", new RequestHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Central App is running on Port: " + server.getAddress().getPort());
        
    }

    private static Map<String, String> createElevatorEndpoints(int count) {
        Map<String, String> endpoints = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String id = String.valueOf((char) ('A' + i));
            endpoints.put(id, "http://localhost:" + (ELEVATOR_PORT + i));
        }
        return Collections.unmodifiableMap(endpoints);
    }
}
