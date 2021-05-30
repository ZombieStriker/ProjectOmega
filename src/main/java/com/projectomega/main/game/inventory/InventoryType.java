package com.projectomega.main.game.inventory;

public enum InventoryType {

    PLAYER("INVALID",-1,46),
    GENERIC_9x1("minecraft:generic_9x1",0,9)
    ;

    private String type;
    private int id;
    private int slots;

    InventoryType(String type, int id, int slot){
        this.type = type;
        this.id = id;
        this.slots = slot;
    }

    public int getId() {
        return id;
    }

    public int getSlots() {
        return slots;
    }
}
