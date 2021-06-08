package com.projectomega.main.packets.types;

import com.google.errorprone.annotations.Var;
import com.projectomega.main.game.Player;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;

public class PacketUpdateHealth extends OutboundPacket {
    public PacketUpdateHealth(Player player) {
        super(PacketType.UPDATE_HEALTH,player.getProtocolVersion(),player.getHealth(), new VarInt(player.getFood()),player.getSaturation());
    }
}
