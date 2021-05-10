import Examples.KnapsackFitnessFunction;
import com.qqwing.QQWing;
import org.jgap.*;
import org.jgap.gp.function.Pop;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

import java.util.Random;
import java.util.Arrays;


public class SudokuGa {
    private final static  int NumElements = 81 ;
    private final static String CVS_REVISION = "$Revision: 1.10 $";
    /**
     * The total number of times we'll let the population evolve.
     */

    private static final int MAX_ALLOWED_EVOLUTIONS = 140;
    private static final int MAX_ALLOWED_GENERATION = 40;
    private static final int MAX_ALLOWED_POPULATION = 40 ;
    private static final int SUDOKU_SIZE = 9; //  sudoku size = 9
    private static final int BLOCK_SIZE = 3;  //   block size = 3
    private static final int SUDOKU_TOTAL_SIZE = SUDOKU_SIZE * SUDOKU_SIZE ;
    public static void findSolution(QQWing s) throws Exception {
        int numbers[] = new int[100];
        int pos[] = new int[100] ;
        setNumbers(s.getPuzzle(),numbers,pos); //we get the default values in our sudoku (where the values are not 0 )
        //Setting
        // ---------------------------------------------------------------------
        Configuration conf = new DefaultConfiguration();
        conf.setPreservFittestIndividual(true); //Here we determine whether we want to save the fittest element(chromosome)
        FitnessFunction myFunc = new FitnessFunc();
        conf.setFitnessFunction(myFunc);
        // ---------------------------------------------------------------------
        Gene[] sampleGenes = new Gene[SUDOKU_TOTAL_SIZE];
        for (int i = 0; i < SUDOKU_TOTAL_SIZE; i++) {
            sampleGenes[i] = new IntegerGene(conf, 1, 9);
        }
        IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
        conf.setSampleChromosome(sampleChromosome);
        conf.setPopulationSize(5);
        Genotype population ;
        population = Genotype.randomInitialGenotype(conf);
        defaultValues(population,numbers,pos);
        population.evolve();
        IChromosome bestSolutionSoFar = population.getFittestChromosome();
        System.out.println("The best solution has a fitness value of " +
                bestSolutionSoFar.getFitnessValue());
        System.out.println("This is the final Sudoku");
        s.setPuzzle(genes2Sudokus(bestSolutionSoFar)) ;
        s.printPuzzle();
    }
    public static void defaultValues(Genotype p,int[] Nums ,int[]Pos){
        IChromosome[] idx = p.getChromosomes();
        Gene[] Genes ;
        for(int i=0 ;i<idx.length ; ++i){//for each chromosome , we change each specific gene
            Genes = idx[i].getGenes();
            for(int j =0 ; j<idx.length;++j){
                if(j==Pos[j]){
                    Genes[j].setAllele(Nums[j]);
                }
            }
        }
    }
    private static void setNumbers(int[] puzzle, int[] numbers, int[] pos) { //here we define the default values
        numbers = new int[100] ;
        pos = new int[100] ;
        int nNum = 0 ;
        int nPos =0 ;
        for(int i =0 ; i<puzzle.length ; ++i){
            if(puzzle[i]!=0){
                numbers[nNum]=puzzle[i] ;
                ++nNum;
                pos[nPos]=i ;
                ++nPos;
            }
        }
        numbers = Arrays.copyOf(numbers,nNum);
        pos = Arrays.copyOf(pos,nPos);
    }
    public static int[] genes2Sudokus(IChromosome idx){
        int num =0 ;
        int[] res = new int[100] ;
        for(int i =0 ;i<idx.size();++i){
            res[num] = (int) idx.getGene(i).getAllele();
            ++num ;
        }
        res = Arrays.copyOf(res , num) ;
        return res ;
    }
    public static void main(String[] args) throws Exception {
        QQWing MySudoku = new QQWing() ;
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


