package gr.aueb.dmst.javengers.elevator.model;

public class Stop {
    public int targetFloor;
    public StopType stopType;
    public Stop(int targetFloor, StopType stopType) {
        this.targetFloor = targetFloor;
        this.stopType = stopType;
    }
    public int getTargetFloor() {
        return targetFloor;
    }
    public StopType getStopType() {
        return stopType;
    }
}
