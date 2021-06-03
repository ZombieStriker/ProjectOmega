package com.projectomega.main.game.packetlogic;

import com.projectomega.main.game.Location;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.packets.InboundPacket;
import com.projectomega.main.packets.PacketListener;

public class UpdatePlayerPositionPacketLogic implements PacketListener {

    @Override
    public void onCall(InboundPacket packet) {
        Player player = Omega.getPlayerByChannel(packet.getChannel());
        player.getEntity().setLocation(Location.at((double)packet.getData(0),(double)packet.getData(1),(double)packet.getData(2),player.getWorld()));
    }
}
