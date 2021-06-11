import org.jgap.*;
import org.jgap.impl.CrossoverOperator;

import java.util.ArrayList;
import java.util.List;

public class CrossoverOp extends CrossoverOperator {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2201739806650240643L;

	public CrossoverOp(Configuration a_configuration) throws InvalidConfigurationException {
        super(a_configuration, 30);
    }

    public CrossoverOp(Configuration a_configuration, int a_desiredCrossoverRate) throws InvalidConfigurationException {
        super(a_configuration, a_desiredCrossoverRate);
    }

    @Override
    public void operate(Population a_population, List a_candidateChromosomes) {
        IChromosome[] chromosomes = a_population.toChromosomes();// array of chromosomes in the population
        Chromosome chromosomeA;
        Chromosome chromosomeB;
        int tempIndex;
        int rate = getCrossOverRate();
        ArrayList<Integer> tempChromosomeRow;
        ArrayList<Integer>[] chromosomeAInRows;
        ArrayList<Integer>[] chromosomeBInRows;
        for (int i = 0, chromosomesLength = chromosomes.length; i < chromosomesLength - 1; i++) {
            if (SudokuGa.getRandomNumber(1, 101) < rate) {
                chromosomeA = (Chromosome) chromosomes[i].clone();
                chromosomeB = (Chromosome) chromosomes[i + 1].clone();
                chromosomeAInRows = SudokuGa.getChromosomeRows(chromosomeA);// transforming chromosome to array of rows (array lists)
                chromosomeBInRows = SudokuGa.getChromosomeRows(chromosomeB);// transforming chromosome to array of rows (array lists)
                tempIndex = SudokuGa.getRandomNumber(1, chromosomeAInRows.length - 1);
                for (int j = tempIndex; j < chromosomeAInRows.length; j++) {
                    tempChromosomeRow = chromosomeAInRows[j];
                    chromosomeAInRows[j] = chromosomeBInRows[j];
                    chromosomeBInRows[j] = tempChromosomeRow;
                }
                SudokuGa.chromosomeRowsToChromosome(chromosomeAInRows, chromosomeA);
                SudokuGa.chromosomeRowsToChromosome(chromosomeBInRows, chromosomeB);
                //noinspection unchecked
                a_candidateChromosomes.add(chromosomeA);
                //noinspection unchecked
                a_candidateChromosomes.add(chromosomeB);
            }
        }
    }
}
