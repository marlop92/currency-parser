package pl.parser.nbp.services.currency;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.parser.nbp.exceptions.CurrencyRequestValidationException;
import pl.parser.nbp.model.CurrencyData;
import pl.parser.nbp.model.CurrencyStats;
import pl.parser.nbp.model.CurrencyStatsRequest;
import pl.parser.nbp.services.files.FileService;
import pl.parser.nbp.services.statistics.StatisticsService;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(PER_CLASS)
class CurrencyServiceTest {

    CurrencyService currencyService;
    Clock presentDate;
    FileService fileService;
    StatisticsService statisticsService;

    @BeforeAll
    public void setOff() {
        fileService = mock(FileService.class);
        statisticsService = mock(StatisticsService.class);
        currencyService = new SimpleConcurrentCurrencyService(fileService, statisticsService);
        presentDate = Clock.systemDefaultZone();
    }

    @ParameterizedTest
    @CsvSource({"USD, 2, 1, USD",
                "CHF, 0, 0, CHF",
                "EUR, 1, 1, EUR",
                "GBP, 30, 0, GBP"})
    public void validDatesAndCodeShouldReturnCorrectStats(String currencyCode, int beginDateMinusDays, int endDateMinusDays,
                                                          String expectedCurrencyCode) {
        //given
        CurrencyStatsRequest request = new CurrencyStatsRequest(currencyCode, LocalDate.now(presentDate).minusDays(beginDateMinusDays),
                LocalDate.now(presentDate).minusDays(endDateMinusDays));

        CurrencyStats expected = new CurrencyStats(expectedCurrencyCode, new BigDecimal("3.9000"), new BigDecimal("0.1000"),
                LocalDate.now(presentDate).minusDays(beginDateMinusDays), LocalDate.now(presentDate).minusDays(endDateMinusDays));

        when(fileService.getCurrencyFilenames(new CurrencyStatsRequest(currencyCode, LocalDate.now(presentDate).minusDays(beginDateMinusDays),
                        LocalDate.now(presentDate).minusDays(endDateMinusDays)))).thenReturn(List.of("filename1", "filename2"));

        when(fileService.getCurrencyData("filename1", currencyCode))
                .thenReturn(Optional.of(new CurrencyData(currencyCode, LocalDate.now(presentDate).minusDays(beginDateMinusDays),
                        new BigDecimal("4.0"), new BigDecimal("4.1"))));

        when(fileService.getCurrencyData("filename2", currencyCode))
                .thenReturn(Optional.of(new CurrencyData(currencyCode, LocalDate.now(presentDate).minusDays(beginDateMinusDays),
                        new BigDecimal("3.8"), new BigDecimal("3.9"))));

        when(statisticsService.calculateAverage(List.of(new BigDecimal("4.0"), new BigDecimal("3.8"))))
                .thenReturn(new BigDecimal("3.9000"));

        when(statisticsService.calculateStandardDeviation(List.of(new BigDecimal("4.1"), new BigDecimal("3.9"))))
                .thenReturn(new BigDecimal("0.1000"));

        //when
        CurrencyStats result = currencyService.getCurrencyStats(request);

        //than
        assertEquals(expected, result);
    }

    @Test
    public void beginDateFromFutureShouldThrowException() {
        //given
        CurrencyStatsRequest request =
                new CurrencyStatsRequest("USD", LocalDate.now(presentDate).plusDays(1),
                        LocalDate.now(presentDate).plusDays(2));

        //when
        //than
        assertThrows(CurrencyRequestValidationException.class,
                () -> currencyService.getCurrencyStats(request),
                "Any of date passed to the request can't be from future");
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
                "Any of date passed to the request can't be from future");
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
                "End date can't be earlier than begin date");
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
                "ABCD isn't valid currency code");
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
                "null isn't valid currency code");
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
                "null isn't valid date");
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
                "null isn't valid date");
    }

}