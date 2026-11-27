package gr.aueb.dmst.javengers.central.dispatch;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class WeightGenerator {
    private static final double MIN_WEIGHT = 60.0;
    private static final double COMMON_MIN_WEIGHT = 70.0;
    private static final double COMMON_MAX_WEIGHT = 200.0;
    private static final double MAX_WEIGHT = 300.0;

    private static final double LOW_RANGE_PROBABILITY = 0.10;
    private static final double COMMON_RANGE_PROBABILITY = 0.85;
    private static final double ROUND = 10.0;

    private final Random random;

    public WeightGenerator() {
        this(ThreadLocalRandom.current());
    }

    public WeightGenerator(Random random) {
        this.random = random;
    }

    public double nextWeight() {

        double roll = random.nextDouble();
        double rawWeight;

        if (roll < LOW_RANGE_PROBABILITY){
            rawWeight = MIN_WEIGHT + random.nextDouble() * (COMMON_MIN_WEIGHT - MIN_WEIGHT);
        } else if (roll < COMMON_RANGE_PROBABILITY) {
            rawWeight = COMMON_MIN_WEIGHT + random.nextDouble() * (COMMON_MAX_WEIGHT - COMMON_MIN_WEIGHT);
        } else {
            rawWeight = COMMON_MAX_WEIGHT + random.nextDouble() * (MAX_WEIGHT - COMMON_MAX_WEIGHT);
        }

        return Math.round(rawWeight * ROUND) / ROUND;
    }
}
