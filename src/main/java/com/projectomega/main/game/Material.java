package com.projectomega.main.game;

public enum Material {

    AIR("AIR",0,true),
    STONE("STONE",1,true),
    UNKNOWN2("UNKNOWN",2,true),
    UNKNOWN3("UNKNOWN",3,true),
    UNKNOWN4("UNKNOWN",4,true),
    UNKNOWN5("UNKNOWN",5,true),
    UNKNOWN6("UNKNOWN",6,true),
    UNKNOWN7("UNKNOWN",7,true),
    UNKNOWN8("UNKNOWN",8,true),
    UNKNOWN9("UNKNOWN",9,true),
    UNKNOWN10("UNKNOWN",10,true),
    UNKNOWN11("UNKNOWN",11,true),
    UNKNOWN12("UNKNOWN",12,true),
    UNKNOWN13("UNKNOWN",13,true),
    UNKNOWN14("UNKNOWN",14,true),
    UNKNOWN15("UNKNOWN",15,true),
    UNKNOWN16("UNKNOWN",16,true),
    ;

    private String name;
    private boolean block;
    private int id;

    Material(String name, int id, boolean block){
        this.name = name;
        this.block = block;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isBlock() {
        return block;
    }

    public String getName() {
        return name;
    }

}
