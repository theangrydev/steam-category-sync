package io.github.theangrydev.steamcategorysync;

import com.slugsource.vdf.lib.Node;

import java.io.File;
import java.io.IOException;

public class VDFFileWriter {
    public static void writeToFile(File configFile, Node userRoamingConfigStore) {
        try {
            userRoamingConfigStore.writeToFile(configFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not write to file: " + configFile);
        }
    }
}
