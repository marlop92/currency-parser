package pl.parser.nbp.services.parser.text;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import pl.parser.nbp.exceptions.InvalidDateException;
import pl.parser.nbp.services.parser.text.single.TextFileParser;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(PER_CLASS)
class NbpTextFileParserTest {

    NbpTextFileParser parser;
    TextFileParser textFileParser;
    Clock presentDate;

    @BeforeAll
    public void setOff() {
        textFileParser = mock(TextFileParser.class);
        parser = new NbpConcurrentTextFileParser(textFileParser);
        presentDate = Clock.systemDefaultZone();
    }

    @Test
    public void correctDatesShouldReturnFilenamesList() {
        //given
        String rawBeginDate = "2009-10-10";
        String rawEndDate = "2010-10-11";
        LocalDate beginDate = LocalDate.parse(rawBeginDate);
        LocalDate endDate = LocalDate.parse(rawEndDate);
        when(textFileParser.getFilenames("https://www.nbp.pl/kursy/xml/dir2009.txt", 91010,
                101011)).thenReturn(List.of("filename1", "filename2"));
        when(textFileParser.getFilenames("https://www.nbp.pl/kursy/xml/dir2010.txt", 91010,
                101011)).thenReturn(List.of("filename3"));
        List<String> expected = getExpectedList();

        //when
        List<String> result = parser.getFilenames(beginDate, endDate);

        //than
        expected.sort(String::compareToIgnoreCase);
        result.sort(String::compareToIgnoreCase);

        assertEquals(expected, result);
    }

    @Test
    public void futureBeginDateShouldThrowException() {
        //given
        LocalDate beginDate = LocalDate.now(presentDate).plusDays(1);
        LocalDate endDate = LocalDate.now(presentDate);

        //when
        //than
        assertThrows(InvalidDateException.class,
                () -> parser.getFilenames(beginDate, endDate),
                "Dates from future are illegal");
    }

    @Test
    public void futureEndDateShouldThrowException() {
        //given
        LocalDate beginDate = LocalDate.now(presentDate);
        LocalDate endDate = LocalDate.now(presentDate).plusDays(1);

        //when
        //than
        assertThrows(InvalidDateException.class,
                () -> parser.getFilenames(beginDate, endDate),
                "Dates from future are illegal");
    }

    @Test
    public void endDateBeforeBeginDateShouldThrowException() {
        //given
        LocalDate beginDate = LocalDate.now(presentDate);
        LocalDate endDate = LocalDate.now(presentDate).minusDays(1);

        //when
        //than
        assertThrows(InvalidDateException.class,
                () -> parser.getFilenames(beginDate, endDate),
                "End date can't be earlier than begin date");
    }

    @Test
    public void beginDateBeforeNbpFirstHandledYearShouldThrowException() {
        //given
        String rawBeginDate = "2001-01-01";
        LocalDate beginDate = LocalDate.parse("2001-01-01");
        LocalDate endDate = LocalDate.now(presentDate);

        //when
        //than
        assertThrows(InvalidDateException.class,
                () -> parser.getFilenames(beginDate, endDate),
                "NBP has no data available for 2001 year");
    }

    private List<String> getExpectedList() {
        List<String> expected = new ArrayList<>();
        expected.add("filename1.xml");
        expected.add("filename2.xml");
        expected.add("filename3.xml");
        return expected;
    }
}