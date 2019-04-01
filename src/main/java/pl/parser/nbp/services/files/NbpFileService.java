package pl.parser.nbp.services.files;

import pl.parser.nbp.model.CurrencyData;
import pl.parser.nbp.model.CurrencyStatsRequest;
import pl.parser.nbp.services.parser.text.NbpConcurrentTextFileParser;
import pl.parser.nbp.services.parser.text.NbpTextFileParser;
import pl.parser.nbp.services.parser.xml.NbpStaxParser;
import pl.parser.nbp.services.parser.xml.NbpXmlParser;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class NbpFileService implements FileService {

    private NbpXmlParser xmlParser;
    private NbpTextFileParser textFileParser;

    public NbpFileService() {
        xmlParser = new NbpStaxParser();
        textFileParser = new NbpConcurrentTextFileParser();
    }

    public NbpFileService(NbpXmlParser xmlParser, NbpTextFileParser textFileParser) {
        this.xmlParser = xmlParser;
        this.textFileParser = textFileParser;
    }

    @Override
    public List<String> getCurrencyFilenames(CurrencyStatsRequest request) {
        try {
            return textFileParser.getFilenames(request.getStatsBeginDate(), request.getStatsEndDate());
        } catch (MalformedURLException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Optional<CurrencyData> getCurrencyData(String currencyFilename, String currencyCode) {
        return xmlParser.findCurrencyData(currencyFilename, currencyCode);
    }
}
