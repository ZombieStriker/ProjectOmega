package com.projectomega.main.packets.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.game.chat.TextMessage;
import com.projectomega.main.game.inventory.Inventory;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;

public class PacketOpenWindow extends OutboundPacket {
    public PacketOpenWindow(Player player, Inventory inventory) {
        super(PacketType.OPEN_WINDOW, player.getProtocolVersion(), new VarInt(inventory.getWindowID()), new VarInt(inventory.getType().getId()), TextMessage.text("inv"), 0);
    }
}
