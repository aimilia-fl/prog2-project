package gr.aueb.dmst.javengers.elevator.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import gr.aueb.dmst.javengers.common.dto.ElevatorStatus;

/**
 * Handles GET requests for the elevator status.
 */
public class StatusHandler implements HttpHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Supplier<ElevatorStatus> statusSupplier;

    /**
     * Creates the handler.
     * @param statusSupplier gives us the current status.
     */

    public StatusHandler(Supplier<ElevatorStatus> statusSupplier) {
        this.statusSupplier = statusSupplier;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405,
                     "{\"error\": \"This method is not allowed. Please use GET\"}");
            return;
        }

        ElevatorStatus elevatorStatus = statusSupplier.get();
        if (elevatorStatus == null) {
            sendResponse(exchange, 500, "{\"error\": \"Status is not available\"}");
            return;

        }

        sendResponse(exchange, 200, objectMapper.writeValueAsString(elevatorStatus));
    }


        private void sendResponse(HttpExchange exchange, int code, String response)
                throws IOException {
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(code, bytes.length);
            try(OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

    }
