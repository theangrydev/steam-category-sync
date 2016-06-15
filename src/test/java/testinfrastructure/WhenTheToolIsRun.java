package testinfrastructure;

import io.github.theangrydev.steamcategorysync.Tool;
import io.github.theangrydev.yatspecfluent.When;

import java.io.File;

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
        Tool.main(new String[]{configFile.getAbsolutePath()});
        return configFile;
    }
}
