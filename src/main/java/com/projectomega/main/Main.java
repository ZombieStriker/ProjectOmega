package com.projectomega.main;

import com.projectomega.main.game.Core;

import java.io.IOException;

public class Main {

    private static ServerThread serverThread;
    private static Core core;

    public static void main(String... args){
        core = new Core();
        core.start();
        try {
            serverThread = new ServerThread();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerThread getServerThread(){
        return serverThread;
    }
    public static Core getCore(){
        return core;
    }
}
