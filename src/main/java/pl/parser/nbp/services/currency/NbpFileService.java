package pl.parser.nbp.services.currency;

import pl.parser.nbp.model.CurrencyData;
import pl.parser.nbp.model.CurrencyStatsRequest;

import java.util.List;
import java.util.Optional;

public class NbpFileService implements ExternalFileService {

    private NbpXmlParser parser;

    @Override
    public List<String> getCurrencyFilenames(CurrencyStatsRequest request) {
        return null;
    }

    @Override
    public Optional<CurrencyData> getCurrencyData(String currencyFilename, String currencyCode) {
        return parser.findCurrencyData(currencyFilename, currencyCode);
    }
}
