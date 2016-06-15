package testinfrastructure;

import com.slugsource.vdf.lib.InvalidFileException;
import com.slugsource.vdf.lib.Node;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenTheConfigFile {

    private final File configFile;

    public ThenTheConfigFile(File configFile) {
        this.configFile = configFile;
    }

    public void hasBackupCopyWithName(String backupFileName) {
        assertThat(configFile.toPath().resolveSibling(backupFileName)).exists();
    }

    public void hasAppWithTags(String appId, String... tags) {
        Node apps = userRoamingConfigStore().getNode("Software").getNode("Valve").getNode("Steam").getNode("apps");
        assertThat(apps.getChildren()).has()

    }

    private Node userRoamingConfigStore() {
        try {
            return Node.readFromFile(configFile);
        } catch (IOException | InvalidFileException e) {
            throw new RuntimeException(e);
        }
    }
}
