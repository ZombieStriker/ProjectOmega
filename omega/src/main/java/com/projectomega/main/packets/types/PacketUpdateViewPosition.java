package com.projectomega.main.packets.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;

public class PacketUpdateViewPosition extends OutboundPacket {
    public PacketUpdateViewPosition(Player player) {
        super(PacketType.UPDATE_VIEW_POSITION, player.getProtocolVersion(), new VarInt(player.getLocation().getChunk().getX()),new VarInt(player.getLocation().getChunk().getZ()));
    }
}
