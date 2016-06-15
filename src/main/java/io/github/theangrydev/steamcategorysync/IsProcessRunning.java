package io.github.theangrydev.steamcategorysync;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class IsProcessRunning {
    // http://stackoverflow.com/a/19005828/3764804
    public static boolean isProcessRunning(String processName) {
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
