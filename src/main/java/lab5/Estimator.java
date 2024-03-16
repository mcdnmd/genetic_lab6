package lab5;

import java.util.List;

public class Estimator {

    public static double meanDouble(List<Double> values) {
        double sum = 0.0;
        for (Double value : values) {
            sum += value;
        }

        return sum / values.size();
    }

    public static int meanLong(List<Long> values) {
        long sum = 0;
        for (Long value : values) {
            sum += value;
        }
        return (int) (sum / values.size());
    }
}
