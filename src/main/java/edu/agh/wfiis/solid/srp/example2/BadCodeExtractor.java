package edu.agh.wfiis.solid.srp.example2;
import java.io.File;

public class BadCodeExtractor {

    public String extractFromFile(File file) {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "bad code from file";
    }

    public String extractFromClassPath(ClassLoader classLoader) {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "bad code from class loader";
    }
}