package com.projectomega.main.game;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A 3-dimensional, immutable position inside a {@link World}.
 */
public class Location {

    private final double x, y, z;
    private final float yaw, pitch;
    private transient final World world;

    private Location(double x, double y, double z, float yaw, float pitch, @NotNull World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = Objects.requireNonNull(world, "world");
    }

    private Location(double x, double y, double z, @NotNull World world) {
        this(x, y, z, 0, 0, world);
    }

    @NotNull
    public Location withX(double x) {
        return at(x, y, z, yaw, pitch, world);
    }

    @NotNull
    public Location withY(double y) {
        return at(x, y, z, yaw, pitch, world);
    }

    @NotNull
    public Location withZ(double z) {
        return at(x, y, z, yaw, pitch, world);
    }

    public Location add(double dX, double dY, double dZ) {
        return at(x + dX, y + dY, z + dZ, yaw, pitch, world);
    }

    public Location subtract(double dX, double dY, double dZ) {
        return at(x - dX, y - dY, z - dZ, yaw, pitch, world);
    }

    @NotNull
    public Location withWorld(World world) {
        return at(x, y, z, yaw, pitch, world);
    }

    @NotNull
    public Block asBlock() {
        return new Block(this);
    }

    @NotNull
    public Location centered() {
        return at(center(x), center(y), center(z), yaw, pitch, world);
    }

    @NotNull
    public Location block() {
        return at(block(x), block(y), block(z), yaw, pitch, world);
    }

    public static double center(double v) {
        return block(v) + 0.5;
    }

    public static int block(double v) {
        return (int) Math.floor(v);
    }

    public static Location at(@NotNull Location location) {
        return at(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), location.getWorld());
    }

    public static Location at(@NotNull Block block) {
        return at(block.getLocation());
    }

    public static Location at(double x, double y, double z, World world) {
        return new Location(x, y, z, world);
    }

    public static Location at(double x, double y, double z, float yaw, float pitch, World world) {
        return new Location(x, y, z, yaw, pitch, world);
    }

    @Override public String toString() {
        return "Location{" + "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                ", world=" + world +
                '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Double.compare(location.x, x) == 0 &&
                Double.compare(location.y, y) == 0 &&
                Double.compare(location.z, z) == 0 &&
                Float.compare(location.yaw, yaw) == 0 &&
                Float.compare(location.pitch, pitch) == 0 &&
                Objects.equals(world, location.world);
    }

    @Override public int hashCode() {
        return Objects.hash(x, y, z, yaw, pitch, world);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public int getBlockX() {
        return block(x);
    }

    public int getBlockY() {
        return block(y);
    }

    public int getBlockZ() {
        return block(z);
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public World getWorld() {
        return world;
    }

}