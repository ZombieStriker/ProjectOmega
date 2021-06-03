package com.projectomega.main;

import com.projectomega.main.debugging.DebuggingUtil;
import com.projectomega.main.game.Omega;
import com.projectomega.main.plugin.loader.PluginClassLoader;
import com.projectomega.main.plugin.loader.PluginManager;
import com.projectomega.main.plugin.loader.PluginProvider;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static ServerThread serverThread;

    public static void main(String... args) {
        Omega.init();
        try {
            serverThread = new ServerThread();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("/stop")) {
                serverThread.stop();
                return;
            }
            if (message.startsWith("/")) {
                Omega.getConsoleSender().issueCommand(message);
            } else {
                Omega.getConsoleSender().chat(message);
            }
        }
    }

    public static ServerThread getServerThread() {
        return serverThread;
    }
}
