package pl.parser.nbp.services.currency;

import pl.parser.nbp.model.CurrencyStats;
import pl.parser.nbp.model.CurrencyStatsRequest;

import java.math.BigDecimal;

public class XmlCurrencyService implements CurrencyService {

    @Override
    public CurrencyStats getCurrencyStats(CurrencyStatsRequest request) {

        return new CurrencyStats(request.getCurrencyCode(), new BigDecimal(0), new BigDecimal(0),
                request.getStatsBeginDate(), request.getStatsEndDate());
    }

}
