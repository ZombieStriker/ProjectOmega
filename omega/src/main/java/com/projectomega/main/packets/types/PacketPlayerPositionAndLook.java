package com.projectomega.main.packets.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;

public class PacketPlayerPositionAndLook extends OutboundPacket {
    public PacketPlayerPositionAndLook(Player player) {
        super(PacketType.PLAYER_POSITION_AND_LOOK,player.getProtocolVersion(),
                player.getEntity().getLocation().getX(),
                player.getEntity().getLocation().getY(),
                player.getEntity().getLocation().getZ(),
                player.getEntity().getLocation().getYaw(),
                player.getEntity().getLocation().getPitch(),
                (byte) 0, new VarInt(1));
    }
}
