package io.github.theangrydev.steamcategorysync;

import com.slugsource.vdf.lib.InvalidFileException;
import com.slugsource.vdf.lib.Node;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class Tool {

    public static void main(String[] arguments) {
        if (arguments.length != 1) {
            throw new IllegalArgumentException("First argument should be the file to modify");
        }
        if (isProcessRunning("steam.exe")) {
            throw new IllegalStateException("Close the Steam client before running this!");
        }
        File configFile = Paths.get(arguments[0]).toFile();
        if (!configFile.exists()) {
            throw new IllegalArgumentException("Config file does not exist: " + configFile);
        }
        backup(configFile);

        Node userRoamingConfigStore = userRoamingConfigStore(configFile);
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
        writeToFile(configFile, userRoamingConfigStore);
    }

    private static void writeToFile(File configFile, Node userRoamingConfigStore) {
        try {
            userRoamingConfigStore.writeToFile(configFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not write to file: " + configFile);
        }
    }

    private static Integer lastOrder(Node tagsNode) {
        return tagsNode.getChildren().stream()
                .reduce((first, second) -> second)
                .map(Node::getName)
                .map(Integer::parseInt)
                .orElse(-1);
    }

    private static void backup(File configFile) {
        try {
            Path backupFile = configFile.toPath().getParent().resolve(configFile.getName() + ".bak");
            Files.copy(configFile.toPath(), backupFile);
        } catch (IOException e) {
            throw new IllegalStateException("Could not backup file: " + configFile, e);
        }
    }

    private static Set<String> popularTags(String appId) {
        Document document = documentAtUrl(System.getProperty("steam.url", "http://store.steampowered.com") + "/app/" + appId);
        return document.select(".app_tag:not(.add_button)").stream()
                .map(Element::text)
                .map(String::trim)
                .collect(toSet());
    }

    private static Document documentAtUrl(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException("Could not fetch page: " + url, e);
        }
    }

    private static Node userRoamingConfigStore(File file) {
        try {
            return Node.readFromFile(file);
        } catch (InvalidFileException | IOException e) {
            throw new RuntimeException("Could not read file: " + file, e);
        }
    }

    // http://stackoverflow.com/a/19005828/3764804
    private static boolean isProcessRunning(String processName) {
        ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
        Process process = startProcess(processBuilder);
        String tasksList = toString(process.getInputStream()).toLowerCase();
        return tasksList.contains(processName.toLowerCase());
    }

    private static Process startProcess(ProcessBuilder processBuilder) {
        try {
            return processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException("Could not start process", e);
        }
    }

    // http://stackoverflow.com/a/5445161/3764804
    private static String toString(InputStream inputStream) {
        try (Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A")) {
            return scanner.hasNext() ? scanner.next() : "";
        }
    }
}
