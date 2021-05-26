package com.projectomega.main.game;

import com.projectomega.main.packets.OutboundPacket;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private InetSocketAddress connection;

    private List<OutboundPacket> outgoingPackets = new ArrayList<>();

    public List<OutboundPacket> getOutgoingPackets(){
        return new ArrayList<>(outgoingPackets);
    }
    public void sendPacket(OutboundPacket packet){
        this.outgoingPackets.add(packet);
    }
    public void cancelPacket(OutboundPacket packet){
        outgoingPackets.remove(packet);
    }

    public Player(InetSocketAddress connection){
        this.connection = connection;
    }
    public InetSocketAddress getConnection(){
        return connection;
    }

    public void clearPackets() {
        outgoingPackets.clear();
    }
}
