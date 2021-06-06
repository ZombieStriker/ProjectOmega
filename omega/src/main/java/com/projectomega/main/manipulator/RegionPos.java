package com.projectomega.main.manipulator;

import java.util.Objects;
import java.util.regex.Pattern;

public final class RegionPos {

    private static final Pattern BY_DOT = Pattern.compile("\\.");

    private final int xPos;
    private final int zPos;

    public RegionPos(int xPos, int zPos) {
        this.xPos = xPos;
        this.zPos = zPos;
    }

    private RegionPos(String[] mcaFileNameParts) {
        this(Integer.parseInt(mcaFileNameParts[1]), Integer.parseInt(mcaFileNameParts[2]));
    }

    public RegionPos(String mcaFileName) {
        this(BY_DOT.split(mcaFileName));
    }

    public int getXPos() {
        return xPos;
    }

    public int getZPos() {
        return zPos;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegionPos)) return false;
        RegionPos regionPos = (RegionPos) o;
        return xPos == regionPos.xPos && zPos == regionPos.zPos;
    }

    @Override public int hashCode() {
        return Objects.hash(xPos, zPos);
    }

    @Override public String toString() {
        return String.format("RegionPos{xPos=%d, zPos=%d}", xPos, zPos);
    }
}
