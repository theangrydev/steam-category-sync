package io.github.theangrydev.steamcategorysync.shared;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class BackupFileTest {

    private static final byte[] FILE_CONTENT = new byte[]{0xA};

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void firstBackupFileJustAppendsDotBak() throws IOException {
        File file = fileToBackUp();

        BackupFile.backup(file);

        assertThat(backupFiles()).containsExactly(file.getName() + ".bak");
        assertThat(file).hasBinaryContent(FILE_CONTENT);
    }

    @Test
    public void subsequentBackupFilesAppendsDotBakAndAUniqueNumber() throws IOException {
        File file = fileToBackUp();

        BackupFile.backup(file);
        BackupFile.backup(file);
        assertThat(backupFiles()).containsExactly(file.getName() + ".bak", file.getName() + ".bak.0");

        BackupFile.backup(file);
        assertThat(backupFiles()).containsExactly(file.getName() + ".bak", file.getName() + ".bak.0", file.getName() + ".bak.1");
        assertThat(file).hasBinaryContent(FILE_CONTENT);
    }

    private List<String> backupFiles() throws IOException {
        return Files.list(temporaryFolder.getRoot().toPath())
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(x -> x.contains(".bak"))
                .collect(toList());
    }

    private File fileToBackUp() throws IOException {
        File file = temporaryFolder.newFile();
        Files.write(file.toPath(), FILE_CONTENT);
        return file;
    }
}