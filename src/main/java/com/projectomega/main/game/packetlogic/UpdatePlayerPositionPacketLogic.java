package com.projectomega.main.game.packetlogic;

import com.projectomega.main.events.EventBus;
import com.projectomega.main.events.types.PlayerMoveEvent;
import com.projectomega.main.game.Location;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.packets.InboundPacket;
import com.projectomega.main.packets.PacketListener;

public class UpdatePlayerPositionPacketLogic implements PacketListener {

    @Override
    public void onCall(InboundPacket packet) {
        Player player = Omega.getPlayerByChannel(packet.getChannel());
        Location newlocation = Location.at((double)packet.getData(0),(double)packet.getData(1),(double)packet.getData(2),player.getWorld());
        PlayerMoveEvent moveevent = new PlayerMoveEvent(player,newlocation);
        EventBus.INSTANCE.post(moveevent);
        if(moveevent.isCancelled()){
            player.teleport(player.getEntity().getLocation());
        }else {
            player.getEntity().setLocation(newlocation);
        }

    }
}
