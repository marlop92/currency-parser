package pl.parser.nbp.services.currency;

import pl.parser.nbp.model.CurrencyStats;
import pl.parser.nbp.model.CurrencyStatsRequest;

public interface CurrencyService {

    CurrencyStats getCurrencyStats(CurrencyStatsRequest request);
}
