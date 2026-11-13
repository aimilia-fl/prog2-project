package gr.aueb.dmst.javengers.elevator.model;

public class ElevatorConfig {
    private int minFloor;
    private int maxFloor;
    private double speed;
    private double maxWeight;
    public ElevatorConfig(int minFloor, int maxFloor, double speed, double maxWeight) {
        if (minFloor > maxFloor) {
            throw new IllegalArgumentException("Minimum floor must be less than maximum floor");
        }
        if (speed <= 0 || maxWeight <= 0) {
            throw new IllegalArgumentException("Speed and maximum weight must be positive");
        }
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.speed = speed;
        this.maxWeight = maxWeight;
    }
    public int getMinFloor() {
        return minFloor;
    }
    public int getMaxFloor() {
        return maxFloor;
    }
    public double getSpeed() {
        return speed;
    }
    public double getMaxWeight() {
        return maxWeight;
    }
}
