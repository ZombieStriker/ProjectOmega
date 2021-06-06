package com.projectomega.main.game;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents an immutable 3-D vector
 */
public final class Vector {

    private final double x;
    private final double y;
    private final double z;

    private Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    @NotNull
    public Vector withX(double x) {
        return at(x, y, z);
    }

    @NotNull
    public Vector withY(double y) {
        return at(x, y, z);
    }

    @NotNull
    public Vector withZ(double z) {
        return at(x, y, z);
    }

    @NotNull
    public Vector add(double dX, double dY, double dZ) {
        return at(x + dX, y + dY, z + dZ);
    }

    @NotNull
    public Vector subtract(double dX, double dY, double dZ) {
        return at(x - dX, y - dY, z - dZ);
    }

    @NotNull
    public Vector rotateAroundX(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        double y = angleCos * getY() - angleSin * getZ();
        double z = angleSin * getY() + angleCos * getZ();
        return at(x, y, z);
    }

    @NotNull
    public Vector rotateAroundY(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        double x = angleCos * getX() + angleSin * getZ();
        double z = -angleSin * getX() + angleCos * getZ();
        return at(x, y, z);
    }

    @NotNull
    public Vector rotateAroundZ(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        double x = angleCos * getX() - angleSin * getY();
        double y = angleSin * getX() + angleCos * getY();
        return at(x, y, z);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector)) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 && Double.compare(vector.y, y) == 0 && Double.compare(vector.z, z) == 0;
    }

    @Override public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public static Vector at(double x, double y, double z) {
        return new Vector(x, y, z);
    }

}
