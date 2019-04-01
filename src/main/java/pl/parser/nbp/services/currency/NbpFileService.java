package pl.parser.nbp.services.currency;

import pl.parser.nbp.model.CurrencyData;
import pl.parser.nbp.model.CurrencyStatsRequest;
import pl.parser.nbp.services.ExternalFileHandler;
import pl.parser.nbp.services.SingleFileHandler;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class NbpFileService implements ExternalFileService {

    private NbpXmlParser parser = new NbpStaxParser();
    private ExternalFileHandler handler = new ExternalFileHandler(new SingleFileHandler());

    @Override
    public List<String> getCurrencyFilenames(CurrencyStatsRequest request) {
        try {
            return handler.getFilenames(request.getStatsBeginDate(), request.getStatsEndDate());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Optional<CurrencyData> getCurrencyData(String currencyFilename, String currencyCode) {
        return new NbpStaxParser().findCurrencyData(currencyFilename, currencyCode);
    }
}
