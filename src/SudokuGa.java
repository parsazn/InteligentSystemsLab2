import com.qqwing.QQWing;
import org.jgap.*;
import org.jgap.event.EventManager;
import org.jgap.impl.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class SudokuGa {
    public static final int SUDOKU_SIZE = 9; 
    public static final int BLOCK_SIZE = 3; 
    /**
     * The total number of times we'll let the population evolve.
     */

    private static int maxAllowedEvolutions = 1400;//140 *
    private static int maxAllowedPopulation = 1000; // *
    
    private static final int SUDOKU_TOTAL_SIZE = SUDOKU_SIZE * SUDOKU_SIZE;
    public static int[] sudoku_Lineal;
    public static boolean[] DefaultValues = new boolean[SUDOKU_TOTAL_SIZE];//Here we create a new matrix same as the other but we check if the value is zero or not
    private static boolean printFitness = true;

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

        conf.setMinimumPopSizePercent(0);
        conf.setKeepPopulationSizeConstant(false);
        conf.setChromosomePool(new ChromosomePool());
        conf.setSelectFromPrevGen(1D);
        conf.setPopulationSize(maxAllowedPopulation);//population is a set of generated sudokus in one evolution
        conf.addGeneticOperator(myMutationOperator);//our mutation operator
        conf.addGeneticOperator(myCrossoverOperator);//our crossover operator
        conf.setRandomGenerator(new GaussianRandomGenerator());// it's necessary
        conf.setEventManager(new EventManager());
        conf.setFitnessEvaluator(new DefaultFitnessEvaluator());
        conf.addNaturalSelector(new BestChromosomesSelector(conf, 0.9D), false);
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
            if (bestFitness < chromosome.getFitnessValue()) {
                bestFitness = chromosome.getFitnessValue();
                System.out.println(bestFitness);
            }
        }

        System.out.println("These are the evolutions so far");
        for (int i = 0; i < maxAllowedEvolutions; i++) {
            genotype.evolve();
            IChromosome bestChromosome = genotype.getPopulation().determineFittestChromosome();//list of evolved chromosomes,
            if (bestFitness < bestChromosome.getFitnessValue()) {
                bestFitness = bestChromosome.getFitnessValue();
                if(printFitness){ //here we write the results in a txt format file
                    List<IChromosome> totalChroms = genotype.getPopulation().getChromosomes();
                    int num =0;
                    double res =0 ;
                    for(IChromosome c : totalChroms){
                       ++num ;
                       res +=c.getFitnessValue();
                    }
                    double average = res/num ;
                    writeTextFile("bestFitness.txt",i,bestFitness);
                    writeTextFile("meanFitness.txt",i,average);
                }
                System.out.println(bestFitness);
            }
            if (bestFitness == 162) break;
        }
        IChromosome bestSolutionSoFar = genotype.getFittestChromosome();
        System.out.println("The best solution has a fitness value of " +
                bestSolutionSoFar.getFitnessValue());
        s.setPuzzle(chromosomeIntoSudoku(bestSolutionSoFar));
        s.printPuzzle();
    }
    
    public static void setMaxAllowedEvolutions(int newMax) {
    	maxAllowedEvolutions = newMax;
    }
    
    public static void setMaxAllowedPopulation(int newMax) {
    	maxAllowedPopulation = newMax;
    }

    private static void setNumbers(int[] puzzle, boolean[] defaults) { //e fill array of bool so we know where are default numbers
        for (int i = 0; i < puzzle.length; ++i) {
            defaults[i] = puzzle[i] != 0;
        }
    }

    private static Genotype fillPopulation(Configuration conf, Chromosome sampleChromosome) throws InvalidConfigurationException {
        Population pop = new Population(conf, maxAllowedPopulation);
        List<Chromosome> newChromosomes = new ArrayList<>();
        Genotype result;

        for (int i = 0; i < maxAllowedPopulation; i++) {
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

    public static void printCurrentPuzzle(IChromosome chromosome) {
        QQWing s = new QQWing();
        s.setPuzzle(chromosomeIntoSudoku(chromosome));
        s.printPuzzle();
    }

    public static void writeTextFile(String name ,int i ,double a) throws IOException {
        FileWriter fileWriter = new FileWriter(name,true) ;
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(i +" "+a);  //New line
        printWriter.close();
    }
}


