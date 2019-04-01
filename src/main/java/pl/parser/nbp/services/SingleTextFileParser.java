package pl.parser.nbp.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SingleTextFileParser implements TextFileParser {

    @Override
    public List<String> getFilenames(URL source) {
        List<String> filenames = new ArrayList<>();

        try {
            BufferedReader sourceReader = new BufferedReader(new InputStreamReader(source.openStream()));
            String line;

            while ((line = sourceReader.readLine()) != null) {
                if (line.charAt(0) == 'c') {
                    filenames.add(line);
                }
            }
            sourceReader.close();
        } catch (IOException ex) {

        }

        return filenames;
    }
}
