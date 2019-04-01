package pl.parser.nbp.services.currency;

import pl.parser.nbp.model.CurrencyData;
import pl.parser.nbp.model.CurrencyStats;
import pl.parser.nbp.model.CurrencyStatsRequest;
import pl.parser.nbp.services.statistics.SimpleStatisticsService;
import pl.parser.nbp.services.statistics.StatisticsService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SimpleConcurrentCurrencyService implements CurrencyService {

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
        List<String> currencyFilenames = fileService.getCurrencyFilenames(request);
        List<CurrencyData> currencyData = getCurrencyData(currencyFilenames, request.getCurrencyCode());

        List<BigDecimal> purchasePrices = getPurchasePrices(currencyData);
        List<BigDecimal> salesPrices = getSalesPrices(currencyData);

        BigDecimal average = statisticsService.calculateAverage(purchasePrices);
        BigDecimal standardDeviation = statisticsService.calculateStandardDeviation(salesPrices);

        return new CurrencyStats(request.getCurrencyCode(), average, standardDeviation, request.getStatsBeginDate(),
                request.getStatsEndDate());
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
