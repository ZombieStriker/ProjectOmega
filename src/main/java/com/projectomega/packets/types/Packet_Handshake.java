package com.projectomega.packets.types;

import com.projectomega.packets.PacketHandler;
import com.projectomega.packets.PacketType;
import com.projectomega.packets.PacketUtil;

import java.io.IOException;
import java.io.InputStream;

public class Packet_Handshake extends PacketHandler {


    public Packet_Handshake() {
        super(PacketType.HANDSHAKE);
    }

    @Override
    public void call(InputStream stream, int packetsize) {
        try {
            System.out.println(PacketUtil.getString(stream,packetsize));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
