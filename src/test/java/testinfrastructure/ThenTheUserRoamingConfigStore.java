package testinfrastructure;

import com.slugsource.vdf.lib.InvalidFileException;
import com.slugsource.vdf.lib.Node;
import org.assertj.core.api.Condition;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class ThenTheUserRoamingConfigStore {

    private final File configFile;

    public ThenTheUserRoamingConfigStore(File configFile) {
        this.configFile = configFile;
    }

    public void hasBackupCopyWithName(String backupFileName) {
        assertThat(configFile.toPath().resolveSibling(backupFileName)).exists();
    }

    public void hasAppWithTags(String appId, String... expectedTags) {
        Node apps = userRoamingConfigStore().getNode("Software").getNode("Valve").getNode("Steam").getNode("apps");
        List<Node> app = apps.getChildren().stream().filter(node -> node.getName().equals(appId)).collect(toList());
        assertThat(app).hasSize(1);
        List<String> actualTags = app.get(0).getNode("tags").getChildren().stream().map(Node::getValue).collect(toList());
        assertThat(actualTags).containsExactly(expectedTags);
    }

    private Node userRoamingConfigStore() {
        try {
            return Node.readFromFile(configFile);
        } catch (IOException | InvalidFileException e) {
            throw new RuntimeException(e);
        }
    }

    public void hasSequentialTagsStartingAtZero() {
        Node apps = userRoamingConfigStore().getNode("Software").getNode("Valve").getNode("Steam").getNode("apps");
        for (Node app : apps.getChildren()) {
            List<Node> tags = app.getNode("tags").getChildren();
            List<String> order = tags.stream().map(Node::getName).collect(toList());
            assertThat(order).isEqualTo(IntStream.range(0, tags.size()).mapToObj(String::valueOf).collect(toList()));
        }
    }
}
