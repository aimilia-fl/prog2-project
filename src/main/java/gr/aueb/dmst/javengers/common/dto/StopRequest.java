package gr.aueb.dmst.javengers.common.dto;

public class StopRequest {
    private int targetFloor;

    public StopRequest() {}

    public StopRequest( int targetFloor) {
        this.targetFloor = targetFloor;
    }

    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }

    public int getTargetFloor() {
        return targetFloor;
    }
}
