package gr.aueb.dmst.javengers.central.dispatch;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import gr.aueb.dmst.javengers.common.dto.ElevatorStatus;
import gr.aueb.dmst.javengers.common.dto.PassengerRequest;
import gr.aueb.dmst.javengers.elevator.model.Direction;
import gr.aueb.dmst.javengers.elevator.model.OperatingState;
import gr.aueb.dmst.javengers.elevator.model.ElevatorConfig;

public class DispatchAlgorithm implements DispatchStrategy {

    private static final int DISTANCE_PENALTY = 100;
    private static final int SAME_DIRECTION_PENALTY = 200;
    private static final int DIFFERENT_DIRECTION_PENALTY = 400;
    private static final int STOP_PENALTY = 80;

    private final double maxWeight;
    private final WeightGenerator weightGenerator;

    public DispatchAlgorithm(ElevatorConfig config) {
        this(config, new WeightGenerator());
    }

    public DispatchAlgorithm(ElevatorConfig config, WeightGenerator weightGenerator) {
        this.maxWeight = config.getMaxWeight();
        this.weightGenerator = weightGenerator;
    }

    @Override 
    public Optional<String> selectElevator(PassengerRequest request, Map<String, ElevatorStatus> fleetStatuses) {
        double incomingWeight = weightGenerator.nextWeight();
        return selectElevator(request, fleetStatuses, incomingWeight);
    }

    public Optional<String> selectElevator(PassengerRequest request, Map<String, ElevatorStatus> fleetStatuses, double incomingWeight) {
        if (request == null || fleetStatuses == null || fleetStatuses.isEmpty()) {
            return Optional.empty();
        }

        String bestId = null;
        int bestScore = Integer.MAX_VALUE;

        for (Map.Entry<String, ElevatorStatus> entry : fleetStatuses.entrySet()) {
            String elevatorId = entry.getKey();
            ElevatorStatus status = entry.getValue();

            if(!isEligible(status, incomingWeight)) {
                continue;
            }

            int score = calculateScore(status, request);

            if (score < bestScore) {
                bestScore = score;
                bestId = elevatorId;
             } else if (score == bestScore && bestId != null && elevatorId.compareTo(bestId) < 0) {
                bestId = elevatorId;
                bestScore = score;
             }
        }

        if (bestId == null) {
            return Optional.empty();
        }
        return Optional.of(bestId);
    }

    private boolean isEligible(ElevatorStatus status, double incomingWeight) {
        if (status == null) {
            return false;
        }
        if (status.getOperatingState() != OperatingState.NORMAL) {
            return false;
        }
        double elevatorWeight = status.getCurrentWeight() + incomingWeight;
        if (elevatorWeight > maxWeight) {
            return false;
        }
        return true;
    }

    private int calculateScore(ElevatorStatus status, PassengerRequest request) {
        int currentFloor = status.getCurrentFloor();
        int sourceFloor = request.getSourceFloor();
        int destinationFloor = request.getDestinationFloor();

        Direction requestDirection;
        if (destinationFloor > sourceFloor) {
            requestDirection = Direction.UP;
        } else {
            requestDirection = Direction.DOWN;
        }

        Direction elevatorDirection = status.getDirection();

        int score = Math.abs(currentFloor - sourceFloor) * DISTANCE_PENALTY;

        if (elevatorDirection == Direction.IDLE) {
            score = score + 0;
        } else if (elevatorDirection == requestDirection) {
            boolean approval =((requestDirection == Direction.UP && currentFloor <= sourceFloor)
            || (requestDirection == Direction.DOWN && currentFloor >= sourceFloor));
            if (approval) {
                score = score + SAME_DIRECTION_PENALTY;
            } else {
                score = score + DIFFERENT_DIRECTION_PENALTY;
            }
        } else {
            score = score + DIFFERENT_DIRECTION_PENALTY;
        }

        List<Integer> nextStops = status.getNextStops();
        int stopCount;
        if (nextStops == null){
            stopCount = 0;
        } else {
            stopCount = nextStops.size();
        }

        score = score + stopCount * STOP_PENALTY;
        return score;
    }
}