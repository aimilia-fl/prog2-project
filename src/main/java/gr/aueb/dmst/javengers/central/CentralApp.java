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
import gr.aueb.dmst.javengers.central.handlers.StatusHandler;
import gr.aueb.dmst.javengers.common.dto.ElevatorStatus;
import gr.aueb.dmst.javengers.common.dto.PassengerRequest;
import gr.aueb.dmst.javengers.elevator.model.OperatingState;


/**
 * Central coordinator service that receives passenger requests and dispatches
 * them to the most suitable elevator in the fleet.
 */
public class CentralApp {

    private static final int PORT = 8080;
    private static final int ELEVATOR_COUNT = 20;
    private static final int ELEVATOR_PORT = 8081;

    private final Map<String,String> elevatorEndpoints;
    private final ObjectMapper objectMapper;
    private final HttpClient client;

/**
* Constructs a new @code CentralApp instance, initializing the elevator
* endpoint mappings, JSON serializer, and HTTP client.
 */

    public CentralApp(){
        this.elevatorEndpoints = createElevatorEndpoints(ELEVATOR_COUNT);
        this.objectMapper = new ObjectMapper();
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();

    }
    
    /**
     * Evaluates available elevators and assigns the given passenger request to
     * the nearest operational elevator.
     *
     * @param passengerRequest the passenger trip request containing source and destination floors
     * @return the identifier of the assigned elevator (e.g., {@code "A"}), or {@code null} if none is available
     */

    public String assignElevator(PassengerRequest passengerRequest){
        String bestElevatorId = null;
        StatusHandler handler = new StatusHandler();
        int minDistance = Integer.MAX_VALUE;

        for (Map.Entry<String,String> entry : elevatorEndpoints.entrySet()){

            ElevatorStatus status = handler.getElevatorStatus(entry.getValue());
            if ((status == null) || (OperatingState.NORMAL != status.getOperatingState())){
                continue;
            }

            // Line below will be deleted after dispatch algorithm is implemented
            int distance = Math.abs(status.getCurrentFloor() - passengerRequest.getDestinationFloor());
            if (distance < minDistance){
                minDistance = distance;
                bestElevatorId = entry.getKey();
            }
        }
            if (bestElevatorId == null) {
                return null;
            }

            boolean elevatorSent = sendToElevator(elevatorEndpoints.get(bestElevatorId),passengerRequest);
            return elevatorSent ? bestElevatorId : null;
    }


    /**
     * Sends a passenger dispatch request via HTTP POST to the specified elevator service.
     *
     * @param baseUrl          the base URL of the target elevator service
     * @param passengerRequest the passenger request payload to dispatch
     * @return {@code true} if the elevator accepted the dispatch (HTTP 200), {@code false} otherwise
     */
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


    /**
     * Starts the central HTTP server and registers the passenger request endpoint.
     *
     * @param args command-line arguments (not used)
     * @throws IOException if an I/O error occurs when creating or starting the HTTP server
     */
    public static void main(String[] args) throws IOException {
    
        CentralApp app = new CentralApp();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT),0);
        server.createContext("/api/v1/central/requests", new RequestHandler(app));
        server.setExecutor(null);
        server.start();
        System.out.println("Central App is running on Port: " + server.getAddress().getPort());
        
    }



    /**
     * Creates an unmodifiable mapping of alphabetical elevator IDs to their local HTTP base URLs.
     *
     * @param count the total number of elevators to register
     * @return an unmodifiable map pairing each elevator ID with its endpoint URL
     */
    private static Map<String, String> createElevatorEndpoints(int count) {
        Map<String, String> endpoints = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String id = String.valueOf((char) ('A' + i));
            endpoints.put(id, "http://localhost:" + (ELEVATOR_PORT + i));
        }
        return Collections.unmodifiableMap(endpoints);
    }
}
