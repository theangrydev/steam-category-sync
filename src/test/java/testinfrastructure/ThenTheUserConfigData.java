package testinfrastructure;

import com.slugsource.vdf.lib.InvalidFileException;
import com.slugsource.vdf.lib.Node;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class ThenTheUserConfigData {

    private final File configFile;

    public ThenTheUserConfigData(File configFile) {
        this.configFile = configFile;
    }

    public void hasBackupCopyWithName(String backupFileName) {
        assertThat(configFile.toPath().resolveSibling(backupFileName)).exists();
    }

    private Node userRoamingConfigStore() {
        try {
            return Node.readFromFile(configFile);
        } catch (IOException | InvalidFileException e) {
            throw new RuntimeException(e);
        }
    }

    public void hasAllCategoriesCollapsed() {
        Node uiNavigationPanel = userRoamingConfigStore().getNode("SteamRootDialog").getNode("UINavigatorPanel");

        assertThatSectionIsCollapsed(uiNavigationPanel.getNode("GamesPage_List"));
        assertThatSectionIsCollapsed(uiNavigationPanel.getNode("ToolsPage"));
        assertThatSectionIsCollapsed(uiNavigationPanel.getNode("GamesPage_Details"));
    }

    private void assertThatSectionIsCollapsed(Node section) {
        Node gamesList = section.getNode("GamesList");
        for (Node category : gamesList.getChildren()) {
            if (category.getName().startsWith("U:")) {
                assertThat(category.getValue()).isEqualTo("1");
            }
        }
        for (Node collapser : gamesList.getNode("Collapsers").getChildren()) {
            assertThat(collapser.getValue()).isEqualTo("1");
        }
    }
}
