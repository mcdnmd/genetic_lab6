package lab5;

import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyCrossover extends AbstractCrossover<double[]> {
    private static double ALPHA;

    protected MyCrossover(double alpha) {
        super(1);
        ALPHA = alpha;
    }

    protected List<double[]> mate(double[] p1, double[] p2, int i, Random random) {
        ArrayList<double[]> children = new ArrayList<>();

        // your implementation:
        double[] child1 = createNewChild(p1, p2, ALPHA);
        double[] child2 = createNewChild(p1, p2, ALPHA);
        children.add(child1);
        children.add(child2);
        return children;
    }

    private double[] createNewChild(double[] parent1, double[] parent2, double alpha) {
        double[] newChild = new double[parent1.length];

        for (int i = 0; i < parent1.length; i++){
            newChild[i] = alpha * parent1[i] + (1-alpha) * parent2[i];
        }

        return newChild;
    }
}
