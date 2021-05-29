package com.projectomega.main.packets.datatype;

public class VarInt {
    int integer;
    public int getInteger(){
        return integer;
    }
    public VarInt(int i){
        this.integer = i;
    }
}
