package com.projectomega;

import java.io.IOException;

public class Main {

    public static void main(String... args){
        try {
            new ServerThread();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
