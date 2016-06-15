package io.github.theangrydev.steamcategorysync.shared;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BackupFile {
    public static void backup(File file) {
        try {
            Path backupFile = determineBackupFileName(file);
            Files.copy(file.toPath(), backupFile);
        } catch (IOException e) {
            throw new IllegalStateException("Could not backup file: " + file, e);
        }
    }

    private static Path determineBackupFileName(File configFile) {
        Path backupFile = backupFile(configFile, ".bak");
        int i = 0;
        while (backupFile.toFile().exists()) {
            backupFile = backupFile(configFile, ".bak." + i);
            i++;
        }
        return backupFile;
    }

    private static Path backupFile(File configFile, String suffix) {
        return configFile.toPath().getParent().resolve(configFile.getName() + suffix);
    }
}
