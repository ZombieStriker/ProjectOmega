package com.projectomega.main.packets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PacketManager {

    private static HashMap<PacketType, List<PacketListener>> listeners = new HashMap<>();

    public static void registerPacketListener(PacketType type, PacketListener packetListener){
        if(!listeners.containsKey(type)){
            listeners.put(type,new ArrayList<>());
        }
        List<PacketListener> listenerlist = listeners.get(type);
        listenerlist.add(packetListener);
    }
    public static List<PacketListener> getListeners(PacketType type){
        return listeners.get(type);
    }

}
