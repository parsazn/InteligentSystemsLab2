import org.jgap.Configuration;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.impl.CrossoverOperator;

import java.util.List;

public class CrossoverOp extends CrossoverOperator {

    public CrossoverOp() throws InvalidConfigurationException {
    }

    public CrossoverOp(Configuration a_configuration) throws InvalidConfigurationException {
        super(a_configuration);
    }

    public CrossoverOp(Configuration a_configuration, int a_desiredCrossoverRate) throws InvalidConfigurationException {
        super(a_configuration, a_desiredCrossoverRate);
    }

    @Override
    public void operate(Population a_population, List a_candidateChromosomes) {
    }
}
