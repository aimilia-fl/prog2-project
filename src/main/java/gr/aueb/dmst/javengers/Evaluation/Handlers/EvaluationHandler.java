package gr.aueb.dmst.javengers.Evaluation.Handlers;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import gr.aueb.dmst.javengers.Evaluation.Generator.RandomGenerator;
import gr.aueb.dmst.javengers.Evaluation.MathEvaluator.MathEvaluator;
import gr.aueb.dmst.javengers.Evaluation.Storage.DataStorage;

public class EvaluationHandler implements HttpHandler {
    
    private final ObjectMapper ObjectMapper;
    private final RandomGenerator RandomGenerator;
    private final MathEvaluator MathEvaluator;
    private final DataStorage DataStorage;

    public EvaluationHandler(ObjectMapper objectMapper, RandomGenerator randomGenerator, MathEvaluator mathEvaluator, DataStorage dataStorage) {
        this.ObjectMapper = objectMapper;
        this.RandomGenerator = randomGenerator;
        this.MathEvaluator = mathEvaluator;
        this.DataStorage = dataStorage;
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("POST".equalsIgnoreCase(method) && "/api/v1/evaluation".equals(path)) {
                handleStartScenario(exchange);
            } else if ("POST".equalsIgnoreCase(method) && "/api/v1/evaluation/stop".equals(path)) {
                handleResetScenario(exchange);
            } else if ("GET".equalsIgnoreCase(method) && "/api/v1/evaluation/results".equals(path)) {
                handleGetMetrics(exchange);
            } else {
                System.err.println("Endpoint not found: " + path);
                sendResponse(exchange, 404, "Not Found");
            }
        } catch (IOException e) {
            System.err.println("Error occurred while handling request: " + e.getMessage());
            sendResponse(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }
    //Not completed code
    private void handleStartScenario(HttpExchange exchange) throws IOException {
        // Logic to start the evaluation scenario
    }
    private void handleResetScenario(HttpExchange exchange) throws IOException {
        // Logic to reset the evaluation scenario
    }
    private void handleGetMetrics(HttpExchange exchange) throws IOException {
        // Logic to get evaluation metrics
    }
    private void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        
    }
    
}
