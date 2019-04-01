package pl.parser.nbp.services;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;

public interface NbpTextFileParser {

    List<String> getFilenames(LocalDate beginDate, LocalDate endDate) throws MalformedURLException;
}
