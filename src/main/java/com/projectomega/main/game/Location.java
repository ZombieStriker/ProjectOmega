package com.projectomega.main.game;

public class Location extends Vector{
    private World world;

    public Location(World world, double x, double y, double z) {
        super(x, y, z);
    this.world =world;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public Location add(double x, double y, double z) {
        return (Location) super.add(x, y, z);
    }
    @Override
    public Location subtract(double x, double y, double z) {
        return (Location) super.subtract(x, y, z);
    }

    public int getBlockX() {
        return (int) getX();
    }
    public int getBlockY() {
        return (int) getY();
    }
    public int getBlockZ() {
        return (int) getZ();
    }
}
