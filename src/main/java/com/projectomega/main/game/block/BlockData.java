package com.projectomega.main.game.block;

import com.projectomega.main.game.Material;

import java.util.HashMap;

public class BlockData {

    private int id;
    private int dataValues=1;
    private Material materialType;

    public BlockData(int id, Material type){
        this.id = id;
        this.materialType = type;
    }
    public BlockData(int id, Material type, BlockDataTag... tags){
        this.id = id;
        this.materialType = type;
        for(BlockDataTag tag : tags){
            dataValues*=tag.getStateCount();
        }
    }

    public int getId() {
        return id;
    }

    public Material getMaterial() {
        return materialType;
    }

    public int getDataValues() {
        return dataValues;
    }
}
