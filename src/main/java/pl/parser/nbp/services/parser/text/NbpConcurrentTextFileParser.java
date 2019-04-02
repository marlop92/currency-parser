package pl.parser.nbp.services.parser.text;

import pl.parser.nbp.exceptions.InvalidDateException;
import pl.parser.nbp.services.parser.text.single.SingleTextFileParser;
import pl.parser.nbp.services.parser.text.single.TextFileParser;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NbpConcurrentTextFileParser implements NbpTextFileParser {

    private static final int NBP_FIRST_HANDLED_YEAR = 2002;
    private static final int CURRENT_YEAR = LocalDate.now().getYear();
    private static final String FILE_SOURCE_FORMAT = ".txt";
    private static final String FILE_SOURCE_BASE = "https://www.nbp.pl/kursy/xml/dir";
    private static final String INVALID_URL = "Application was unable to create %s URL path";
    private static final String END_DATE_BEFORE_BEGIN_DATE = "End date can't be earlier than begin date";
    private static final String FUTURE_DATES = "Dates from future are illegal";
    private static final String YEAR_NOT_HANDLED = "NBP has no data available for %d year";
    private static final String XML_FORMAT = ".xml";

    private final TextFileParser textFileParser;

    public NbpConcurrentTextFileParser() {
        textFileParser = new SingleTextFileParser();
    }

    public NbpConcurrentTextFileParser(SingleTextFileParser textFileParser) {
        this.textFileParser = textFileParser;
    }

    @Override
    public List<String> getFilenames(LocalDate beginDate, LocalDate endDate) {
        validateDates(beginDate, endDate);
        int minDateFilePattern = calculateFilePattern(beginDate);
        int maxDateFilePattern = calculateFilePattern(endDate);

        List<URL> fileSources = getFileSources(beginDate, endDate);
        List<String> filenames = fileSources.stream().parallel()
                .map(url -> textFileParser.getFilenames(url, minDateFilePattern, maxDateFilePattern))
                .flatMap(Collection::stream)
                .map(filename -> filename + XML_FORMAT)
                .collect(Collectors.toList());

        return filenames;
    }

    private int calculateFilePattern(LocalDate beginDate) {
        return (beginDate.getYear() % 100) * 10000 + beginDate.getMonth().getValue() * 100 + beginDate.getDayOfMonth();
    }

    private void validateDates(LocalDate beginDate, LocalDate endDate) {
        if (endDate.isBefore(beginDate)) {
            throw new InvalidDateException(END_DATE_BEFORE_BEGIN_DATE);
        }

        if (beginDate.isAfter(LocalDate.now()) || endDate.isAfter(LocalDate.now())) {
            throw new InvalidDateException(FUTURE_DATES);
        }

        if (beginDate.getYear() < NBP_FIRST_HANDLED_YEAR) {
            throw new InvalidDateException(String.format(YEAR_NOT_HANDLED, beginDate.getYear()));
        }
    }

    private List<URL> getFileSources(LocalDate startDate, LocalDate endDate) {
        List<URL> filesSources = new ArrayList<>();

        for (int year = startDate.getYear(); year <= endDate.getYear(); year++) {
            if (CURRENT_YEAR == year) {
                filesSources.add(createUrl(FILE_SOURCE_BASE + FILE_SOURCE_FORMAT));
                break;
            }

            filesSources.add(createUrl(FILE_SOURCE_BASE + year + FILE_SOURCE_FORMAT));
        }

        return filesSources;
    }

    private URL createUrl(String filename) {
        try {
            return new URL(filename);
        } catch (MalformedURLException ex) {
            throw new IllegalStateException(String.format(INVALID_URL, filename));
        }
    }
}
