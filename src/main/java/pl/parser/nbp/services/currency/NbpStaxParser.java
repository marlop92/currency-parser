package pl.parser.nbp.services.currency;

import pl.parser.nbp.exceptions.XmlFIleException;
import pl.parser.nbp.model.CurrencyData;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;

public class NbpStaxParser implements NbpXmlParser{

    private static final String DIVISOR = "przelicznik";
    private static final String CURRENCY_CODE = "kod_waluty";
    private static final String PURCHASE_PRICE = "kurs_kupna";
    private static final String SALES_PRICE = "kurs_sprzedazy";
    private static final String XML_MONEY_DELIMITER = ",";
    private static final String JAVA_MONEY_DELIMITER = ".";
    private static final String PUBLICATION_DATE = "data_publikacji";
    private static final String FILE_UNAVAILABLE = "Selected file %s is unavailable";
    private static final String UNEXPECTED_XML_EXCEPTION = "Some problems occurred with XML. Processing aborted";

    @Override
    public Optional<CurrencyData> findCurrencyData(String filename, String expectedCurrencyCode) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlEventReader = createXmlReader("http://www.nbp.pl/kursy/xml/" + filename, xmlInputFactory);

        int divisor = 0;
        boolean searchedCurrencyFound = false;

        CurrencyData currencyData = new CurrencyData();
        currencyData.setCurrency(expectedCurrencyCode);

        while (xmlEventReader.hasNext()) {
            XMLEvent xmlEvent = getXmlEvent(xmlEventReader);
            if (!xmlEvent.isStartElement()) {
                continue;
            }

            StartElement currentEl = xmlEvent.asStartElement();
            if (isTagName(currentEl, PUBLICATION_DATE)) {
                currencyData.setPublicationDate(LocalDate.parse(getEventData(xmlEventReader)));
            }

            if (isTagName(currentEl, DIVISOR)) {
                divisor = getDivisor(xmlEventReader);
                continue;
            }

            if (isTagName(currentEl, CURRENCY_CODE)) {
                searchedCurrencyFound = isSearchedCurrency(expectedCurrencyCode, xmlEventReader);
                continue;
            }

            if (!searchedCurrencyFound) {
                continue;
            }

            if (isTagName(currentEl, PURCHASE_PRICE)) {
                currencyData.setPurchasePrice(divisor, getPrice(xmlEventReader));
                continue;
            }

            if (isTagName(currentEl, SALES_PRICE)) {
                currencyData.setSalesPrice(divisor, getPrice(xmlEventReader));
                return Optional.ofNullable(currencyData);
            }

        }

        return Optional.empty();
    }

    private XMLEventReader createXmlReader(String filename, XMLInputFactory xmlInputFactory) {
            XMLEventReader reader = null;
            int retry = 0;
            while(reader == null) {
                try {
                    reader = xmlInputFactory.createXMLEventReader(getReader(filename));
                } catch (XMLStreamException ex) {
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(retry == 10) {
                    throw new RuntimeException("Too much invalid connections");
                }
            }

            return reader;
    }

    private BufferedReader getReader(String filename) {
        try {
            return new BufferedReader(new InputStreamReader(new URL(filename).openStream()));
        } catch (IOException ex) {
            throw new XmlFIleException(String.format(FILE_UNAVAILABLE, filename));
        }
    }

    private boolean isSearchedCurrency(String expectedCurrencyCode, XMLEventReader xmlEventReader) {
        return expectedCurrencyCode.equals(getEventData(xmlEventReader));
    }

    private BigDecimal getPrice(XMLEventReader xmlEventReader) {
        return new BigDecimal(getEventData(xmlEventReader).replaceAll(XML_MONEY_DELIMITER, JAVA_MONEY_DELIMITER));
    }

    private int getDivisor(XMLEventReader xmlEventReader) {
        return Integer.parseInt(getEventData(xmlEventReader));
    }

    private boolean isTagName(StartElement tag, String name) {
        return name.equals(getTagName(tag));
    }

    private String getEventData(XMLEventReader xmlEventReader) {
        return getXmlEvent(xmlEventReader).asCharacters().getData();
    }

    private XMLEvent getXmlEvent(XMLEventReader xmlEventReader){
        try {
            return xmlEventReader.nextEvent();
        } catch (XMLStreamException ex) {
            throw new XmlFIleException(UNEXPECTED_XML_EXCEPTION);
        }
    }

    private String getTagName(StartElement startElement) {
        return startElement.getName().getLocalPart();
    }
}
