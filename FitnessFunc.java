import org.jgap.FitnessFunction;
import org.jgap.IChromosome;
import java.util.Arrays;
import java.util.Random;

//fitness function considers number of duplicated numbers in the array
public class FitnessFunc extends FitnessFunction {
    @Override
    protected double evaluate(IChromosome iChromosome) {
        Random rnd = new Random() ;
        int i = (Math.abs(rnd.nextInt())) ;
        return i ;
    }
//maybe this function can help
    private void numDuplicate(int numbers[]) {
        Arrays.sort(numbers);
        int previous = numbers[0] - 1;

        int dupCount = 0;

        for (int i = 0; i < numbers.length; ++i) {
            if (numbers[i] == previous) {
                ++dupCount;
            } else {
                previous = numbers[i];
            }
        }
        System.out.println("There were " + dupCount + " duplicates in the array.");
    }
}
