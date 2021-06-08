package com.projectomega.main.packets.types;

import com.projectomega.main.game.Chunk;
import com.projectomega.main.game.Player;
import com.projectomega.main.game.World;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.nullicorn.nedit.type.NBTCompound;

public class PacketChunkData extends OutboundPacket {
    public PacketChunkData(Player player, Chunk chunk) {
        super(PacketType.CHUNK_DATA, player.getProtocolVersion());
        if (getProtcolVersion() <= 762) {

            NBTCompound heightmap = new NBTCompound();
            //NBTList motion_blocking = new NBTList(TagType.LONG);
            long[] motion_blocking = new long[36];
            for (int i = 0; i < 36; i++) {
                motion_blocking[i] = 0l;
            }
            heightmap.put("MOTION_BLOCKING", motion_blocking);
            VarInt[] biomes = new VarInt[1024];
            for (int i = 0; i < biomes.length; i++) {
                biomes[i] = new VarInt(0);
            }
            ByteBuf data = Unpooled.buffer();
            int length = World.createChunkSectionStructure(chunk, 16, player, data);
            byte[] b = new byte[length];
            for (int i = 0; i < length; i++) {
                b[i] = data.getByte(i);
            }
            setData(chunk.getX(), chunk.getZ(), true, new VarInt(65535), heightmap, new VarInt(biomes.length), biomes, new VarInt(length), b, new VarInt(0));
        } else {
            NBTCompound heightmap = new NBTCompound();
            //NBTList motion_blocking = new NBTList(TagType.LONG);
            long[] motion_blocking = new long[36];
            for (int i = 0; i < 36; i++) {
                motion_blocking[i] = 0l;
            }
            heightmap.put("MOTION_BLOCKING", motion_blocking);
            VarInt[] biomes = new VarInt[1024];
            for (int i = 0; i < biomes.length; i++) {
                biomes[i] = new VarInt(0);
            }
            ByteBuf data = Unpooled.buffer();
            int length = World.createChunkSectionStructure(chunk, 18, player, data);
            byte[] b = new byte[length];
            for (int i = 0; i < length; i++) {
                b[i] = data.getByte(i);
            }
            setData(chunk.getX(), chunk.getZ(), new VarInt(2), 65535L,0L, heightmap, new VarInt(biomes.length), biomes, new VarInt(length), b, new VarInt(0));
        }
    }
}
