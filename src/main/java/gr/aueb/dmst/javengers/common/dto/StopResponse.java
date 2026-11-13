package gr.aueb.dmst.javengers.common.dto;

public class StopResponse {
    private String elevatorId;
    private int status;

    public StopResponse() {}

    public StopResponse(String elevatorId, int status) {
        this.elevatorId = elevatorId;
        this.status = status;
    }

    public void setElevatorId(String elevatorId) {
        this.elevatorId = elevatorId;
    }

    public String getElevatorId() {
        return elevatorId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
