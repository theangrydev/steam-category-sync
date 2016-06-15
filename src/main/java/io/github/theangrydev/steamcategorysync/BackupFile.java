package io.github.theangrydev.steamcategorysync;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class BackupFile {
    public static void backup(File configFile) {
        try {
            Path backupFile = configFile.toPath().getParent().resolve(configFile.getName() + ".bak");
            Files.copy(configFile.toPath(), backupFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Could not backup file: " + configFile, e);
        }
    }
}
