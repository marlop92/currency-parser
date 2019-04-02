package pl.parser.nbp.services.currency;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.parser.nbp.exceptions.CurrencyRequestValidationException;
import pl.parser.nbp.model.CurrencyStats;
import pl.parser.nbp.model.CurrencyStatsRequest;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class CurrencyServiceTest {

    CurrencyService currencyService;
    Clock presentDate;

    @BeforeAll
    public void setOff() {
        currencyService = new SimpleConcurrentCurrencyService();
        presentDate = Clock.systemDefaultZone();
    }

    @ParameterizedTest
    @CsvSource({"USD, 2, 1",
                "CHF, 0, 0",
                "EUR, 1, 1",
                "GBP, 30, 0"})
    public void validDatesAndCodeShouldReturnCorrectStats(String currencyCode, int beginDateMinusDays, int endDateMinusDays) {
        //given
        CurrencyStatsRequest request = new CurrencyStatsRequest(currencyCode, LocalDate.now(presentDate).minusDays(beginDateMinusDays),
                LocalDate.now(presentDate).minusDays(endDateMinusDays));

        CurrencyStats expected = new CurrencyStats("USD", new BigDecimal("3.90"), new BigDecimal("0.1"),
                LocalDate.now(presentDate).minusDays(beginDateMinusDays), LocalDate.now(presentDate).minusDays(endDateMinusDays));

        //when
        CurrencyStats result = currencyService.getCurrencyStats(request);

        //than
        assertEquals(expected, result);
    }

    @Test
    public void endDateFromFutureShouldThrowException() {
        //given
        CurrencyStatsRequest request =
                new CurrencyStatsRequest("USD", LocalDate.now(presentDate), LocalDate.now(presentDate).plusDays(2));

        //when
        //than
        assertThrows(CurrencyRequestValidationException.class,
                () -> currencyService.getCurrencyStats(request),
                "Invalid end date: future date is illegal");
    }

    @Test
    public void endDateBeforeBeginDateShouldThrowException() {
        //given
        CurrencyStatsRequest request =
                new CurrencyStatsRequest("USD", LocalDate.now(presentDate), LocalDate.now(presentDate).minusDays(2));

        //when
        //than
        assertThrows(CurrencyRequestValidationException.class,
                () -> currencyService.getCurrencyStats(request),
                "Invalid end date: endDate is earlier than beginDate");
    }

    @Test
    public void invalidCurrencyCodeShouldThrowException() {
        //given
        CurrencyStatsRequest request =
                new CurrencyStatsRequest("ABCD", LocalDate.now(presentDate), LocalDate.now(presentDate));

        //when
        //than
        assertThrows(CurrencyRequestValidationException.class,
                () -> currencyService.getCurrencyStats(request),
                "Invalid currency: ABCD");
    }

    @Test
    public void nullCurrencyShouldThrowException() {
        //given
        CurrencyStatsRequest request =
                new CurrencyStatsRequest(null, LocalDate.now(presentDate), LocalDate.now(presentDate));

        //when
        //than
        assertThrows(CurrencyRequestValidationException.class,
                () -> currencyService.getCurrencyStats(request),
                "Invalid currency: null");
    }

    @Test
    public void nullBeginDateShouldThrowException() {
        //given
        CurrencyStatsRequest request =
                new CurrencyStatsRequest("USD", null, LocalDate.now(presentDate));

        //when
        //than
        assertThrows(CurrencyRequestValidationException.class,
                () -> currencyService.getCurrencyStats(request),
                "Invalid begin date: null");
    }

    @Test
    public void nullEndDateShouldThrowException() {
        //given
        CurrencyStatsRequest request =
                new CurrencyStatsRequest("USD", LocalDate.now(presentDate), null);

        //when
        //than
        assertThrows(CurrencyRequestValidationException.class,
                () -> currencyService.getCurrencyStats(request),
                "Invalid end date: null");
    }

}