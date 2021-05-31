package com.projectomega.main.game;

import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.PacketUtil;
import com.projectomega.main.packets.datatype.UnsignedByte;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.nullicorn.nedit.type.NBTCompound;
import me.nullicorn.nedit.type.NBTList;
import me.nullicorn.nedit.type.TagType;

import java.util.ArrayList;
import java.util.List;

public class World {

    private List<Region> regions = new ArrayList<>();
    private String name;

    public World(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void sendChunkData(ChunkPosition position, Player player) {
        Region region = getRegion(position.getRegionX(), position.getRegionZ());
        int x = position.getX();
        int z = position.getZ();
        Chunk chunk = region.getLoadedChunk(x, z);
        if (chunk == null) {
            chunk = region.createChunk(x, z);
        }
        NBTCompound heightmap = new NBTCompound();
        //NBTList motion_blocking = new NBTList(TagType.LONG);
        long[] motion_blocking = new long[36];
        for (int i = 0; i < 36; i++) {
            motion_blocking[i] = 0l;
        }
        heightmap.put("MOTION_BLOCKING", motion_blocking);
        System.out.println(heightmap.toString());
        int[] biomes = new int[1024];
        for(int i = 0; i < biomes.length; i++){
            biomes[i]=0;
        }
        byte[] data = new byte[128*128*128];
        int length = createChunkSectionStructure(data);
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            b[i] = data[i];
        }

        NBTCompound blockentities = new NBTCompound();
        //player.sendPacket(new OutboundPacket(PacketType.CHUNK_DATA, new Object[]{x,z,true,new VarInt(127),heightmap,biomes,new VarInt(data.length),data,new VarInt(0),blockentities,0}));
        player.sendPacket(new OutboundPacket(PacketType.CHUNK_DATA, new Object[]{x, z, false, new VarInt(Integer.MAX_VALUE), heightmap, new VarInt(length), b, new VarInt(0)}));

    }

    private int createChunkSectionStructure(byte[] data) {
        byte bitsperblock = 8;

        int palletelength = 0;
        List<Integer> pallete = new ArrayList<>();
        if (bitsperblock <= 4) {

        } else if (bitsperblock <= 8) {
            pallete.add(0);
            pallete.add(1);
            palletelength = pallete.size();
        } else {
            //Direct
            //No fields
        }
        int dataarraylength = 4096;
        long[] dataarray = new long[4096];
        for (int i = 0; i < dataarray.length; i++) {
            dataarray[i] = i % 2;
        }

        byte[] blocklight = new byte[4096 / 2];
        byte[] skylight = new byte[4096 / 2];
        for (int i = 0; i < blocklight.length; i++) {
            blocklight[i] = 127;
            skylight[i] = 127;
        }

        int offset = 0;
        offset += ByteUtils.addByteToByteArray(data, offset, new UnsignedByte(bitsperblock).getUnsignedByte());
        offset += PacketUtil.writeVarInt(data, offset, palletelength);
        for (int i = 0; i < palletelength; i++) {
            offset += ByteUtils.addVarIntToByteArray(data, offset, pallete.get(i));
        }
        offset += ByteUtils.addVarIntToByteArray(data, offset, dataarraylength);
        for (long d : dataarray) {
            offset += PacketUtil.writeLong(data, offset, d);
        }
        for(int i = 0; i < blocklight.length;i++){
            offset+= ByteUtils.addByteToByteArray(data,offset,blocklight[i]);
        }
        for(int i = 0; i < skylight.length;i++){
            offset+= ByteUtils.addByteToByteArray(data,offset,skylight[i]);
        }


        return offset;
    }

    public Region getRegion(int x, int z) {
        for (Region region : regions) {
            if (region.getX() == x && region.getZ() == z)
                return region;
        }
        Region region = new Region(this, x, z);
        regions.add(region);
        return region;
    }
}
