package pl.parser.nbp.services.currency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.parser.nbp.exceptions.CurrencyRequestValidationException;
import pl.parser.nbp.model.CurrencyStats;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CurrencyServiceTest {

    CurrencyService currencyService = new CurrencyServiceImpl();

    @ParameterizedTest
    @CsvSource({"USD, 2, 3",
                "CHF, 0, 0",
                "EUR, 1, 1",
                "GBP, 30, 0"})
    public void validDatesAndCodeShouldReturnCorrectStats(String currencyCode, int beginDateMinusDays, int endDateMinusDays) {
        //given
        Clock presentDate = Clock.systemDefaultZone();
        LocalDate beginDate = LocalDate.now(presentDate).minusDays(beginDateMinusDays);
        LocalDate endDate = LocalDate.now(presentDate).minusDays(endDateMinusDays);
        CurrencyStats expected = new CurrencyStats("USD", new BigDecimal("3.90"), new BigDecimal("0.1"),
                LocalDate.now(presentDate).minusDays(beginDateMinusDays),
                LocalDate.now(presentDate).minusDays(endDateMinusDays));

        //when
        CurrencyStats result = currencyService.getCurrencyStats(currencyCode, beginDate, endDate);

        //than
        assertEquals(expected, result);
    }

    @Test
    public void beginDateFromFutureShouldThrowException() {
        //given
        String currencyCode = "USD";
        Clock presentDate = Clock.systemDefaultZone();
        LocalDate beginDate = LocalDate.now(presentDate).plusDays(1);
        LocalDate endDate = LocalDate.now(presentDate).minusDays(2);

        //when
        //than
        assertThrows(CurrencyRequestValidationException.class,
                () -> currencyService.getCurrencyStats(currencyCode,  beginDate, endDate),
                "Invalid begin date: future date is illegal");
    }

    @Test
    public void endDateBeforeBeginDateShouldThrowException() {
        //given
        String currencyCode = "USD";
        Clock presentDate = Clock.systemDefaultZone();
        LocalDate beginDate = LocalDate.now(presentDate);
        LocalDate endDate = LocalDate.now(presentDate).minusDays(2);

        //when
        //than
        assertThrows(CurrencyRequestValidationException.class,
                () -> currencyService.getCurrencyStats(currencyCode,  beginDate, endDate),
                "Invalid end date: endDate is earlier than beginDate");
    }

    @Test
    public void invalidCurrencyCodeShouldThrowException() {
        //given
        String currencyCode = "ABCD";
        Clock presentDate = Clock.systemDefaultZone();
        LocalDate beginDate = LocalDate.now(presentDate);
        LocalDate endDate = LocalDate.now(presentDate).plusDays(2);

        //when
        //than
        assertThrows(CurrencyRequestValidationException.class,
                () -> currencyService.getCurrencyStats(currencyCode, beginDate, endDate),
                "Invalid currency: ABCD");
    }

    @Test
    public void nullCurrencyShouldThrowException() {
        //given
        String currencyCode = null;
        Clock presentDate = Clock.systemDefaultZone();
        LocalDate beginDate = LocalDate.now(presentDate);
        LocalDate endDate = LocalDate.now(presentDate).plusDays(2);

        //when
        //than
        assertThrows(CurrencyRequestValidationException.class,
                () -> currencyService.getCurrencyStats(currencyCode, beginDate, endDate),
                "Invalid currency: null");
    }

    @Test
    public void nullBeginDateShouldThrowException() {
        //given
        String currencyCode = "USD";
        Clock presentDate = Clock.systemDefaultZone();
        LocalDate beginDate = null;
        LocalDate endDate = LocalDate.now(presentDate).plusDays(2);

        //when
        //than
        assertThrows(CurrencyRequestValidationException.class,
                () -> currencyService.getCurrencyStats(currencyCode, beginDate, endDate),
                "Invalid begin date: null");
    }

    @Test
    public void nullEndDateShouldThrowException() {
        //given
        String currencyCode = "USD";
        Clock presentDate = Clock.systemDefaultZone();
        LocalDate beginDate = LocalDate.now(presentDate);
        LocalDate endDate = null;

        //when
        //than
        assertThrows(CurrencyRequestValidationException.class,
                () -> currencyService.getCurrencyStats(currencyCode, beginDate, endDate),
                "Invalid end date: null");
    }

}