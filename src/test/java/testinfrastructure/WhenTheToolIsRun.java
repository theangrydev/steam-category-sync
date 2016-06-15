package testinfrastructure;

import io.github.theangrydev.steamcategorysync.PopularTagsTool;
import io.github.theangrydev.yatspecfluent.When;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class WhenTheToolIsRun implements When<File, File> {

    private final TestInfrastructure testInfrastructure;

    private String fileName;

    public WhenTheToolIsRun(TestInfrastructure testInfrastructure) {
        this.testInfrastructure = testInfrastructure;
    }

    public WhenTheToolIsRun againstConfigFile(String fileName) {
        this.fileName = fileName;
        return this;
    }

    @Override
    public File request() {
        return testInfrastructure.fileHandle(fileName);
    }

    @Override
    public File response(File configFile) {
        PopularTagsTool.main(new String[]{configFile.getAbsolutePath()});
        testInfrastructure.addToCapturedInputsAndOutputs("New " + configFile.getName(), readFile(configFile));
        return configFile;
    }

    private String readFile(File configFile) {
        try {
            return new String(Files.readAllBytes(configFile.toPath()));
        } catch (IOException e) {
            throw new IllegalStateException("Could not read: " + configFile);
        }
    }
}
