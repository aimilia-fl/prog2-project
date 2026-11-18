package gr.aueb.dmst.javengers.central.handlers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gr.aueb.dmst.javengers.common.dto.ElevatorStatus;

public class StatusHandler {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public StatusHandler() {
    }

    public ElevatorStatus getElevatorStatus(String elevatorUrl) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(elevatorUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Σφάλμα HTTP");
                return null;
            }

            String body = response.body().trim();
            if (body.isEmpty()) {
                System.out.println("Κενό Json");
                return null;
            }

            ElevatorStatus status;
            try {
                status = objectMapper.readValue(body, ElevatorStatus.class);
            } catch (JsonProcessingException exception) {
                System.out.println("Λάθος Μορφή JSON");
                return null;
            }

            return status;

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return null;
        } catch (IOException exception) {
            return null;
        }
    }
}
