package com.projectomega.main.game.chat;

public enum BossBarDivisions {

    NONE(0),
    SIX_NOTCHES(1),
    TEN_NOTCHES(2),
    TWELVE_NOTCHES(3),
    TWENTY_NOTCHES(4)
    ;

    int id;

    BossBarDivisions(int i){
        this.id = i;
    }

    public int getId() {
        return id;
    }
}
