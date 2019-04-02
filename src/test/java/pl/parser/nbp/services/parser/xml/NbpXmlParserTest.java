package pl.parser.nbp.services.parser.xml;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import pl.parser.nbp.model.CurrencyData;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class NbpXmlParserTest {

    NbpXmlParser parser;

    @Test
    public void validXmlShouldReturnCurrencyData() throws FileNotFoundException {
        //given
        String filename = "src/test/resources/c001z190101.xml";
        parser = new NbpStaxParser(new FileReader(filename));
        String currencyCode = "USD";
        Optional<CurrencyData> expected = Optional.of(new CurrencyData("USD", LocalDate.parse("2019-01-01"),
                new BigDecimal("2.8210"), new BigDecimal("2.8780")));

        //when
        Optional<CurrencyData> result = parser.findCurrencyData(filename, currencyCode);

        //than
        assertEquals(expected, result);
    }
}