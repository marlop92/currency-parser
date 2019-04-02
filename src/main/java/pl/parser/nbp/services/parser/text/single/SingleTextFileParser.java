package pl.parser.nbp.services.parser.text.single;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SingleTextFileParser implements TextFileParser {

    private static final int DATE_PATTERN_FIRST_LETTER = 5;
    private static final char PURCHASE_SALES_CURRENCY_FILE_SIGN = 'c';
    private static final char FIRST_EMPTY_CHAR = '\uFEFF';
    private static final String UNEXPECTED_PROBLEM = "Unexpected error occurred. Check your network connection";
    private static final String INVALID_URL = "Application was unable to create %s URL path";

    private FileReader fileReader;

    public SingleTextFileParser() {
    }

    public SingleTextFileParser(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    @Override
    public List<String> getFilenames(String filename, int minDateFilePattern, int maxDateFilePattern) {
        List<String> filenames = new ArrayList<>();

        try (BufferedReader reader = getBufferedReader(filename)) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = removeEmptyChar(line);
                if (isCorrectFile(line, minDateFilePattern, maxDateFilePattern)) {
                    filenames.add(line);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(UNEXPECTED_PROBLEM);
        }

        return filenames;
    }

    private String removeEmptyChar(String line) {
        if (line.charAt(0) == FIRST_EMPTY_CHAR) {
            line = line.substring(1);
        }
        return line;
    }

    private boolean isCorrectFile(String line, int minDateFilePattern, int maxDateFilePattern) {
        int dateFilePattern = Integer.valueOf(line.substring(DATE_PATTERN_FIRST_LETTER));
        return line.charAt(0) == PURCHASE_SALES_CURRENCY_FILE_SIGN && dateFilePattern >= minDateFilePattern
                && dateFilePattern <= maxDateFilePattern;
    }

    private BufferedReader getBufferedReader(String filename) throws IOException {
        if(fileReader == null) {
            return new BufferedReader(new InputStreamReader(createUrl(filename).openStream()));
        }

        return new BufferedReader(fileReader);
    }

    private URL createUrl(String filename) {
        try {
            return new URL(filename);
        } catch (MalformedURLException ex) {
            throw new IllegalStateException(String.format(INVALID_URL, filename));
        }
    }
}
