package com.projectomega.main.game.packetlogic;

import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.packets.InboundPacket;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketListener;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.UnsignedByte;

public class UpdateHeldItemSlot implements PacketListener {
    @Override
    public void onCall(InboundPacket packet) {
        short slot = (short) packet.getData(0);
        Player player = Omega.getPlayerByChannel(packet.getChannel());
        player.updateHeldItemSlot(slot);
    }
}
