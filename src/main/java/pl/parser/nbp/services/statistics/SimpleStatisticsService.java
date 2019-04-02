package pl.parser.nbp.services.statistics;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class SimpleStatisticsService implements StatisticsService {

    private static final int ACCURACY = 4;
    private static final int MEDIAN_ACCURACY = 12;
    private static final int SQUARE = 2;

    @Override
    public BigDecimal calculateAverage(List<BigDecimal> numbers) {
        if(numbers.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal numbersCount = new BigDecimal(numbers.size());
        BigDecimal sum = calculateSum(numbers);

        return sum.divide(numbersCount, ACCURACY, RoundingMode.HALF_EVEN);
    }

    @Override
    public BigDecimal calculateStandardDeviation(List<BigDecimal> numbers) {
        if(numbers.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal variance = calculateVariance(numbers);
        return variance.sqrt(MathContext.DECIMAL32).setScale(ACCURACY, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calculateVariance(List<BigDecimal> numbers) {
        BigDecimal numbersCount = new BigDecimal(numbers.size());
        BigDecimal average = calculateAverage(numbers);
        BigDecimal varianceTmp = numbers.stream().
                map(val -> val.subtract(average)).
                map(val -> val.pow(SQUARE)).
                reduce(BigDecimal.ZERO, BigDecimal::add);

        return varianceTmp.divide(numbersCount, MEDIAN_ACCURACY, RoundingMode.HALF_EVEN);
    }


    private BigDecimal calculateSum(List<BigDecimal> numbers) {
        return numbers.stream().
                reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
