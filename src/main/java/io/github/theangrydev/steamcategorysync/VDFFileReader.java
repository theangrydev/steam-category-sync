package io.github.theangrydev.steamcategorysync;

import com.slugsource.vdf.lib.InvalidFileException;
import com.slugsource.vdf.lib.Node;

import java.io.File;
import java.io.IOException;

public class VDFFileReader {
    public static Node readVDFFile(File file) {
        try {
            return Node.readFromFile(file);
        } catch (InvalidFileException | IOException e) {
            throw new RuntimeException("Could not read file: " + file, e);
        }
    }
}
