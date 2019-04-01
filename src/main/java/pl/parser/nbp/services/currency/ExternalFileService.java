package pl.parser.nbp.services.currency;

import pl.parser.nbp.model.CurrencyData;
import pl.parser.nbp.model.CurrencyStatsRequest;

import java.util.List;

public interface ExternalFileService {

    List<String> getCurrencyFilenames(CurrencyStatsRequest request);

    CurrencyData getCurrencyData(String currencyFilename);
}
