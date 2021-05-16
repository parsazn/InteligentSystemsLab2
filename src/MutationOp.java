import org.jgap.*;
import org.jgap.Configuration;
import org.jgap.impl.MutationOperator;
import org.jgap.IUniversalRateCalculator;
import org.jgap.InvalidConfigurationException;

import java.util.ArrayList;
import java.util.List;

public class MutationOp extends MutationOperator {
    public MutationOp() throws InvalidConfigurationException {
    }

    public MutationOp(Configuration a_conf) throws InvalidConfigurationException {
        super(a_conf);
    }

    public MutationOp(Configuration a_config, IUniversalRateCalculator a_mutationRateCalculator) throws InvalidConfigurationException {
        super(a_config, a_mutationRateCalculator);
    }

    public MutationOp(Configuration a_config, int a_desiredMutationRate) throws InvalidConfigurationException {
        super(a_config, a_desiredMutationRate);
    }

    @Override
    public void operate(Population a_population, List a_candidateChromosomes) {
        super.operate(a_population, a_candidateChromosomes);
        IChromosome[] chromosomes = a_population.toChromosomes();
        int rate = getMutationRate();
        ArrayList<Integer>[] chromosomeInRows;
        int tempVal;
        int tempIndex;
        ArrayList<Integer> swappedIndexes = new ArrayList<>();
        for (IChromosome chromosome : chromosomes) {
            System.out.println("1");
            chromosomeInRows = SudokuGa.getChromosomeRows(chromosome);
            for (ArrayList<Integer> chromosomeInRow : chromosomeInRows) {
                System.out.println("2");
                for (int k = 0; k < chromosomeInRow.size(); k++) {
                    System.out.println("3");
                    if (getRandomNumber(0, rate) == 0 && !swappedIndexes.contains(k)) {
                        swappedIndexes.add(k);
//                        do {
                            tempIndex = getRandomNumber(0, chromosomeInRow.size());
                            System.out.println("4");
//                        }
//                        while (swappedIndexes.contains(tempIndex));
                        swappedIndexes.add(tempIndex);
                        tempVal = chromosomeInRow.get(tempIndex);
                        chromosomeInRow.set(tempIndex, chromosomeInRow.get(k));
                        chromosomeInRow.set(k, tempVal);
                    }
                }
            }
            SudokuGa.chromosomeRowsToChromosome(chromosomeInRows, chromosome);
        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}