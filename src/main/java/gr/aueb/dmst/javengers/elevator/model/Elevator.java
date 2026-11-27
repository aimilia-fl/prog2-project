package gr.aueb.dmst.javengers.elevator.model;

import gr.aueb.dmst.javengers.common.exceptions.InvalidStopException;

public class Elevator {
    private String elevatorId;
    private int currentFloor;
    private DoorState doorState;
    private StopQueue stopQueue;
    private OperatingState operatingState;
    private ElevatorConfig config;
    private Direction direction;

    public Elevator(String elevatorId, int currentFloor, ElevatorConfig config) {
        if (elevatorId == null || elevatorId.isBlank()) {
            throw new IllegalArgumentException("The elevatorId can not be empty");
        }

        if (config == null) {
            throw new IllegalArgumentException("The config of an elevator can not be null");
        }

        if (currentFloor < config.getMinFloor() || currentFloor > config.getMaxFloor()) {
            throw new IllegalArgumentException("It is impossible the current floor to be smaller than the minFloor or bigger than the maxFloor");
        }

        this.elevatorId = elevatorId;
        this.currentFloor = currentFloor;
        this.config = config;
        this.direction = Direction.IDLE;
        this.doorState = DoorState.CLOSED;
        this.operatingState = OperatingState.NORMAL;
        this.stopQueue = new StopQueue();
    }

    public String getElevatorId() {
        return elevatorId;
    }
    
    public int getCurrentFloor() {
        return currentFloor;
    }

    public DoorState getDoorState() {
        return doorState;
    }

    public Direction getDirection() {
        return direction;
    }

    public OperatingState getOperatingState() {
        return operatingState;
    }

    public int getRemainingStops() {
        return stopQueue.getRemainingStops();
    }

    public ElevatorConfig getElevatorConfig() {
        return config;
    }

    public void addStop(Stop stop) throws InvalidStopException{
        if (stop == null) {
            throw new IllegalArgumentException("The stop cannot be null");
        }

        if (stop.getTargetFloor() < config.getMinFloor() || stop.getTargetFloor() > config.getMaxFloor()) {
            throw new IllegalArgumentException("This stop does not excist");
        }

        stopQueue.addStop(stop);
    }
}
