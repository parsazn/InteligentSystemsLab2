import com.qqwing.QQWing;
import org.jgap.*;
import org.jgap.event.EventManager;
import org.jgap.impl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class SudokuGa {
    private final static String CVS_REVISION = "$Revision: 1.10 $";
    /**
     * The total number of times we'll let the population evolve.
     */

    private static final int MAX_ALLOWED_EVOLUTIONS = 1400;//140
    //    private static final int MAX_ALLOWED_GENERATION = 40;
    private static final int MAX_ALLOWED_POPULATION = 2000;
    public static final int SUDOKU_SIZE = 9;
    public static final int BLOCK_SIZE = 3;
    private static final int SUDOKU_TOTAL_SIZE = SUDOKU_SIZE * SUDOKU_SIZE;
    public static int[] sudoku_Lineal;
    public static boolean[] DefaultValues = new boolean[SUDOKU_TOTAL_SIZE];//Here we create a new matrix same as the other but we check if the value is zero or not

    public static void findSolution(QQWing s) throws Exception {
        //whole sudoku but in only one line
        ArrayList<Integer> arrayListSudoku = new ArrayList<>();
        sudoku_Lineal = s.getPuzzle();
        for (int i : sudoku_Lineal) {
            arrayListSudoku.add(i);
        }
        setNumbers(sudoku_Lineal, DefaultValues); //we get array of booleans, true when there is default value

        ////////////////////////////              Configuration               /////////////////////////////////////////
        Configuration conf = new Configuration();
        GeneticOperator myMutationOperator = new MutationOp(conf, 18);// probability = 1/rate default 20
        GeneticOperator myCrossoverOperator = new CrossoverOp(conf, 95);// probability = rate/100 default 100
        FitnessFunction myFunc = new FitnessFunc(); //our fitness function

        conf.setMinimumPopSizePercent(0);// I don't know yet
        conf.setKeepPopulationSizeConstant(false);//I don't know yet
        conf.setChromosomePool(new ChromosomePool());//Not important from what i know
        conf.setSelectFromPrevGen(1D);//Not sure what it is
        conf.setPopulationSize(MAX_ALLOWED_POPULATION);//population is a set of generated sudokus in one evolution
        conf.addGeneticOperator(myMutationOperator);//our mutation operator
        conf.addGeneticOperator(myCrossoverOperator);//our crossover operator
        conf.setRandomGenerator(new GaussianRandomGenerator());//I don't know if we are using it but it's necessary
        conf.setEventManager(new EventManager());//I don't know what is that but necessary
        conf.setFitnessEvaluator(new DefaultFitnessEvaluator());//I don't know but necessary
        conf.addNaturalSelector(new BestChromosomesSelector(conf, 0.9D), false);//not sure how it works
        conf.setPreservFittestIndividual(true); //Here we determine whether we want to save the fittest element(chromosome)
        conf.setFitnessFunction(myFunc);//we set our fitness function


        Gene[] sampleGenes = new Gene[Collections.frequency(arrayListSudoku, 0)]; //array of Genes, Gene is single empty sudoku cell
        Integer[] possibleNumbers = getRandomizedChromosome();//we are getting an array of possible numbers fitting row constraint
        for (int i = 0; i < possibleNumbers.length; i++) {
            sampleGenes[i] = new IntegerGene(conf, 1, 9);
        }
        Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);// chromosome is only dynamic values of sudoku

        conf.setSampleChromosome(sampleChromosome); //we set sample chromosome


        Genotype genotype;
        genotype = fillPopulation(conf, sampleChromosome);//we fill the population with values that match row constraint
        System.out.println("This is the initial population");
        List<IChromosome> initialPop = genotype.getPopulation().getChromosomes();
        double bestFitness = 0;
        for (IChromosome chromosome : initialPop) {
            if(bestFitness<chromosome.getFitnessValue()) {
                bestFitness=chromosome.getFitnessValue();
                System.out.println(bestFitness);
            }
        }
//        genotype.evolve(MAX_ALLOWED_EVOLUTIONS);//we evolve starting with initial population

        System.out.println("These are the evolutions so far");
        for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
            genotype.evolve();
            IChromosome bestChromosome = genotype.getPopulation().determineFittestChromosome();//list of evolved chromosomes,
            if(bestFitness<bestChromosome.getFitnessValue()) {
                bestFitness=bestChromosome.getFitnessValue();
                System.out.println(bestFitness);
            }
            if(bestFitness==162) break;
        }
