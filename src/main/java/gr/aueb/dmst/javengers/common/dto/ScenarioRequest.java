package gr.aueb.dmst.javengers.common.dto;

public class ScenarioRequest {
   private int totalElevators;
   private int totalFloors;
   private String passengerTrafficScenario;
   private int durationMinutes;

    public ScenarioRequest() {}

    public ScenarioRequest(int totalElevators, int totalFloors, String passengerTrafficScenario, int durationMinutes) {
        this.totalElevators = totalElevators;
        this.totalFloors = totalFloors;
        this.passengerTrafficScenario = passengerTrafficScenario;
        this.durationMinutes = durationMinutes;
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

    public String getPassengerTrafficScenario() {
        return passengerTrafficScenario;
    }

    public void setPassengerTrafficScenario(String passengerTrafficScenario) {
        this.passengerTrafficScenario = passengerTrafficScenario;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}
