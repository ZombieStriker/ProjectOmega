package com.projectomega.main.game.entity;

import com.projectomega.main.game.Location;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.game.chat.TextMessage;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.MetaData;
import com.projectomega.main.packets.datatype.VarInt;

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
        OutboundPacket teleport = new OutboundPacket(PacketType.ENTITY_TELEPORT, new VarInt(entityID), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), true);
        for (Player player : Omega.getPlayers()) {
            if (player.getWorld().equals(location.getWorld())) {
                player.sendPacket(teleport);
            } else {

            }
        }

    }

    public Object getUniqueID() {
        return uuid;
    }

    public void setCustomNameVisable(boolean b) {
        this.customnamevisable = b;
        OutboundPacket customnameVisable = new OutboundPacket(PacketType.ENTITY_METADATA, new VarInt(getEntityID()), new MetaData().add(3, b));
        for (Player player : Omega.getPlayers()) {
            player.sendPacket(customnameVisable);
        }
    }

    public void setCustomName(String customname) {
        this.customname = customname;
        OutboundPacket customnamepacket = new OutboundPacket(PacketType.ENTITY_METADATA, new VarInt(getEntityID()), new MetaData().add(2, TextMessage.text(customname)));
        for (Player player : Omega.getPlayers()) {
            player.sendPacket(customnamepacket);
        }
    }
}
