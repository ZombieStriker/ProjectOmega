package com.projectomega.packets;

import com.projectomega.packets.types.Packet_Handshake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PacketUtil {

    private static List<PacketHandler> handlers = new ArrayList<>();



    public static void handlePacket(InputStream is) throws IOException {
        int read = is.read();

        int packetid = is.read();

        System.out.println("Packet Recieved: "+read+"||"+packetid);
        boolean packetFound = false;
        for(PacketHandler handler : handlers){
            if(handler.getPacketType().getId()==packetid){
                packetFound = true;
                handler.call(is,read);
            }
        }
        if(!packetFound){
            System.out.println("PACKET "+packetid+" NOT REGISTERED: "+getString(is,read));
        }
    }

    public static int getInteger(InputStream is) throws IOException {
        return is.read(new byte[4]);
    }
    public static int getShort(InputStream is) throws IOException {
        return is.read(new byte[2]);
    }

    public static String getString(InputStream is, int read) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        for(int i = 0; i < read; i++){
            sb.append((char)br.read());
        }
        return sb.toString();
    }

    public static void init() {handlers.add(new Packet_Handshake());
    }
}
