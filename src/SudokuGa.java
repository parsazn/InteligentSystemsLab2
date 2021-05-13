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

    public static void findSolution(QQWing s) throws Exception {
        boolean[] DefaultValues = new boolean[81];//Here we create a new matrix same as the other but we check if the value is zero or not
        //whole sudoku but in only one line
        ArrayList<Integer> arrayListSudoku = new ArrayList<>();
        sudoku_Lineal = s.getPuzzle();
        for (int i:sudoku_Lineal) {
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
        System.out.println(Collections.frequency(arrayListSudoku, 0));
        Gene[] sampleGenes = new Gene[Collections.frequency(arrayListSudoku, 0)]; //array of Genes, Gene is single sudoku cell
        for (int i = 0; i < sampleGenes.length; i++) {
                sampleGenes[i] = new IntegerGene(conf, 1, 9);//the JPAG it self changes the values randomly
        }
        // ---------------------------------------------------------------------
        IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);// chromosome is one full sudoku with default values and random values
        conf.setSampleChromosome(sampleChromosome);
        conf.setPopulationSize(MAX_ALLOWED_POPULATION);//population is a set of generated sudokus in one evolution
//        GeneticOperator mutationOperator = new MutationOperator(conf, 3);
////        GeneticOperator crossoverOperator = new CrossoverOperator(conf);
//        conf.addGeneticOperator(mutationOperator);
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
            s.setPuzzle(genes2Sudokus(I));
            s.printPuzzle();
        }
        //here is the Best one
        //toDo Complete FitnessFunc in order to find the best solution.
        IChromosome bestSolutionSoFar = population.getFittestChromosome();
        System.out.println("The best solution has a fitness value of " +
                bestSolutionSoFar.getFitnessValue());
        s.setPuzzle(genes2Sudokus(bestSolutionSoFar));
        s.printPuzzle();
    }

    //we are trying to find the default values .
    private static void setNumbers(int[] puzzle, boolean[] defaults) { //here we define the default values
        for (int i = 0; i < puzzle.length; ++i) {
            defaults[i] = puzzle[i] != 0;
        }
    }

    //converting each chromosome to a lineal array ( so that we can change the Sudoku )
    public static int[] genes2Sudokus(IChromosome idx) {
        int[] res = new int[idx.size()];
        for (int i = 0; i < idx.size(); ++i) {
            res[i] = (int) idx.getGene(i).getAllele();
        }
        return res;
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


