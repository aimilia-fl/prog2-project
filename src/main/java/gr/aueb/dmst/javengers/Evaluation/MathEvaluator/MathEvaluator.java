package gr.aueb.dmst.javengers.Evaluation.MathEvaluator;

import java.util.Iterator;
import java.util.List;

import gr.aueb.dmst.javengers.common.dto.MetricsResponse;

/**
 * Calculates the performance metrics collected during an evaluation scenario.
 */
public class MathEvaluator {

    /**
     * Calculates the average wait and journey times, maximum wait time, and
     * passenger count for a scenario.
     *
     * @param waitTimesSeconds passenger wait times in seconds
     * @param journeyTimesSeconds passenger journey times in seconds
     * @param overloadedRejections number of requests rejected due to overload
     * @return the calculated scenario metrics
     * @throws IllegalArgumentException if the inputs are inconsistent or invalid
     */
    public MetricsResponse.Metrics evaluate(
            List<Double> waitTimesSeconds,
            List<Double> journeyTimesSeconds,
            int overloadedRejections) {
        if (waitTimesSeconds == null || journeyTimesSeconds == null) {
            throw new IllegalArgumentException("Time lists cannot be null");
        }
        if (waitTimesSeconds.size() != journeyTimesSeconds.size()) {
            throw new IllegalArgumentException(
                    "Each served passenger must have a wait time and a journey time");
        }
        if (overloadedRejections < 0) {
            throw new IllegalArgumentException("Overload rejections cannot be negative");
        }

        Iterator<Double> waitTimes = waitTimesSeconds.iterator();
        Iterator<Double> journeyTimes = journeyTimesSeconds.iterator();
        double totalWaitTime = 0.0;
        double totalJourneyTime = 0.0;
        double maxWaitTime = 0.0;
        int passengerCount = 0;

        while (waitTimes.hasNext()) {
            double waitTime = requireValidTime(waitTimes.next(), "wait", passengerCount);
            double journeyTime = requireValidTime(
                    journeyTimes.next(), "journey", passengerCount);

            totalWaitTime += waitTime;
            totalJourneyTime += journeyTime;
            if (!Double.isFinite(totalWaitTime) || !Double.isFinite(totalJourneyTime)) {
                throw new IllegalArgumentException("The total time exceeds the supported range");
            }
            maxWaitTime = Math.max(maxWaitTime, waitTime);
            passengerCount++;
        }

        double averageWaitTime = average(totalWaitTime, passengerCount);
        double averageJourneyTime = average(totalJourneyTime, passengerCount);
        return new MetricsResponse.Metrics(
                averageWaitTime,
                averageJourneyTime,
                maxWaitTime,
                overloadedRejections,
                passengerCount);
    }

    private double requireValidTime(Double time, String type, int passengerIndex) {
        if (time == null || !Double.isFinite(time) || time < 0.0) {
            throw new IllegalArgumentException(
                    "Passenger " + passengerIndex + " has an invalid " + type + " time");
        }
        return time;
    }

    private double average(double total, int count) {
        return count == 0 ? 0.0 : total / count;
    }
}
