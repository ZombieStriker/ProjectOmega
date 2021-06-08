package com.projectomega.main.packets.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;

public class PacketKeepAlive extends OutboundPacket {
    public PacketKeepAlive( Player player) {
        super(PacketType.KEEP_ALIVE_CLIENTBOUND, player.getProtocolVersion());
        setData(System.currentTimeMillis());
    }
}
