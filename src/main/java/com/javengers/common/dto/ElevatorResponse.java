package com.javengers.common.dto;

public class ElevatorResponse {
    private char assignedElevator;
    private int status;

    public ElevatorResponse() {}

    public ElevatorResponse(char assignedElevator, int status) {
        this.assignedElevator = assignedElevator;
        this.status = status;
    }

    public void setAssignesElevator(char assignedElevator) {
        this.assignedElevator = assignedElevator;
    }

    public char getAssignedElevator() {
        return assignedElevator;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
