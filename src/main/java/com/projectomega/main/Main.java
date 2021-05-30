package com.projectomega.main;

import com.projectomega.main.game.Omega;

import java.io.IOException;

public class Main {

    private static ServerThread serverThread;

    public static void main(String... args){
        Omega.init();
        try {
            serverThread = new ServerThread();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerThread getServerThread(){
        return serverThread;
    }
}
