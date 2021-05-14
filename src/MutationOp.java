import org.jgap.*;
import org.jgap.Configuration;
import org.jgap.impl.MutationOperator;
import org.jgap.IUniversalRateCalculator;
import org.jgap.InvalidConfigurationException;

import java.util.List;

public class MutationOp extends MutationOperator {
    public MutationOp() throws InvalidConfigurationException {
    }

    public MutationOp(Configuration a_conf) throws InvalidConfigurationException {
        super(a_conf);
    }

    public MutationOp(Configuration a_config, IUniversalRateCalculator a_mutationRateCalculator) throws InvalidConfigurationException {
        super(a_config, a_mutationRateCalculator);
    }

    public MutationOp(Configuration a_config, int a_desiredMutationRate) throws InvalidConfigurationException {
        super(a_config, a_desiredMutationRate);
    }

    @Override
    public void operate(Population a_population, List a_candidateChromosomes) {
        super.operate(a_population, a_candidateChromosomes);
        int rate = getMutationRate();
        int[] currentChromosome;
        for (int i = 0; i < a_candidateChromosomes.size(); i++) {
//            currentChromosome = a_candidateChromosomes.get(i);// ToDO
//            for (int j = 0; j < currentChromosome.length; j++) {
//                if (getRandomNumber(0, rate + 1) == 0) {
//                do some shit
//                }
//            }
        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}