package com.projectomega.main.game.sound;

public enum Sound {
    EERIE_NOISE(0),
    BLOCK_BREAKING(10),
    ANVIL_LAND(11),
    BLOCK_PLACED(12),
    SOUND_TEST(10),
    ;

    private int id;

    Sound(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
