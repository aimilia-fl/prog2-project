package gr.aueb.dmst.javengers.Evaluation.MathEvaluator;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import gr.aueb.dmst.javengers.common.dto.MetricsResponse;

public class MathEvaluatorTest {

    private final MathEvaluator evaluator = new MathEvaluator();

    @Test
    public void calculatesScenarioMetrics() {
        MetricsResponse.Metrics metrics = evaluator.evaluate(
                Arrays.asList(10.0, 20.0, 5.0),
                Arrays.asList(30.0, 40.0, 25.0),
                2);

        Assert.assertEquals(3, metrics.getTotalPassengersServed());
        Assert.assertEquals(35.0 / 3.0, metrics.getAverageWaitTimeSeconds(), 0.0001);
        Assert.assertEquals(95.0 / 3.0, metrics.getAverageJourneyTimeSeconds(), 0.0001);
        Assert.assertEquals(20.0, metrics.getMaxWaitTimeSeconds(), 0.0);
        Assert.assertEquals(2.0, metrics.getOverloadedRejections(), 0.0);
    }

    @Test
    public void returnsZeroMetricsForNoServedPassengers() {
        MetricsResponse.Metrics metrics = evaluator.evaluate(
                Collections.emptyList(), Collections.emptyList(), 1);

        Assert.assertEquals(0, metrics.getTotalPassengersServed());
        Assert.assertEquals(0.0, metrics.getAverageWaitTimeSeconds(), 0.0);
        Assert.assertEquals(0.0, metrics.getAverageJourneyTimeSeconds(), 0.0);
        Assert.assertEquals(0.0, metrics.getMaxWaitTimeSeconds(), 0.0);
        Assert.assertEquals(1.0, metrics.getOverloadedRejections(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsMismatchedPassengerTimes() {
        evaluator.evaluate(Arrays.asList(10.0), Collections.emptyList(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNegativeTime() {
        evaluator.evaluate(Arrays.asList(-1.0), Arrays.asList(10.0), 0);
    }
}
