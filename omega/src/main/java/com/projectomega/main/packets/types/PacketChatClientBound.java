package com.projectomega.main.packets.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.game.chat.TextMessage;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketHandler;
import com.projectomega.main.packets.PacketType;

import java.util.UUID;

public class PacketChatClientBound extends OutboundPacket {
    public PacketChatClientBound(Player player, String text, int typeID, UUID sender) {
        super(PacketType.CHAT_CLIENTBOUND,player.getProtocolVersion(), TextMessage.text(text), (byte)typeID, sender);
    }
}
