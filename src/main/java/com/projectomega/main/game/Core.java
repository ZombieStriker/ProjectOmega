package com.projectomega.main.game;

import com.projectomega.main.game.packetlogic.PacketLogicManager;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.PacketUtil;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

public class Core extends Thread {

    public List<Player> players = new ArrayList<>();


    public void run() {
        System.out.println("Starting Server...");
        PacketLogicManager.init();
        while (true) {
            long start = System.currentTimeMillis();
            for (Player player : new ArrayList<>(players)) {
                if(!player.getConnection().isActive() || !player.getConnection().isOpen()){
                    players.remove(player);
                    continue;
                }
                player.sendPacket(new OutboundPacket(PacketType.KEEP_ALIVE_CLIENTBOUND_OLD,new Object[]{start}));
                for (OutboundPacket packet : player.getOutgoingPackets()) {
                    try {
                        PacketUtil.writePacketToOutputStream(player.getConnection(), packet);
                    }catch (Error|Exception e43){
                        e43.printStackTrace();
                    }
                }
                player.clearPackets();
            }
            try {
                if((1000 / 20) - (System.currentTimeMillis() - start) > 0)
                Thread.sleep((1000 / 20) - (System.currentTimeMillis() - start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void addPlayerConnection(Player player){
        players.add( player);
    }

    public Player getPlayerByChannel(Channel channel) {
        for(Player player : players){
            if(player.getConnection().equals(channel))
                return player;
        }
        return null;
    }
}
