package pl.parser.nbp.services;

import pl.parser.nbp.exceptions.InvalidDateException;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExternalFileHandler {

    private static final int NBP_FIRST_HANDLED_YEAR = 2002;
    private static final int CURRENT_YEAR = LocalDate.now().getYear();
    private static final String FILE_SOURCE_FORMAT = ".txt";
    private static final String FILE_SOURCE_BASE = "https://www.nbp.pl/kursy/xml/dir";
    private final SingleFileHandler handler;

    public ExternalFileHandler(SingleFileHandler handler) {
        this.handler = handler;
    }

    public List<String> getFilenames(LocalDate beginDate, LocalDate endDate) throws MalformedURLException {
        validateDates(beginDate, endDate);

        List<URL> fileSources = getFileSources(beginDate, endDate);
        List<String> filenames = fileSources.stream().parallel().
                map(source -> handler.getFilenames(source).stream()).
                flatMap(n -> n).
                collect(Collectors.toList())
                .stream()
                .map(n -> new StringBuilder(n).append(".xml").toString())
                .collect(Collectors.toList());

        return filenames;
    }

    private void validateDates(LocalDate beginDate, LocalDate endDate) {
        if(endDate.isBefore(beginDate)) {
            throw new InvalidDateException("End date can't be earlier than begin date");
        }

        if(beginDate.isAfter(LocalDate.now()) || endDate.isAfter(LocalDate.now())) {
            throw new InvalidDateException("Dates from future are illegal");
        }

        if(beginDate.getYear() < NBP_FIRST_HANDLED_YEAR) {
            throw new InvalidDateException(String.format("NBP has no data from %d year", beginDate.getYear()));
        }
    }

    private List<URL> getFileSources(LocalDate startDate, LocalDate endDate) throws MalformedURLException {
        List<URL> filesSources = new ArrayList<>();

        for(int year = startDate.getYear(); year <= endDate.getYear(); year++) {
            if(CURRENT_YEAR == year) {
                filesSources.add(new URL(FILE_SOURCE_BASE + FILE_SOURCE_FORMAT));
                break;
            }

            filesSources.add(new URL(FILE_SOURCE_BASE + year + FILE_SOURCE_FORMAT));
        }

        return filesSources;
    }
}
