package com.projectomega.main.game;

import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;

import java.util.ArrayList;
import java.util.List;

public class World {

    public List<Region> regions = new ArrayList<>();

    public void sendChunkData(ChunkPosition position, Player player){
        Region region = getRegion(position.getRegionX(),position.getRegionZ());
        int x = position.getX();
        int z =  position.getZ();
        Chunk chunk = region.getLoadedChunk(x,z);
        player.sendPacket(new OutboundPacket(PacketType.CHUNK_DATA, new Object[]{x,z,new VarInt(127)}));

    }
    public Region getRegion(int x, int z){
        for(Region region : regions){
            if(region.getX()==x&&region.getZ()==z)
                return region;
        }
        return null;
    }
}
