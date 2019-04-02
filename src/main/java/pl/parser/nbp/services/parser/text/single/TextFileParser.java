package pl.parser.nbp.services.parser.text.single;

import java.util.List;

public interface TextFileParser {

    List<String> getFilenames(String filename, int minDateFilePattern, int maxDateFilePattern);
}
