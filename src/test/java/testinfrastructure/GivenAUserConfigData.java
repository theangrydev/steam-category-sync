package testinfrastructure;

import com.slugsource.vdf.lib.Node;
import io.github.theangrydev.yatspecfluent.Given;

import java.io.File;
import java.io.IOException;

public class GivenAUserConfigData implements Given {

    private final TestInfrastructure testInfrastructure;

    private String fileName;

    private Node userConfigData;
    private final Node gamesList;
    private final Node collapsers;

    public GivenAUserConfigData(TestInfrastructure testInfrastructure) {
        this.testInfrastructure = testInfrastructure;

        userConfigData = new Node("UserConfigData");
        Node steamRootDialog = new Node("SteamRootDialog");
        Node uiNavigationPanel = new Node("UINavigatorPanel");
        Node gamesPageList = new Node("GamesPage_List");
        Node toolsPage = new Node("ToolsPage");
        Node gamesPageDetails = new Node("GamesPage_Details");
        collapsers = new Node("Collapsers");
        gamesList = new Node("GamesList");

        userConfigData.addNode(steamRootDialog);
        steamRootDialog.addNode(uiNavigationPanel);
        uiNavigationPanel.addNode(gamesPageList);
        uiNavigationPanel.addNode(toolsPage);
        uiNavigationPanel.addNode(gamesPageDetails);
        gamesPageList.addNode(gamesList);
        toolsPage.addNode(gamesList);
        gamesPageDetails.addNode(gamesList);
        gamesList.addNode(collapsers);
    }

    public GivenAUserConfigData inFile(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public GivenAUserConfigData withCategory(String appName, String collapsed) {
        gamesList.addNode(new Node(appName + "_collapsed", collapsed));
        collapsers.addNode(new Node("U:" + appName, collapsed));
        return this;
    }

    @Override
    public void prime() {
        testInfrastructure.addToCapturedInputsAndOutputs("Old " + fileName, userConfigData.toVdf());
        File configFile = testInfrastructure.createFile(fileName);
        try {
            userConfigData.writeToFile(configFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not write to " + configFile, e);
        }
    }
}
