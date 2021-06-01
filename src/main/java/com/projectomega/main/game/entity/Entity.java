package com.projectomega.main.game.entity;

import com.projectomega.main.packets.PacketUtil;

public class Entity {

    private EntityType type;
    private int entityID;

    public Entity(int entityID, EntityType type){
        this.type = type;
        this.entityID = entityID;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public int getEntityID() {
        return entityID;
    }

    public EntityType getType() {
        return type;
    }
}
