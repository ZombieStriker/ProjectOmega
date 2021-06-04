package com.projectomega.main.packets.datatype;

import me.nullicorn.nedit.type.NBTCompound;

public class Slot {

    final boolean isItem = true;
    final short id;
    final byte amount;
    final short damage;
    final NBTCompound tag;

    public Slot(short id, byte amount, short damage, NBTCompound tag) {
        this.id = id;
        this.amount = amount;
        this.tag = tag;
        this.damage = damage;
    }

    public boolean isItem(){
        return isItem;
    }
    public short getId(){
        return id;
    }
    public byte getAmount(){
        return amount;
    }
    public NBTCompound getNBT(){
        return tag;
    }
}
