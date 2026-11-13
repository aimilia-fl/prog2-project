package gr.aueb.dmst.javengers.common.dto;

public class PassengerRequest {
    private int sourceFloor;
    private int destinationFloor;

    public PassengerRequest() {}

    public PassengerRequest(int sourceFloor, int destinationFloor) {
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
    }

    public void setSourceFloor(int sourceFloor) {
        this.sourceFloor = sourceFloor;
    }

    public int getSourceFloor() {
        return sourceFloor;
    }

    public void setDestinationFloor(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }
}
