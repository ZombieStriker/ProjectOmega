package com.projectomega.main.packets.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;

public class PacketSetExperience extends OutboundPacket {
    public PacketSetExperience(Player player, float barLevel, int level, int totalXP) {
        super(PacketType.SET_EXPERIENCE,player.getProtocolVersion(),barLevel, new VarInt(level), new VarInt(totalXP));
    }
}
