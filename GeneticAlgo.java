import java.util.*;

public abstract class GeneticAlgo<T extends Chromosome> {

    public List<Chromosome> population;

    void generateNextGeneration() {

        //for (Chromosome i : pop){
        //	System.out.println(i.GetStr());
        //}
        List<ParentPair> parents = selection();
        //for (ParentPair i : parents){
        //	System.out.println(i.a.GetStr() + " AND " + i.b.GetStr());
        //}
        List<Chromosome> children = crossover(parents);
        children = mutate(children);
        for (Chromosome c : children) {
            c.setFitness(computeFitnessLevel(c));
        }
        population = acceptance(children);
    }

    // Defines a pair of parents
    class ParentPair {

        public Chromosome a, b;

        public ParentPair(Chromosome a, Chromosome b) {
            this.a = a;
            this.b = b;
        }
    }

    GeneticAlgo(List<Chromosome> initialPop) {
        population = initialPop;
        for (Chromosome c : population) {
            c.setFitness(computeFitnessLevel(c));
        }
    }

    // Computes the fitness of a given Chromosome
    abstract protected double computeFitnessLevel(Chromosome c);

    // Selects which chromosomes to breed
    abstract protected List<ParentPair> selection();

    // Creates the children from the parents chosen. Returns a list of children.
    abstract protected List<Chromosome> crossover(List<ParentPair> parents);

    // Implements mutation. Leave blank if we do not wish for mutation to occur.
    abstract protected List<Chromosome> mutate(List<Chromosome> children);

    // Merges the parent and children populations to give the next generation
    abstract protected List<Chromosome> acceptance(List<Chromosome> children);

    // Gets the best chromosome in the current population
    public Chromosome getBest() {
        Chromosome bestChromosome = null;
        for (Chromosome c : population) {
            if (bestChromosome == null || bestChromosome.getFitness() < c.getFitness()) {
                bestChromosome = c;
            }
        }
        return bestChromosome;
    }

    public List<Chromosome> getPopulation() {
        return population;
    }
}
