package pl.parser.nbp.services.currency;

import pl.parser.nbp.exceptions.CurrencyRequestValidationException;
import pl.parser.nbp.model.CurrencyData;
import pl.parser.nbp.model.CurrencyStats;
import pl.parser.nbp.model.CurrencyStatsRequest;
import pl.parser.nbp.services.files.FileService;
import pl.parser.nbp.services.files.NbpFileService;
import pl.parser.nbp.services.statistics.SimpleStatisticsService;
import pl.parser.nbp.services.statistics.StatisticsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SimpleConcurrentCurrencyService implements CurrencyService {

    public static final String FUTURE_DATE = "Any of date passed to the request can't be from future";
    public static final String END_DATE_BEFORE_BEGIN_DATE = "End date can't be earlier than begin date";
    public static final int CURRENCY_CODE_LENGTH = 3;
    public static final String INVALID_CURRENCY_CODE = "%s isn't valid currency code";
    public static final String INVALID_DATE = "null isn't valid date";
    private FileService fileService;
    private StatisticsService statisticsService;

    public SimpleConcurrentCurrencyService() {
        fileService = new NbpFileService();
        statisticsService = new SimpleStatisticsService();
    }

    public SimpleConcurrentCurrencyService(FileService fileService, StatisticsService statisticsService) {
        this.fileService = fileService;
        this.statisticsService = statisticsService;
    }

    @Override
    public CurrencyStats getCurrencyStats(CurrencyStatsRequest request) {
        validateRequest(request);
        List<String> currencyFilenames = fileService.getCurrencyFilenames(request);
        List<CurrencyData> currencyData = getCurrencyData(currencyFilenames, request.getCurrencyCode());

        List<BigDecimal> purchasePrices = getPurchasePrices(currencyData);
        List<BigDecimal> salesPrices = getSalesPrices(currencyData);

        BigDecimal average = statisticsService.calculateAverage(purchasePrices);
        BigDecimal standardDeviation = statisticsService.calculateStandardDeviation(salesPrices);

        return new CurrencyStats(request.getCurrencyCode(), average, standardDeviation, request.getStatsBeginDate(),
                request.getStatsEndDate());
    }

    private void validateRequest(CurrencyStatsRequest request) {
        if(request.getCurrencyCode() == null) {
            throw new CurrencyRequestValidationException(String.format(INVALID_CURRENCY_CODE, null));
        }

        if(request.getStatsBeginDate() == null || request.getStatsEndDate() == null) {
            throw new CurrencyRequestValidationException(String.format(INVALID_DATE, null));
        }

        if (request.getStatsBeginDate().isAfter(LocalDate.now()) || request.getStatsEndDate().isAfter(LocalDate.now())) {
            throw new CurrencyRequestValidationException(FUTURE_DATE);
        }

        if (request.getStatsBeginDate().isAfter(request.getStatsEndDate())) {
            throw new CurrencyRequestValidationException(END_DATE_BEFORE_BEGIN_DATE);
        }

        if (request.getCurrencyCode().length() != CURRENCY_CODE_LENGTH) {
            throw new CurrencyRequestValidationException(
                    String.format(INVALID_CURRENCY_CODE, request.getCurrencyCode()));
        }
    }

    private List<CurrencyData> getCurrencyData(List<String> currencyFilenames, String currencyCode) {
        return currencyFilenames.stream().parallel().
                map(filename -> fileService.getCurrencyData(filename, currencyCode)).
                flatMap(Optional::stream).
                collect(Collectors.toList());
    }

    private List<BigDecimal> getPurchasePrices(List<CurrencyData> currencyData) {
        return currencyData.stream().
                map(CurrencyData::getPurchasePrice).
                collect(Collectors.toList());
    }

    private List<BigDecimal> getSalesPrices(List<CurrencyData> currencyData) {
        return currencyData.stream().
                map(CurrencyData::getSalesPrice).
                collect(Collectors.toList());
    }

}
