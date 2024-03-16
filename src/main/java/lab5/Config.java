package lab5;

public class Config {
    public double CROSSOVER_ALPHA;

    public double MUTATION_PROBABILITY;
    public double MUTATION_DECAY_RATE;
    public double MIN_MUTATION_PROBABILITY;

    public Config(
            double crossoverAlpha,
            double mutationProbability,
            double decayRate,
            double minMutationProbability
    ){
        this.CROSSOVER_ALPHA = crossoverAlpha;
        this.MUTATION_PROBABILITY = mutationProbability;
        this.MUTATION_DECAY_RATE = decayRate;
        this.MIN_MUTATION_PROBABILITY = minMutationProbability;

    }
}
