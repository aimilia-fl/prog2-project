package gr.aueb.dmst.javengers.common.dto;

public class ConfigResponse {
    private int totalElevators;
    private int totalFloors;

    public ConfigResponse() {}

    public ConfigResponse(int totalElevators, int totalFloors) {
        this.totalElevators = totalElevators;
        this.totalFloors = totalFloors;
    }

    public int getTotalElevators() {
        return totalElevators;
    }

    public void setTotalElevators(int totalElevators) {
        this.totalElevators = totalElevators;
    }

    public int getTotalFloors() {
        return totalFloors;
    }

    public void setTotalFloors(int totalFloors) {
        this.totalFloors = totalFloors;
    }
}
