package com.projectomega.main.game.player;

public class GameProfile {

    private String name;
    private String value;
    private boolean signed = false;

    public GameProfile(String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isSigned() {
        return signed;
    }
}
