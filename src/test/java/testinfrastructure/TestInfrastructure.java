package testinfrastructure;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.RemoteMappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class TestInfrastructure {

    private Path configFolder;
    private WireMock wireMock;

    public void setUp(File configFolder) {
        this.configFolder = configFolder.toPath();

        WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(1235));
        wireMockServer.start();
        wireMock = new WireMock(wireMockServer);
    }

    public void tearDown() {
        wireMock.shutdown();
    }

    public void givenThat(RemoteMappingBuilder remoteMappingBuilder) {
        wireMock.register(remoteMappingBuilder);
    }

    public File fileHandle(String fileName) {
        return configFolder.resolve(fileName).toFile();
    }

    public File createFile(String fileName) {
        File file = fileHandle(fileName);
        boolean created = createNewFile(fileName, file);
        if (!created) {
            throw new IllegalStateException("Could not create file: " + fileName);
        }
        return file;
    }

    private boolean createNewFile(String fileName, File file) {
        try {
            return file.createNewFile();
        } catch (IOException e) {
            throw new IllegalStateException("Could not create file: " + fileName, e);
        }
    }
}
