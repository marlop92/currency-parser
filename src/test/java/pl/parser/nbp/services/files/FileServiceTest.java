package pl.parser.nbp.services.files;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import pl.parser.nbp.model.CurrencyData;
import pl.parser.nbp.model.CurrencyStatsRequest;
import pl.parser.nbp.services.parser.text.NbpTextFileParser;
import pl.parser.nbp.services.parser.xml.NbpXmlParser;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(PER_CLASS)
class FileServiceTest {

    FileService fileService;
    NbpXmlParser xmlParser;
    NbpTextFileParser textFileParser;
    Clock presentDate;

    @BeforeAll
    public void setOff() {
        xmlParser = mock(NbpXmlParser.class);
        textFileParser = mock(NbpTextFileParser.class);
        fileService = new NbpFileService(xmlParser, textFileParser);
        presentDate = Clock.systemDefaultZone();
    }

    @Test
    public void validCurrencyRequestShouldReturnFilenamesList() {
        //given
        CurrencyStatsRequest request = new CurrencyStatsRequest("USD", LocalDate.now(presentDate).minusDays(2),
                LocalDate.now(presentDate).minusDays(1));
        when(textFileParser.getFilenames(LocalDate.now(presentDate).minusDays(2), LocalDate.now(presentDate).minusDays(1)))
                .thenReturn(List.of("filename1"));

        //when
        List<String> result = fileService.getCurrencyFilenames(request);

        //than
        assertEquals(List.of("filename1"), result);
    }

    @Test
    public void validCurrencyFilenameAndCodeShouldReturnCurrencyData() {
        //given
        String currencyFilename = "filename1.xml";
        String currencyCode = "USD";
        Optional<CurrencyData> expected = Optional.of(new CurrencyData("USD", LocalDate.now(presentDate).minusDays(2),
                new BigDecimal("3.9"), new BigDecimal("4.0")));
        when(xmlParser.findCurrencyData("filename1.xml", "USD"))
                .thenReturn(Optional.of(new CurrencyData("USD", LocalDate.now(presentDate).minusDays(2),
                        new BigDecimal("3.9"), new BigDecimal("4.0"))));

        //when
        Optional<CurrencyData> result = fileService.getCurrencyData(currencyFilename, currencyCode);

        //than
        assertEquals(expected, result);
    }
}