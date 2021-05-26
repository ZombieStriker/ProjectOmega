package com.projectomega.main;

import com.projectomega.main.game.Core;

import java.io.IOException;

public class Main {

    public static void main(String... args){
        Core core = new Core();
        core.start();
        try {
            new ServerThread();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
