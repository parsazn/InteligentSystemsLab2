import org.jgap.FitnessFunction;
import org.jgap.IChromosome;


//fitness function considers number of duplicated numbers in the array
public class FitnessFunc extends FitnessFunction {
    private final int sudokuSize = SudokuGa.SUDOKU_SIZE;

    @Override
    protected double evaluate(IChromosome iChromosome) {
        int[] chromosome = SudokuGa.chromosomeIntoSudoku(iChromosome);
        return uniqueInColumn2(chromosome) + uniqueInSquare2(chromosome);
//        return uniqueInColumn(chromosome) + uniqueInSquare(chromosome);
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

//    private int uniqueInRow(int[] numbers) {
//        int satisfiedConstraints = 0;
//        int[][] rows = new int[sudokuSize][sudokuSize];
//        for (int i = 0; i < sudokuSize; i++) {
//            System.arraycopy(numbers, i * sudokuSize, rows[i], 0, sudokuSize);
//            if (countDistinct(rows[i]) == sudokuSize) satisfiedConstraints += 1;
//        }
//        return satisfiedConstraints;
//    }

    private int uniqueInColumn(int[] numbers) {
        int satisfiedConstraints = 0;
        int[][] columns = new int[sudokuSize][sudokuSize];
        for (int i = 0; i < sudokuSize; i++) {
            for (int j = 0; j < sudokuSize; j++) {
                columns[i][j] = numbers[i + sudokuSize * j];
            }
            if (countDistinct(columns[i]) == sudokuSize) satisfiedConstraints += 1;
        }
        return satisfiedConstraints;
    }

    private int uniqueInColumn2(int[] numbers) {
        int satisfiedConstraints = 0;
        int[][] columns = new int[sudokuSize][sudokuSize];
        for (int i = 0; i < sudokuSize; i++) {
            for (int j = 0; j < sudokuSize; j++) {
                columns[i][j] = numbers[i + sudokuSize * j];
            }
            satisfiedConstraints += countDistinct(columns[i]);
        }
        return satisfiedConstraints;
    }

    private int uniqueInSquare(int[] numbers) {
        int satisfiedConstraints = 0;
        int[][] squares = new int[sudokuSize][sudokuSize];
        int blockSize = SudokuGa.BLOCK_SIZE;
        for (int k = 0; k < sudokuSize; k++) {
            for (int i = 0; i < blockSize; i++) {
                for (int j = 0; j < blockSize; j++) {
                    squares[k][j + i * blockSize] = numbers[(blockSize * (k % blockSize) + j + (i + k / blockSize * blockSize) * sudokuSize)];
                }
            }
            if (countDistinct(squares[k]) == sudokuSize) satisfiedConstraints += 1;
        }
        return satisfiedConstraints;
    }

    private int uniqueInSquare2(int[] numbers) {
        int satisfiedConstraints = 0;
        int[][] squares = new int[sudokuSize][sudokuSize];
        int blockSize = SudokuGa.BLOCK_SIZE;
        for (int k = 0; k < sudokuSize; k++) {
            for (int i = 0; i < blockSize; i++) {
                for (int j = 0; j < blockSize; j++) {
                    squares[k][j + i * blockSize] = numbers[(blockSize * (k % blockSize) + j + (i + k / blockSize * blockSize) * sudokuSize)];
                }
            }
            satisfiedConstraints += countDistinct(squares[k]);
        }
        return satisfiedConstraints;
    }
}
