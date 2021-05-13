import org.jgap.FitnessFunction;
import org.jgap.IChromosome;


//fitness function considers number of duplicated numbers in the array
public class FitnessFunc extends FitnessFunction {
    @Override
    protected double evaluate(IChromosome iChromosome) {
        int[] chromosome = SudokuGa.chromosomeIntoSudoku(iChromosome);
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

    private int uniqueInRow(int[] numbers) {
        int satisfiedConstraints = 0;
        int[][] rows = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(numbers, i * 9, rows[i], 0, 9);
            if (countDistinct(rows[i]) == 9) satisfiedConstraints += 1;
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
            if (countDistinct(columns[i]) == 9) satisfiedConstraints += 1;
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
            if (countDistinct(squares[k]) == 9) satisfiedConstraints += 1;
        }
        return satisfiedConstraints;
    }
}
