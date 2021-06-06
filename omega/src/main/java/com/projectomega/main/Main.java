package com.projectomega.main;

import com.projectomega.main.game.Omega;

import java.util.Scanner;

public class Main {

    private static ServerThread serverThread;

    public static void main(String... args) throws Throwable {
        System.out.println("Hello");
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
                Omega.getConsoleSender().issueCommand(message);
            } else {
                Omega.getConsoleSender().chat(message);
            }
        }
    }

}
