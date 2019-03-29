package pl.parser.nbp.services.currency;

import pl.parser.nbp.model.CurrencyStats;

import java.time.LocalDate;

public interface CurrencyService {

    CurrencyStats getCurrencyStats(String currencyCode, LocalDate statsBeginDate, LocalDate statsEndDate);
}
