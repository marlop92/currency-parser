package pl.parser.nbp.services.parser.text;

import java.time.LocalDate;
import java.util.List;

public interface NbpTextFileParser {

    List<String> getFilenames(LocalDate beginDate, LocalDate endDate);
}
