package gr.aueb.dmst.javengers.Evaluation.Handlers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.aueb.dmst.javengers.common.dto.ConfigResponse;
import gr.aueb.dmst.javengers.common.dto.ElevatorResponse;
import gr.aueb.dmst.javengers.common.dto.PassengerRequest;

public class EvaluationClient {
    private final String CentralBaseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public EvaluationClient(String centralBaseUrl, HttpClient httpClient, ObjectMapper objectMapper) {
        this.CentralBaseUrl = centralBaseUrl;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public ConfigResponse getCentralConfig() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CentralBaseUrl + "/api/v1/central/config"))
                .header("Accept", "application/json")
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body() , ConfigResponse.class);
        } else {
            throw new RuntimeException("Failed to get central config: " + response.statusCode());
        }
    }

    public ElevatorResponse sendPassengerRequest(PassengerRequest passengerRequest) throws Exception {

        String requestBody = objectMapper.writeValueAsString(passengerRequest);
       
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CentralBaseUrl + "/api/v1/central/request"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body() , ElevatorResponse.class);
        } else {
            throw new RuntimeException("Failed to send passenger request: " + response.statusCode());
        }
    }
}
