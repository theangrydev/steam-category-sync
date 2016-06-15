package testinfrastructure;

import com.slugsource.vdf.lib.Node;
import io.github.theangrydev.yatspecfluent.Given;

import java.io.File;
import java.io.IOException;

public class GivenAnExistingConfiguration implements Given {

    private final TestInfrastructure testInfrastructure;

    private String fileName;

    private Node userRoamingConfigStore;
    private Node apps;

    public GivenAnExistingConfiguration(TestInfrastructure testInfrastructure) {
        this.testInfrastructure = testInfrastructure;

        userRoamingConfigStore = new Node("UserRoamingConfigStore");
        Node software = new Node("Software");
        Node valve = new Node("Valve");
        Node steam = new Node("Steam");
        apps = new Node("apps");

        userRoamingConfigStore.addNode(software);
        software.addNode(valve);
        valve.addNode(steam);
        steam.addNode(apps);
    }

    public GivenAnExistingConfiguration inFile(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public GivenAnExistingConfiguration withApp(String appId, String... existingTags) {
        Node app = new Node(appId);
        Node tags = new Node("tags");
        app.addNode(tags);

        int order = 0;
        for (String existingTag : existingTags) {
            tags.addNode(new Node(String.valueOf(order), existingTag));
        }
        apps.addNode(app);
        return this;
    }

    @Override
    public void prime() {
        File configFile = testInfrastructure.createFile(fileName);
        try {
            userRoamingConfigStore.writeToFile(configFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not write to " + configFile, e);
        }
    }
}
