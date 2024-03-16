package lab5;

import java.util.concurrent.atomic.AtomicReference;

public class FitnessStorageMultithreading implements  FitnessStorage{
    private final AtomicReference<Double> bestFitness = new AtomicReference<>(0.0);

    public void update(double fitness) {
        Double curFitness;
        do {
            curFitness = bestFitness.get();
        } while (curFitness < fitness && !bestFitness.compareAndSet(curFitness, fitness));
    }

    public double getBestFitness() {
        return bestFitness.get();
    }
}
