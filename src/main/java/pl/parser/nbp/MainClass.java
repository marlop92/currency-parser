package pl.parser.nbp;

import pl.parser.nbp.model.CurrencyStats;
import pl.parser.nbp.model.CurrencyStatsRequest;
import pl.parser.nbp.services.currency.SimpleConcurrentCurrencyService;

import java.time.LocalDate;

public class MainClass {

    public static void main(String[] args) {
        SimpleConcurrentCurrencyService simpleConcurrentCurrencyService = new SimpleConcurrentCurrencyService();
        CurrencyStats stats = simpleConcurrentCurrencyService.getCurrencyStats(new CurrencyStatsRequest("USD",
                LocalDate.parse("2015-04-13"), LocalDate.now()));
        System.out.println(stats);
    }
}
