import org.jgap.FitnessFunction;
import org.jgap.IChromosome;


//fitness function considers number of duplicated numbers in the array
public class FitnessFunc extends FitnessFunction {
    @Override
    protected double evaluate(IChromosome iChromosome) {
        int[] chromosome = chromosomeIntoSudoku(SudokuGa.genes2Sudokus(iChromosome));
        return uniqueInColumn(chromosome) + uniqueInRow(chromosome) + uniqueInSquare(chromosome);
    }

    private int countDistinct(int[] arr) {
        int uniqueValues = 1;
        for (int i = 1; i < arr.length; i++) {
            int j;
            for (j = 0; j < i; j++)
                if (arr[i] == arr[j])
                    break;
            if (i == j)
                uniqueValues++;
        }
        return uniqueValues;
    }

    private int[] chromosomeIntoSudoku(int[] chromosome){
        int[] mainSudoku = SudokuGa.sudoku_Lineal.clone();
        int j = 0;
        for (int i = 0; i < mainSudoku.length; i++) {

            if(mainSudoku[i]==0){
                mainSudoku[i]=chromosome[j];
                j++;
            }
        }
        return mainSudoku;
    }

    private int uniqueInRow(int[] numbers) {
        int satisfiedConstraints = 0;
        int[][] rows = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(numbers, i * 9, rows[i], 0, 9);
            if(countDistinct(rows[i])==9) satisfiedConstraints +=1;
        }
        return satisfiedConstraints;
    }

    private int uniqueInColumn(int[] numbers) {
        int satisfiedConstraints = 0;
        int[][] columns = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                columns[i][j] = numbers[i + 9 * j];
            }
            if(countDistinct(columns[i])==9) satisfiedConstraints +=1;
        }
        return satisfiedConstraints;
    }

    private int uniqueInSquare(int[] numbers) {
        int satisfiedConstraints = 0;
        int[][] squares = new int[9][9];
        for (int k = 0; k < 9; k++) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    squares[k][j + i * 3] = numbers[(3 * (k % 3) + j + (i + k / 3 * 3) * 9)];
                }
            }
            if(countDistinct(squares[k])==9) satisfiedConstraints +=1;
        }
        return satisfiedConstraints;
    }
}
//0,0 3*0+0+0*9
//0,1 3*0+1+0*9
//0,2 3*0+2+0*9
//0,3 3*0+0+1*9
//0,4 3*0+1+1*9
//0,5 3*0+2+1*9
//0,6 3*0+0+2*9
//0,7 3*0+1+2*9
//0,8 3*0+2+2*9
//1,0 3*1+0+0*9
//1,1 3*1+1+0*9
//1,2 3*1+2+0*9
//1,3 3*1+0+1*9
//1,4 3*1+1+1*9
//1,5 3*1+2+1*9
//1,6 3*1+0+2*9
//1,7 3*1+1+2*9
//1,8 3*1+2+2*9
//2,0 3*2+0+0*9
//2,1 3*2+1+0*9
//2,2 3*2+2+0*9
//2,3 3*2+0+1*9
//2,4 3*2+1+1*9
//2,5 3*2+2+1*9
//2,6 3*2+0+2*9
//2,7 3*2+1+2*9
//2,8 3*2+2+2*9
//3,0 3*0+0+3*9
//3,1 3*0+1+3*9
//3,1 3*0+2+3*9
//3,1 3*0+0+4*9
//0 0
//1 0
//2 0
//3 3
//4 3
//5 3
