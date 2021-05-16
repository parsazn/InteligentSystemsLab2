import com.qqwing.QQWing;
import org.jgap.*;
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.MutationOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class SudokuGa {
    private final static int NumElements = 81;
    private final static String CVS_REVISION = "$Revision: 1.10 $";
    /**
     * The total number of times we'll let the population evolve.
     */

    private static final int MAX_ALLOWED_EVOLUTIONS = 140;
    private static final int MAX_ALLOWED_GENERATION = 40;
    private static final int MAX_ALLOWED_POPULATION = 5;
    private static final int SUDOKU_SIZE = 9; //  sudoku size = 9
    private static final int BLOCK_SIZE = 3;  //   block size = 3
    private static final int SUDOKU_TOTAL_SIZE = SUDOKU_SIZE * SUDOKU_SIZE;
    public static int[] sudoku_Lineal;
    public static boolean[] DefaultValues = new boolean[81];//Here we create a new matrix same as the other but we check if the value is zero or not

    public static void findSolution(QQWing s) throws Exception {
        //whole sudoku but in only one line
        ArrayList<Integer> arrayListSudoku = new ArrayList<>();
        sudoku_Lineal = s.getPuzzle();
        for (int i : sudoku_Lineal) {
            arrayListSudoku.add(i);
        }
        setNumbers(sudoku_Lineal, DefaultValues); //we get the default values in our sudoku (where the values are not 0 )
        //Setting
        // ---------------------------------------------------------------------
        Configuration conf = new DefaultConfiguration();
        conf.setPreservFittestIndividual(true); //Here we determine whether we want to save the fittest element(chromosome)
        FitnessFunction myFunc = new FitnessFunc();
        conf.setFitnessFunction(myFunc);
        // ---------------------------------------------------------------------
        Gene[] sampleGenes = new Gene[Collections.frequency(arrayListSudoku, 0)]; //array of Genes, Gene is single sudoku cell

        Integer[] possibleNumbers = getRandomizedChromosome();
        for (int i = 0; i < possibleNumbers.length; i++) {
            sampleGenes[i] = new IntegerGene(conf, possibleNumbers[i], possibleNumbers[i]);
        }
        // ---------------------------------------------------------------------
        IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);// chromosome is one full sudoku with default values and random values
        conf.setSampleChromosome(sampleChromosome);
        conf.setPopulationSize(MAX_ALLOWED_POPULATION);//population is a set of generated sudokus in one evolution
        GeneticOperator myMutationOperator = new MutationOp(conf, 12);
//        GeneticOperator crossoverOperator = new CrossoverOperator(conf);
        conf.addGeneticOperator(myMutationOperator);
////        conf.addGeneticOperator(crossoverOperator);
        Genotype population;

        population = Genotype.randomInitialGenotype(conf);
        for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
            population.evolve();
        }
        //we have a list of chromosomes which they have been evolved .
        System.out.println("These are the evolutions so far");
        IChromosome[] evols = population.getChromosomes();
        for (IChromosome I : evols) {
            s.setPuzzle(chromosomeIntoSudoku(I));
            s.printPuzzle();
        }
        //here is the Best one
        //toDo Complete FitnessFunc in order to find the best solution.
        IChromosome bestSolutionSoFar = population.getFittestChromosome();
        System.out.println("The best solution has a fitness value of " +
                bestSolutionSoFar.getFitnessValue());
        s.setPuzzle(chromosomeIntoSudoku(bestSolutionSoFar));
        s.printPuzzle();
    }

    //we are trying to find the default values .
    private static void setNumbers(int[] puzzle, boolean[] defaults) { //here we define the default values
        for (int i = 0; i < puzzle.length; ++i) {
            defaults[i] = puzzle[i] != 0;
        }
    }

    public static ArrayList<Integer>[] getChromosomeRows(IChromosome idx) {
        @SuppressWarnings("unchecked") ArrayList<Integer>[] chromosomeRows = (ArrayList<Integer>[]) new ArrayList[SUDOKU_SIZE];
        int[] chromosome = chromosomeToArray(idx);
        int k = 0;
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            chromosomeRows[i] = new ArrayList<>();
            for (int j = 0; j < SUDOKU_SIZE; j++) {
                if (!DefaultValues[i * SUDOKU_SIZE + j]) chromosomeRows[i].add(chromosome[k++]);
            }
        }
        return chromosomeRows;
    }

    private static Integer[] getRandomizedChromosome() {
        @SuppressWarnings("unchecked") ArrayList<Integer>[] chromosomePossibleNums = (ArrayList<Integer>[]) new ArrayList[SUDOKU_SIZE];
        ArrayList<Integer> possibleNums = new ArrayList<>();
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            chromosomePossibleNums[i] = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
            for (int j = 0; j < SUDOKU_SIZE; j++) {
                if (sudoku_Lineal[i * SUDOKU_SIZE + j] != 0)
                    chromosomePossibleNums[i].remove(Integer.valueOf(sudoku_Lineal[i * SUDOKU_SIZE + j]));
            }
            Collections.shuffle(chromosomePossibleNums[i]);
            possibleNums.addAll(chromosomePossibleNums[i]);
        }
        Integer[] linearizedNumbers = new Integer[possibleNums.size()];
        linearizedNumbers = possibleNums.toArray(linearizedNumbers);
        return linearizedNumbers;
    }

    //converting each chromosome to a lineal array ( so that we can change the Sudoku )
    public static int[] chromosomeToArray(IChromosome idx) {
        int[] res = new int[idx.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = (int) idx.getGene(i).getAllele();
        }
        return res;
    }

    public static ArrayList<Integer> chromosomeRowsToLineal(ArrayList<Integer>[] chromosomeRows) {
        ArrayList<Integer> linChromosome = new ArrayList<>();
        for (ArrayList<Integer> chromosomeRow : chromosomeRows) {
            linChromosome.addAll(chromosomeRow);
        }
        return linChromosome;
    }

    public static void chromosomeRowsToChromosome(ArrayList<Integer>[] chromosomeRows, IChromosome myChromosome) {
        ArrayList<Integer> chromosomeLineal = chromosomeRowsToLineal(chromosomeRows);
        for (int i = 0; i < chromosomeLineal.size(); i++) {
            myChromosome.getGene(i).setAllele(chromosomeLineal.get(i));
        }
    }

    public static int[] chromosomeIntoSudoku(IChromosome idx) {
        int[] chromosome = chromosomeToArray(idx);
        int[] mainSudoku = SudokuGa.sudoku_Lineal.clone();
        int j = 0;
        for (int i = 0; i < mainSudoku.length; i++) {
            if (mainSudoku[i] == 0) {
                mainSudoku[i] = chromosome[j++];
            }
        }
        return mainSudoku;
    }

    public static void main(String[] args) throws Exception {
        QQWing MySudoku = new QQWing();
        MySudoku.generatePuzzle();
        MySudoku.printPuzzle();
        findSolution(MySudoku);
       /* MySudoku = new QQWing();
        MySudoku.generatePuzzle();
        MySudoku.printPuzzle();
        MySudoku.getPuzzle() ;
        System.out.println(Arrays.toString(MySudoku.getPuzzle()));
        //System.out.println(Arrays.toString(InitialSolutions(MySudoku.getPuzzle())));
        MySudoku.solve();
        MySudoku.printSolution();
*/
    }
}


