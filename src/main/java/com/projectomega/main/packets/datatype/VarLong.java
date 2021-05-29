package com.projectomega.main.packets.datatype;

public class VarLong {
    long integer;
    public long getLong(){
        return integer;
    }
    public VarLong(long i){
        this.integer = i;
    }
}
