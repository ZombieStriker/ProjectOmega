package com.projectomega.main.game.block;

import com.projectomega.main.game.Material;

import java.util.HashMap;

public class BlockData {

    private int id;
    private Material materialType;
    private HashMap<BlockDataTag, Object> blockDataTags = new HashMap<>();

    public BlockData(int id, Material type){
        this.id = id;
        this.materialType = type;
    }

    public void setBlockDataTag(BlockDataTag tag, Object value){
        blockDataTags.put(tag,value);
    }
    public Object getBlockDataTagValue(BlockDataTag tag){
        return blockDataTags.get(tag);
    }
}
