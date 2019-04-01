package pl.parser.nbp.services.parser.text.single;

import java.net.URL;
import java.util.List;

public interface TextFileParser {

    List<String> getFilenames(URL source);
}
