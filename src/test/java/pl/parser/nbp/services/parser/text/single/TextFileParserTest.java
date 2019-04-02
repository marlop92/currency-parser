package pl.parser.nbp.services.parser.text.single;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class TextFileParserTest {

    TextFileParser textFileParser;

    @Test
    public void matchingDatesFormatsShouldReturnValidFilenames() throws FileNotFoundException {
        //given
        String filename = "src/test/resources/dir.txt";
        textFileParser = new SingleTextFileParser(new FileReader(filename));
        List<String> expected = List.of("c003z090909");

        //when
        List<String> result = textFileParser.getFilenames(filename, 90908,90910);

        //than
        assertEquals(expected, result);
    }
}