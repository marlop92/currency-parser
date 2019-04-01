package pl.parser.nbp.services.currency;

import pl.parser.nbp.model.CurrencyData;
import pl.parser.nbp.model.CurrencyKey;
import pl.parser.nbp.model.CurrencyStats;
import pl.parser.nbp.model.CurrencyStatsRequest;
import pl.parser.nbp.services.statistics.StatisticsService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SimpleConcurrentCurrencyService implements CurrencyService {

    private ExternalFileService externalFileService;
    private StatisticsService statisticsService;

    @Override
    public CurrencyStats getCurrencyStats(CurrencyStatsRequest request) {
        List<String> currencyFilenames = externalFileService.getCurrencyFilenames(request);
        List<CurrencyData> currencyData = getCurrencyData(currencyFilenames);

        List<BigDecimal> purchasePrices = getPurchasePrices(currencyData);
        List<BigDecimal> salesPrices = getSalesPrices(currencyData);

        BigDecimal average = statisticsService.calculateAverage(purchasePrices);
        BigDecimal standardDeviation = statisticsService.calculateStandardDeviation(salesPrices);

        return new CurrencyStats(request.getCurrencyCode(), average, standardDeviation, request.getStatsBeginDate(),
                request.getStatsEndDate());
    }

    private List<CurrencyData> getCurrencyData(List<String> currencyFilenames) {
        return currencyFilenames.stream().parallel().
                map(filename -> externalFileService.getCurrencyData(filename)).
                collect(Collectors.toList());
    }

    private List<BigDecimal> getPurchasePrices(List<CurrencyData> currencyData) {
        return currencyData.stream().
                map(n -> n.getPurchasePrice()).
                collect(Collectors.toList());
    }

    private List<BigDecimal> getSalesPrices(List<CurrencyData> currencyData) {
        return currencyData.stream().
                map(n -> n.getSalesPrice()).
                collect(Collectors.toList());
    }

}
