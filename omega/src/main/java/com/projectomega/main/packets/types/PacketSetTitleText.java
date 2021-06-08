package com.projectomega.main.packets.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.game.chat.TextMessage;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;

public class PacketSetTitleText extends OutboundPacket {
    public PacketSetTitleText(Player player, String text) {
        super(PacketType.SET_TITLE_TEXT,player.getProtocolVersion(), TextMessage.text(text));
    }
}
