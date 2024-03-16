package lab5;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.Random;

public class MyFactory extends AbstractCandidateFactory<double[]> {

    private int dimension;

    private final MyRandomGenerator randomGenerator = new MyRandomGenerator();

    public MyFactory(int dimension) {
        this.dimension = dimension;
    }

    public double[] generateRandomCandidate(Random random) {
        double[] solution = new double[dimension];

        for (int i = 0; i < dimension; i++) {
            // Генерация случайного значения в диапазоне [MIN_VALUE, MAX_VALUE]
            solution[i] = randomGenerator.generateRandomDouble();
        }

        return solution;
    }
}

