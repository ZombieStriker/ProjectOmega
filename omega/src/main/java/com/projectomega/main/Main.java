package com.projectomega.main;

import com.projectomega.main.game.Omega;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Scanner;

public class Main {

    private static ServerThread serverThread;
    private static File runDirectory;

    Main(@NotNull File runDirectory) throws Throwable {
        Main.runDirectory = runDirectory;
        Omega.init();
        serverThread = new ServerThread();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("/stop")) {
                serverThread.stop();
                return;
            }
            if (message.startsWith("/")) {
                Omega.getConsoleSender().issueCommand(message.substring(1));
            } else {
                Omega.getConsoleSender().chat(message);
            }
        }

    }

    public static void main(String... args) throws Throwable {
        File directory = new File("run").getAbsoluteFile();
        new Main(directory);
    }

    public static ServerThread getServerThread() {
        return serverThread;
    }

    public static File getRunDirectory() {
        return runDirectory;
    }
}
