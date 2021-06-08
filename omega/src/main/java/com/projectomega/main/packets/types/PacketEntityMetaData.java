package com.projectomega.main.packets.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.game.entity.Entity;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.MetaData;
import com.projectomega.main.packets.datatype.VarInt;

public class PacketEntityMetaData extends OutboundPacket {
    public PacketEntityMetaData(Player player, Entity entity, MetaData metaData) {
        super(PacketType.ENTITY_METADATA,player.getProtocolVersion(), new VarInt(entity.getEntityID()), metaData);
    }
}
