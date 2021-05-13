import com.qqwing.QQWing;
import org.jgap.*;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

import java.util.Arrays;


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

    public static void findSolution(QQWing s) throws Exception {
        boolean[] DefaultValues = new boolean[81];//Here we create a new matrix same as the other but we check if the value is zero or not
        //whole sudoku but in only one line
        int[] sudoku_Lineal = s.getPuzzle();
        setNumbers(sudoku_Lineal, DefaultValues); //we get the default values in our sudoku (where the values are not 0 )
        //Setting
        // ---------------------------------------------------------------------
        Configuration conf = new DefaultConfiguration();
        conf.setPreservFittestIndividual(true); //Here we determine whether we want to save the fittest element(chromosome)
        FitnessFunction myFunc = new FitnessFunc();
        conf.setFitnessFunction(myFunc);
        // ---------------------------------------------------------------------
        Gene[] sampleGenes = new Gene[SUDOKU_TOTAL_SIZE]; //array of Genes, Gene is single sudoku cell
        for (int i = 0; i < SUDOKU_TOTAL_SIZE; i++) {
            if (DefaultValues[i]) {
                sampleGenes[i] = new IntegerGene(conf, sudoku_Lineal[i], sudoku_Lineal[i]);//we assign which element we should put
            } else {
                sampleGenes[i] = new IntegerGene(conf, 1, 9);//the JPAG it self changes the values randomly
            }
        }
        // ---------------------------------------------------------------------
        IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);// chromosome is one full sudoku with default values and random values
        conf.setSampleChromosome(sampleChromosome);
        conf.setPopulationSize(MAX_ALLOWED_POPULATION);//population is a set of generated sudokus in one evolution
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
        int num = 0;
        int[] res = new int[81];
        for (int i = 0; i < idx.size(); ++i) {
            res[num] = (int) idx.getGene(i).getAllele();
            ++num;
        }
        res = Arrays.copyOf(res, num);
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


