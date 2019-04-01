package pl.parser.nbp.services.statistics;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SimpleStatisticsService implements StatisticsService {

    private static final int ACCURACY = 4;
    private static final int SQUARE = 2;

    @Override
    public BigDecimal calculateAverage(List<BigDecimal> numbers) {
        if(numbers.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal numbersCount = new BigDecimal(numbers.size());
        BigDecimal sum = calculateSum(numbers);

        return sum.divide(numbersCount, ACCURACY, RoundingMode.FLOOR);
    }

    @Override
    public BigDecimal calculateStandardDeviation(List<BigDecimal> numbers) {
        if(numbers.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal variance = calculateVariance(numbers);
        return variance.sqrt(MathContext.DECIMAL32);
    }

    private BigDecimal calculateVariance(List<BigDecimal> numbers) {
        BigDecimal numbersCount = new BigDecimal(numbers.size());
        BigDecimal average = calculateAverage(numbers);
        BigDecimal varianceTmp = numbers.stream().
                map(val -> val.subtract(average)).
                map(val -> val.pow(SQUARE)).
                reduce(BigDecimal.ZERO, BigDecimal::add);

        return varianceTmp.divide(numbersCount, ACCURACY, RoundingMode.FLOOR);
    }


    private BigDecimal calculateSum(List<BigDecimal> numbers) {
        return numbers.stream().
                reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
