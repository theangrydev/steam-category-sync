package io.github.theangrydev.steamcategorysync;

import com.slugsource.vdf.lib.Node;

import java.io.File;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.theangrydev.steamcategorysync.BackupFile.backup;
import static io.github.theangrydev.steamcategorysync.IsProcessRunning.isProcessRunning;

public class CategoryCollapseTool {

    private static final String COLLAPSED_VALUE = "1";

    public static void main(String[] arguments) {
        if (arguments.length != 1) {
            throw new IllegalArgumentException("First argument should be the 'Steam\\config\\DialogConfig.vdf' file");
        }
        if (isProcessRunning("steam.exe")) {
            throw new IllegalStateException("Close the Steam client before running this!");
        }
        File configFile = Paths.get(arguments[0]).toFile();
        if (!configFile.exists()) {
            throw new IllegalArgumentException("Config file does not exist: " + configFile);
        }
        backup(configFile);

        Node userConfigData = VDFFileReader.readVDFFile(configFile);
        Node navigationPanel = userConfigData.getNode("SteamRootDialog").getNode("UINavigatorPanel");
        collapse(navigationPanel.getNode("GamesPage_List").getNode("GamesList"));
        collapse(navigationPanel.getNode("ToolsPage").getNode("GamesList"));
        collapse(navigationPanel.getNode("GamesPage_Details").getNode("GamesList"));

        VDFFileWriter.writeToFile(configFile, userConfigData);
    }

    private static void collapse(Node nodeWithCollapsers) {
        Node collapsers = nodeWithCollapsers.getNode("Collapsers");
        for (Node collapser : collapsers.getChildren()) {
            collapser.setValue(COLLAPSED_VALUE);
        }
        Set<String> sections = collapsers.getChildren().stream()
                .filter(x -> x.getName().startsWith("U:"))
                .map(x -> x.getName().replaceFirst("U:", "") + "_collapsed")
                .collect(Collectors.toSet());

        nodeWithCollapsers.getChildren().stream()
                .filter(x -> sections.contains(x.getName()))
                .forEach(node -> node.setValue(COLLAPSED_VALUE));
    }
}
