package io.github.theangrydev.steamcategorysync;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Tool {

    public static void main(String[] arguments) throws IOException, InterruptedException {
        if (isProcessRunning("steam.exe")) {
            throw new IllegalStateException("Close the Steam client before running this!");
        }
    }

    // http://stackoverflow.com/a/19005828/3764804
    private static boolean isProcessRunning(String processName) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
        Process process = processBuilder.start();
        String tasksList = toString(process.getInputStream()).toLowerCase();
        return tasksList.contains(processName.toLowerCase());
    }

    // http://stackoverflow.com/a/5445161/3764804
    private static String toString(InputStream inputStream) {
        try (Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A")) {
            return scanner.hasNext() ? scanner.next() : "";
        }
    }
}
