package pl.parser.nbp.services.currency;

import pl.parser.nbp.model.CurrencyStats;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CurrencyServiceImpl implements CurrencyService {

    @Override
    public CurrencyStats getCurrencyStats(String currencyCode, LocalDate statsBeginDate, LocalDate statsEndDate) {

        return new CurrencyStats(currencyCode, new BigDecimal(0), new BigDecimal(0), statsBeginDate, statsEndDate);
    }
}
