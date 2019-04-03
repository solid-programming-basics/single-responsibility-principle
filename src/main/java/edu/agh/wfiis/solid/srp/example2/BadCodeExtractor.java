package edu.agh.wfiis.solid.srp.example2;
import java.io.File;

public class BadCodeExtractor {

    public String extractBadCodeFromFile(File file) throws IOException {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "bad code from file";
    }

    public String extractBadCodeFromClassPath(ClassLoader classLoader) {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "bad code from class loader";
    }

}