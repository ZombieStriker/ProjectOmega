package com.projectomega.main.game;

import com.projectomega.main.game.packetlogic.PacketLogicManager;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketUtil;

import java.util.ArrayList;
import java.util.List;

public class Core extends Thread {

    public List<Player> players = new ArrayList<>();


    public void run() {
        System.out.println("Starting Server...");
        PacketLogicManager.init();
        while (true) {
            long start = System.currentTimeMillis();
            for (Player player : players) {
                for (OutboundPacket packet : player.getOutgoingPackets()) {
                    PacketUtil.writePacketToOutputStream(player.getConnection(), packet);
                }
                player.clearPackets();
            }
            try {
                Thread.sleep((1000 / 20) - (System.currentTimeMillis() - start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
