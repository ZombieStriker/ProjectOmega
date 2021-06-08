package com.projectomega.main.game.entity;

import com.projectomega.main.game.Location;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.game.chat.TextMessage;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.MetaData;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.packets.types.PacketEntityMetaData;
import com.projectomega.main.packets.types.PacketEntityTeleport;

import java.util.UUID;

public class Entity {

    private EntityType type;
    private Location location;
    private int entityID;
    private UUID uuid;
    private String customname;
    private boolean customnamevisable;

    public Entity(int entityID, Location location, EntityType type) {
        this.type = type;
        this.entityID = entityID;
        this.location = location;
        uuid = UUID.randomUUID();
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

    public Location getLocation() {
        return location;
    }

    public void teleport(Location location) {
        this.location = location;
        for (Player player : Omega.getPlayers()) {
            if (player.getWorld().equals(location.getWorld())) {
                if (player.getEntity() != this)
                    player.sendPacket(new PacketEntityTeleport(player, this));
            } else {

            }
        }

    }

    public Object getUniqueID() {
        return uuid;
    }

    public void setCustomNameVisable(boolean b) {
        this.customnamevisable = b;
        for (Player player : Omega.getPlayers()) {
            player.sendPacket(new PacketEntityMetaData(player,this,new MetaData().add(3,b)));
        }
    }

    public void setCustomName(String customname) {
        this.customname = customname;
        for (Player player : Omega.getPlayers()) {
            player.sendPacket(new PacketEntityMetaData(player,this, new MetaData().add(2, TextMessage.text(customname))));
        }
    }

    public void setLocation(Location at) {
        location = at;
    }
}
