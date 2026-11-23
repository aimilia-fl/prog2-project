package gr.aueb.dmst.javengers.Evaluation.Generator;

import java.util.Random;

import gr.aueb.dmst.javengers.Evaluation.Handlers.EvaluationClient;
import gr.aueb.dmst.javengers.common.dto.PassengerRequest;

public class RandomGenerator {
    
    private final EvaluationClient evaluationClient;
    private final int totalFloors;
    private final Random random;
    private volatile boolean running;
    private int remainingRequests;

    public RandomGenerator(EvaluationClient evaluationClient, int totalFloors) {
        this.evaluationClient = evaluationClient;
        this.totalFloors = totalFloors + 1 ;
        this.random = new Random();
        this.running = false;
        this.remainingRequests = 0;
    }
    
    public void start(int totalRequests){
        remainingRequests = totalRequests;
        running = true;
        generateRequests();
    }

    public void generateRequests() {

        if (running == false || remainingRequests <= 0) {
           
            running = false;
            remainingRequests = 0;
           return;
        }

        int sourceFloor = random.nextInt(totalFloors);
        int desstinationFloor = random.nextInt(totalFloors);
        while (desstinationFloor == sourceFloor) {
            desstinationFloor = random.nextInt(totalFloors);
        }

        PassengerRequest passengerRequest = new PassengerRequest(sourceFloor, desstinationFloor);
        evaluationClient.sendPassengerRequestAsync(passengerRequest);
        remainingRequests--;
    }

    public void stop() {
        running = false;
        remainingRequests = 0;
    }

    public void RequestSentSuccessfully() {
        if (running == true && remainingRequests > 0) {
            generateRequests();
        }
    }

}
