package io.github.theangrydev.steamcategorysync;

import com.slugsource.vdf.lib.Node;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class PopularTagsTool {

    public static void main(String[] arguments) {
        if (arguments.length != 1) {
            throw new IllegalArgumentException("First argument should be the sharedconfig.vdf file");
        }
        if (IsProcessRunning.isProcessRunning("steam.exe")) {
            throw new IllegalStateException("Close the Steam client before running this!");
        }
        File configFile = Paths.get(arguments[0]).toFile();
        if (!configFile.exists()) {
            throw new IllegalArgumentException("Config file does not exist: " + configFile);
        }
        BackupFile.backup(configFile);

        Node userRoamingConfigStore = VDFFileReader.readVDFFile(configFile);
        Node apps = userRoamingConfigStore.getNode("Software").getNode("Valve").getNode("Steam").getNode("apps");
        for (Node app : apps.getChildren()) {
            String appId = app.getName();
            Set<String> popularTags = popularTags(appId);
            if (popularTags.isEmpty()) {
                continue;
            }
            Node tagsNode = app.getNode("tags");
            if (tagsNode == null) {
                tagsNode = new Node("tags");
                app.addNode(tagsNode);
            }
            Set<String> existingTags = tagsNode.getChildren().stream().map(Node::getValue).collect(Collectors.toSet());
            Set<String> tagsToAdd = new HashSet<>(popularTags);
            tagsToAdd.removeAll(existingTags);

            int order = lastOrder(tagsNode) + 1;
            for (String tagToAdd : tagsToAdd) {
                tagsNode.addNode(new Node(String.valueOf(order++), tagToAdd));
            }
        }
        VDFFileWriter.writeToFile(configFile, userRoamingConfigStore);
    }

    private static Integer lastOrder(Node tagsNode) {
        return tagsNode.getChildren().stream()
                .reduce((first, second) -> second)
                .map(Node::getName)
                .map(Integer::parseInt)
                .orElse(-1);
    }

    private static Set<String> popularTags(String appId) {
        System.out.println("Fetching tags for app id " + appId);
        Document document = documentAtUrl(System.getProperty("steam.url", "http://store.steampowered.com") + "/app/" + appId);
        return document.select(".app_tag:not(.add_button)").stream()
                .map(Element::text)
                .map(String::trim)
                .collect(toSet());
    }

    private static Document documentAtUrl(String url) {
        try {
            return Jsoup.connect(url).timeout(10_000).get();
        } catch (IOException e) {
            throw new RuntimeException("Could not fetch page: " + url, e);
        }
    }

}
