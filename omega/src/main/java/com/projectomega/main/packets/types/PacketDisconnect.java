package com.projectomega.main.packets.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;

public class PacketDisconnect extends OutboundPacket {
    public PacketDisconnect(Player player, String disconnectMessage) {
        super(PacketType.DISCONNECT,player.getProtocolVersion(),disconnectMessage);
    }
}
