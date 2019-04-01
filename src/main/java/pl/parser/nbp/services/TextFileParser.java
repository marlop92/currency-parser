package pl.parser.nbp.services;

import java.net.URL;
import java.util.List;

public interface TextFileParser {

    List<String> getFilenames(URL source);
}
