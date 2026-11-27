package gr.aueb.dmst.javengers.common.dto;

import gr.aueb.dmst.javengers.elevator.model.StopType;

public class StopRequest {
    private int targetFloor;
    private StopType type;

    public StopRequest() {}

    public StopRequest( int targetFloor, StopType type) {
        this.targetFloor = targetFloor;
        this.type = type;
    }

    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public StopType getType() {
        return type;
    }

    public void setType(StopType type) {
        this.type = type;
    }
}
