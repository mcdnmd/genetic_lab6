package lab5;

public class FitnessStorageSingletThread implements FitnessStorage {
    private double bestFitness = 0.0;

    public void update(double fitness) {
        if (fitness > bestFitness) {
            bestFitness = fitness;
        }
    }

    public double getBestFitness() {
        return bestFitness;
    }
}
