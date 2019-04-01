package pl.parser.nbp.services.currency;

import pl.parser.nbp.model.CurrencyData;
import pl.parser.nbp.model.CurrencyStatsRequest;

import java.util.List;
import java.util.Optional;

public interface FileService {

    List<String> getCurrencyFilenames(CurrencyStatsRequest request);

    Optional<CurrencyData> getCurrencyData(String currencyFilename, String currencyCode);
}
