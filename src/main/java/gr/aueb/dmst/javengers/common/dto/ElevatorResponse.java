package gr.aueb.dmst.javengers.common.dto;

public class ElevatorResponse {
    private String assignedElevator;
    private int status;

    public ElevatorResponse() {}

    public ElevatorResponse(String assignedElevator, int status) {
        this.assignedElevator = assignedElevator;
        this.status = status;
    }

    public void setAssignesElevator(String assignedElevator) {
        this.assignedElevator = assignedElevator;
    }

    public String getAssignedElevator() {
        return assignedElevator;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
