package pl.parser.nbp.services.parser.xml;

import pl.parser.nbp.model.CurrencyData;

import java.util.Optional;

public interface NbpXmlParser {

    Optional<CurrencyData> findCurrencyData(String filename, String currencyCode);
}
