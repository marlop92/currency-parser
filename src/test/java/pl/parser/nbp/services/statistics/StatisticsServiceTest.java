package pl.parser.nbp.services.statistics;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class StatisticsServiceTest {

    StatisticsService statisticsService;

    @BeforeAll
    public void setOff() {
        statisticsService = new SimpleStatisticsService();
    }

    @ParameterizedTest
    @CsvSource({
            "5, 5, 5, 5, 5",
            "8, 4, 2, 0, 3.5",
            "7, 8, 5, 5, 6.25"
    })
    public void notEmptyListShouldCalculateAverage(String num1, String num2, String num3, String num4, String expectedVal) {
        //given
        List<BigDecimal> numbers = getNumbers(num1, num2, num3, num4);
        BigDecimal expected = new BigDecimal(expectedVal);

        //when
        BigDecimal result = statisticsService.calculateAverage(numbers);

        //than
        assertEquals(expected.setScale(4), result.setScale(4));
    }

    @ParameterizedTest
    @CsvSource({
            "5, 5, 5, 5, 0",
            "2, 4, 2, 4, 1",
            "12, 2, 5, 1, 4.3012",
            "15, 9, 17, 2, 5.8470"
    })
    public void notEmptyListShouldCalculateStandardDeviation(String num1, String num2, String num3, String num4, String expectedVal) {
        //given
        List<BigDecimal> numbers = getNumbers(num1, num2, num3, num4);
        BigDecimal expected = new BigDecimal(expectedVal);

        //when
        BigDecimal result = statisticsService.calculateStandardDeviation(numbers);

        //than
        assertEquals(expected.setScale(4), result.setScale(4, RoundingMode.FLOOR));
    }

    @Test
    public void emptyListShouldReturnZeroAverage() {
        //given
        List<BigDecimal> numbers = Collections.emptyList();
        BigDecimal expected = new BigDecimal("0");

        //when
        BigDecimal result = statisticsService.calculateAverage(numbers);

        //than
        assertEquals(expected, result);
    }

    @Test
    public void emptyListShouldReturnZeroStandardDeviation() {
        //given
        List<BigDecimal> numbers = Collections.emptyList();
        BigDecimal expected = new BigDecimal("0");

        //when
        BigDecimal result = statisticsService.calculateStandardDeviation(numbers);

        //than
        assertEquals(expected, result);
    }

    private List<BigDecimal> getNumbers(String num1, String num2, String num3, String num4) {
        List<BigDecimal> numbers = new ArrayList<>();
        numbers.add(new BigDecimal(num1));
        numbers.add(new BigDecimal(num2));
        numbers.add(new BigDecimal(num3));
        numbers.add(new BigDecimal(num4));

        return numbers;
    }

}