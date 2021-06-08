package com.projectomega.main.packets.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.game.entity.Entity;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.Angle;
import com.projectomega.main.packets.datatype.VarInt;

public class PacketTypeSpawnEntity extends OutboundPacket {


    public PacketTypeSpawnEntity(Player player, Entity entity) {
        super(PacketType.SPAWN_ENTITY, player.getProtocolVersion(), new VarInt(entity.getEntityID()), entity.getUniqueID(),
                entity.getType(), entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(),
                new Angle(entity.getLocation().getYaw()), new Angle(entity.getLocation().getPitch()), 0, (short) 0, (short) 0, (short) 0);
    }
}
