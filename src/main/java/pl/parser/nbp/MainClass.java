package pl.parser.nbp;

import pl.parser.nbp.model.CurrencyStats;
import pl.parser.nbp.model.CurrencyStatsRequest;
import pl.parser.nbp.services.currency.CurrencyService;
import pl.parser.nbp.services.currency.SimpleConcurrentCurrencyService;
import pl.parser.nbp.util.InputParser;

public class MainClass {

    public static void main(String[] args) {
        CurrencyStatsRequest request = InputParser.parseInput(args);
        CurrencyService currencyService = new SimpleConcurrentCurrencyService();
        CurrencyStats stats = currencyService.getCurrencyStats(request);
        System.out.println(stats.getAvgPurchasePrice());
        System.out.println(stats.getSalesStandardDeviation());
    }
}
