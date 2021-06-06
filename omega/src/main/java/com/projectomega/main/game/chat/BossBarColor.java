package com.projectomega.main.game.chat;

public enum BossBarColor {
    PINK(0),
    BLUE(1),
    RED(2),
    GREEN(3),
    YELLOW(4),
    PURPLE(5),
    WHITE(6)
    ;

    private int id;

    BossBarColor(int i){
        this.id = i;
    }
    public int getId(){
        return id;
    }
}
