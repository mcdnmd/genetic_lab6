package lab5;

import java.util.concurrent.ThreadLocalRandom;

public class MyRandomGenerator {
    private static final double minValue = -5.0;
    private static final double maxValue = 5.0;

    public double generateRandomDouble() {
        return ThreadLocalRandom.current().nextDouble(minValue, maxValue);
    }
}
