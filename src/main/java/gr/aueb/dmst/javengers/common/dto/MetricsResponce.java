package gr.aueb.dmst.javengers.common.dto;

public class MetricsResponce {
    private String ScenarioId;
    private String status;
    private Metrics metrics;

    public MetricsResponce() {}

    public MetricsResponce(String scenarioId, String status, Metrics metrics) {
        ScenarioId = scenarioId;
        this.status = status;
        this.metrics = metrics;
    }

    public String getScenarioId() {
        return ScenarioId;
    }

    public void setScenarioId(String scenarioId) {
        ScenarioId = scenarioId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    public static class Metrics {
        private double averageWaitTimeSeconds;
        private double averageJourneyTimeSeconds;
        private double maxWaitTimeSeconds;
        private double overloadedRejections;
        private int totalPassengersServed;

        public Metrics() {}

        public Metrics(double averageWaitTimeSeconds, double averageJourneyTimeSeconds, double maxWaitTimeSeconds, double overloadedRejections, int totalPassengersServed) {
            this.averageWaitTimeSeconds = averageWaitTimeSeconds;
            this.averageJourneyTimeSeconds = averageJourneyTimeSeconds;
            this.maxWaitTimeSeconds = maxWaitTimeSeconds;
            this.overloadedRejections = overloadedRejections;
            this.totalPassengersServed = totalPassengersServed;
        }

        public double getAverageWaitTimeSeconds() {
            return averageWaitTimeSeconds;
        }

        public void setAverageWaitTimeSeconds(double averageWaitTimeSeconds) {
            this.averageWaitTimeSeconds = averageWaitTimeSeconds;
        }

        public double getAverageJourneyTimeSeconds() {
            return averageJourneyTimeSeconds;
        }

        public void setAverageJourneyTimeSeconds(double averageJourneyTimeSeconds) {
            this.averageJourneyTimeSeconds = averageJourneyTimeSeconds;
        }

        public double getMaxWaitTimeSeconds() {
            return maxWaitTimeSeconds;
        }

        public void setMaxWaitTimeSeconds(double maxWaitTimeSeconds) {
            this.maxWaitTimeSeconds = maxWaitTimeSeconds;
        }

        public double getOverloadedRejections() {
            return overloadedRejections;
        }

        public void setOverloadedRejections(double overloadedRejections) {
            this.overloadedRejections = overloadedRejections;
        }

        public int getTotalPassengersServed() {
            return totalPassengersServed;
        }

        public void setTotalPassengersServed(int totalPassengersServed) {
            this.totalPassengersServed = totalPassengersServed;
        }
    }
}
