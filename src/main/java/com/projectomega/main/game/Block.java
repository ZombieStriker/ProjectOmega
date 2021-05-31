package com.projectomega.main.game;

public class Block {

    private final Location location;
    private Material type;

    protected Block(Location location){
        this.location = location;
        this.type=Material.AIR;
    }
    protected Block(Location location,Material material){
    this.location=location;
    this.type=material;
    }

    public Location getLocation() {
        return location;
    }

    public Material getType() {
        return type;
    }

    public void setType(Material type) {
        this.type = type;
    }
}
