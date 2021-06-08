package com.projectomega.main.packets.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.game.entity.Entity;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;

public class PacketEntityTeleport extends OutboundPacket {
    public PacketEntityTeleport(Player player, Entity entity) {
        super(PacketType.ENTITY_TELEPORT, player.getProtocolVersion(), new VarInt(entity.getEntityID()), entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch(), true);
    }
}
