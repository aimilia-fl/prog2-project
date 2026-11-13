package gr.aueb.dmst.javengers.common.dto; 


import gr.aueb.dmst.javengers.elevator.model.Direction;
import gr.aueb.dmst.javengers.elevator.model.DoorState;
import gr.aueb.dmst.javengers.elevator.model.OperatingState;
import java.util.List; 

public class ElevatorStatus {
    
    private String elevatorId;
    private OperatingState operatingState;
    private int currentFloor;
    private Direction direction;
    private DoorState doorState;
    private double currentWeight; 
    private List<Integer> nextStops;

    public ElevatorStatus() {}

    public ElevatorStatus(String elevatorId, OperatingState operatingState, int currentFloor, Direction direction,
     DoorState doorState, double currentWeight, List<Integer> nextStops) {
        this.elevatorId = elevatorId;
        this.operatingState = operatingState;
        this.currentFloor = currentFloor;
        this.direction =  direction;
        this.doorState = doorState;
        this.currentWeight = currentWeight;
        this.nextStops = nextStops;
     }

    public String getElevatorId() {
        return elevatorId;
    }

    public void setElevatorId(String elevatorId) {
        this.elevatorId = elevatorId;
    }

    public OperatingState getOperatingState() {
        return operatingState;
    }

    public void setOperatingState(OperatingState operatingState) {
        this.operatingState = operatingState;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrectFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public DoorState getDoorState() {
        return doorState;
    }

    public void setDoorState(DoorState doorState) {
        this.doorState = doorState;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public List<Integer> getNextStops() {
        return nextStops;
    }

    public void setNextStops(List<Integer> nextStops) {
        this.nextStops = nextStops;
    }


    }

