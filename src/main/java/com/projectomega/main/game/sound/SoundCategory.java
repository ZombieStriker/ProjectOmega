package com.projectomega.main.game.sound;

public enum SoundCategory {
    MASTER(0),
    ;

    private int id;

    SoundCategory(int i){
        this.id = i;
    }

    public int getId() {
        return id;
    }
}
