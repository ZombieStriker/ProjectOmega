package com.projectomega.main.game.block;

public enum BlockDataTag {

    DIRECTIONAL(3),
    POWERED(16),
    NOTEBLOCK_TYPE(10),
    WALL_NORTH_TYPE(3),
    WALL_SOUTH_TYPE(3),
    WALL_EAST_TYPE(3),
    WALL_WEST_TYPE(3),
    WATERLOGGED(2),
    GRASS(2), SAPLING(2), WATER_FALLING(2), WATER_LEVEL(8), LEAVES_LEVEL(7),
    PERSISTANT(2), TRIGGERED(2), DIRECTION(6), NOTEBLOCK_TONE(24), BED_FACING(4), BED_OCCUPIED(2), BED_HEADFOOT(2), POWERED_RAIL_FACING(6),
    PISTON_EXTENDED(2), TWO_TALL(2), PISTON_HEAD_STICKY(2), TNT_UNSTABLE(2),
    WALL_TORCH(4);

    public int getStateCount() {
        return statecount;
    }

    private int statecount;

    BlockDataTag(int statecount) {
        this.statecount = statecount;
    }

    enum DirectionalTag {
        X_AXIS,
        Y_AXIS,
        Z_AXIS;
    }
}
