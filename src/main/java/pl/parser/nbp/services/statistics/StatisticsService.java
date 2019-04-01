package pl.parser.nbp.services.statistics;

import java.math.BigDecimal;
import java.util.List;

public interface StatisticsService {

    BigDecimal calculateAverage(List<BigDecimal> numbers);

    BigDecimal calculateStandardDeviation(List<BigDecimal> numbers);

}
