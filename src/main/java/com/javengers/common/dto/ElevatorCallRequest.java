package com.javengers.common.dto;

public class ElevatorCallRequest {
    
    private int sourceFloor;
    private int destinationFloor;

    public ElevatorCallRequest(int sourceFloor, int destinationFloor){
        this.destinationFloor = destinationFloor;
        this.sourceFloor = sourceFloor;
    }
    
    public int getSourceFloor(){
        return this.sourceFloor;
    }

    public int getDestinationFloor(){
        return this.destinationFloor;
    }

    public void setSourceFloor(int sourceFloor){
        this.sourceFloor = sourceFloor;
    }

    public void setDestinationFloor(int destinationFloor){
        this.destinationFloor = destinationFloor;
    }

    @Override
    public String toString(){
        return String.format("{ \"sourceFloor\": %d, \"destinationFloor\": %d }", sourceFloor, destinationFloor);
    }
}
