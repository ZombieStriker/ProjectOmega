package com.projectomega.main.packets.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;

public class PacketLoginSuccess extends OutboundPacket {
    public PacketLoginSuccess(Player player) {
        super(PacketType.LOGIN_SUCCESS, player.getProtocolVersion(), player.getUuid(), player.getName());
    }
}