//        List<IChromosome> evolutions = genotype.getPopulation().getChromosomes();//list of evolved chromosomes,
        // why size of max_population, are these the fittest from every previous population?.
//        for (IChromosome chromosome : evolutions) {
//            if(bestFitness<chromosome.getFitnessValue()) {
//                bestFitness=chromosome.getFitnessValue();
//                System.out.println(bestFitness);
//            }
////            s.setPuzzle(chromosomeIntoSudoku(chromosome));
////            s.printPuzzle();
//        }
        IChromosome bestSolutionSoFar = genotype.getFittestChromosome();
        System.out.println("The best solution has a fitness value of " +
                bestSolutionSoFar.getFitnessValue());
        s.setPuzzle(chromosomeIntoSudoku(bestSolutionSoFar));
        s.printPuzzle();
    }

    private static void setNumbers(int[] puzzle, boolean[] defaults) { //e fill array of bool so we know where are default numbers
        for (int i = 0; i < puzzle.length; ++i) {
            defaults[i] = puzzle[i] != 0;
        }
    }

    private static Genotype fillPopulation(Configuration conf, Chromosome sampleChromosome) throws InvalidConfigurationException {
        Population pop = new Population(conf, MAX_ALLOWED_POPULATION);
        List<Chromosome> newChromosomes = new ArrayList<>();
        Genotype result;

        for (int i = 0; i < MAX_ALLOWED_POPULATION; i++) {
            Chromosome newChromosome = (Chromosome) sampleChromosome.clone();
            Integer[] chromosomeLineal = getRandomizedChromosome();
            for (int j = 0; j < chromosomeLineal.length; j++) {
                newChromosome.getGene(j).setAllele(chromosomeLineal[j]);
            }
            newChromosomes.add(newChromosome);
        }
        pop.setChromosomes(newChromosomes);
        result = new Genotype(conf, pop);
        return result;
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

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static void printMyFuckingPuzzle(IChromosome chromosome) {
        QQWing s = new QQWing();
        s.setPuzzle(chromosomeIntoSudoku(chromosome));
        s.printPuzzle();
    }

    public static void main(String[] args) throws Exception {
        QQWing MySudoku = new QQWing();
        MySudoku.generatePuzzle();
//        int[] easyPuzzle = {6,2,0,3,9,0,0,0,1,0,4,0,0,0,0,2,9,0,1,0,8,0,6,0,0,0,0,4,0,2,0,0,8,9,0,0,0,0,0,9,0,1,4,0,2,3,1,0,0,7,0,0,0,8,9,0,0,0,2,3,8,0,5,2,6,0,5,8,0,3,0,0,8,3,0,0,0,0,1,2,9};
//        MySudoku.setPuzzle(easyPuzzle);
        MySudoku.printPuzzle();
        int[] initialPuzzle = MySudoku.getPuzzle();
        findSolution(MySudoku);
        /*
         MySudoku = new QQWing();
         MySudoku.generatePuzzle();
         MySudoku.printPuzzle();
         MySudoku.getPuzzle() ;
         System.out.println(Arrays.toString(MySudoku.getPuzzle()));
         //System.out.println(Arrays.toString(InitialSolutions(MySudoku.getPuzzle())));
        */
        System.out.println("Real solution");
        MySudoku.setPuzzle(initialPuzzle);
        MySudoku.solve();
        MySudoku.printSolution();
    }
}


