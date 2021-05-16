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
        //super.operate(a_population, a_candidateChromosomes); // do i need this?
        IChromosome[] chromosomes = a_population.toChromosomes();// array of chromosomes in the population
        int rate = getMutationRate();//current rate
        ArrayList<Integer>[] chromosomeInRows;
        IChromosome newChromosome;
        boolean mutated = false;
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
                SudokuGa.chromosomeRowsToChromosome(chromosomeInRows, chromosome);
                a_candidateChromosomes.add(newChromosome);
            }
        }
    }


}