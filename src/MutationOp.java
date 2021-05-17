import org.jgap.*;
import org.jgap.Configuration;
import org.jgap.impl.MutationOperator;
import org.jgap.InvalidConfigurationException;

import java.util.ArrayList;
import java.util.List;

public class MutationOp extends MutationOperator {
    public MutationOp(Configuration a_conf) throws InvalidConfigurationException {
        super(a_conf, 10);
    }

    public MutationOp(Configuration a_config, int a_desiredMutationRate) throws InvalidConfigurationException {
        super(a_config, a_desiredMutationRate);
    }

    @Override
    public void operate(Population a_population, List a_candidateChromosomes) {
        IChromosome[] chromosomes = a_population.toChromosomes();// array of chromosomes in the population
        int rate = getMutationRate();//current rate
        ArrayList<Integer>[] chromosomeInRows;
        IChromosome newChromosome;
        boolean mutated;
        int tempVal;
        int tempIndex;
        ArrayList<Integer> swappedIndexes = new ArrayList<>();//array of indexes that has been already swapped
        for (IChromosome chromosome : chromosomes) {// looping over all chromosomes
            chromosomeInRows = SudokuGa.getChromosomeRows(chromosome);// transforming chromosome to array of rows (array lists)
            mutated = false;
            for (ArrayList<Integer> chromosomeRow : chromosomeInRows) { // going over all rows
                swappedIndexes.clear();// clearing the swapped indexes array
                for (int k = 0; k < chromosomeRow.size(); k++) {//going through all numbers in the row (row has only dynamic numbers)
                    if (SudokuGa.getRandomNumber(0, rate) == 0 && !swappedIndexes.contains(k)) { //condition to swap this number with another
                        swappedIndexes.add(k);
                        do {
                            tempIndex = SudokuGa.getRandomNumber(0, chromosomeRow.size());
                        } while (swappedIndexes.contains(tempIndex) && swappedIndexes.size() < chromosomeRow.size());
                        swappedIndexes.add(tempIndex);
                        mutated = true;
                        tempVal = chromosomeRow.get(tempIndex);
                        chromosomeRow.set(tempIndex, chromosomeRow.get(k));
                        chromosomeRow.set(k, tempVal);
                    }
                }
            }
            if (mutated) {
                newChromosome = (IChromosome) chromosome.clone();
                SudokuGa.chromosomeRowsToChromosome(chromosomeInRows, newChromosome);
                //noinspection unchecked
                a_candidateChromosomes.add(newChromosome);
            }
        }
    }
}