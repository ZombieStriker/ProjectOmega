package com.projectomega.main.game;

import com.projectomega.main.game.chat.JsonChatBuilder;
import com.projectomega.main.game.chat.JsonChatElement;
import com.projectomega.main.game.entity.Entity;
import com.projectomega.main.game.entity.EntityType;
import com.projectomega.main.game.inventory.ItemStack;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.PacketUtil;
import com.projectomega.main.packets.datatype.*;
import com.projectomega.main.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.nullicorn.nedit.type.NBTCompound;
import me.nullicorn.nedit.type.NBTList;
import me.nullicorn.nedit.type.TagType;

import java.util.ArrayList;
import java.util.List;

public class World {

    private final List<Region> regions = new ArrayList<>();
    private final String name;
    private final List<Entity> entities = new ArrayList<>();

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
        VarInt[] biomes = new VarInt[1024];
        for (int i = 0; i < biomes.length; i++) {
            biomes[i] = new VarInt(0);
        }
        byte[] data = new byte[128 * 128 * 128];
        int length = createChunkSectionStructure(data);
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            b[i] = data[i];
        }

        NBTCompound blockentities = new NBTCompound();
        player.sendPacket(new OutboundPacket(PacketType.CHUNK_DATA, new Object[]{x,z,true,new VarInt(255),heightmap,new VarInt(biomes.length), biomes,new VarInt(data.length),data,new VarInt(0),blockentities,0}));
       //player.sendPacket(new OutboundPacket(PacketType.CHUNK_DATA, new Object[]{x, z, false, new VarInt(255), heightmap, new VarInt(length), b, new VarInt(0)}));

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
        for (int i = 0; i < blocklight.length; i++) {
            offset += ByteUtils.addByteToByteArray(data, offset, blocklight[i]);
        }
        for (int i = 0; i < skylight.length; i++) {
            offset += ByteUtils.addByteToByteArray(data, offset, skylight[i]);
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

    public Entity spawnEntity(EntityType type, Location location) {
        return spawnEntity(getUnusedEID(), type, location);
    }

    private Entity spawnEntity(int unusedEID, EntityType type, Location location) {
        Entity ent = new Entity(unusedEID, location, type);
        entities.add(ent);
        System.out.println(type.isLiving());
        if (type.isLiving()) {
            OutboundPacket spawnLivingEntity = new OutboundPacket(PacketType.SPAWN_LIVING_ENTITY, new Object[]{new VarInt(ent.getEntityID()), ent.getUniqueID(),
                    new VarInt(ent.getType().getTypeID()), ent.getLocation().getX(), ent.getLocation().getY(), ent.getLocation().getZ(),
                    new Angle(ent.getLocation().getYaw()), new Angle(ent.getLocation().getPitch()), new Angle(ent.getLocation().getPitch()), (short) 0, (short) 0, (short) 0});
            for (Player player : getPlayers()) {
                player.sendPacket(spawnLivingEntity);
            }
        } else {
            OutboundPacket spawnEntity = new OutboundPacket(PacketType.SPAWN_ENTITY, new Object[]{new VarInt(ent.getEntityID()), ent.getUniqueID(),
                    new VarInt(ent.getType().getTypeID()), ent.getLocation().getX(), ent.getLocation().getY(), ent.getLocation().getZ(),
                    new Angle(ent.getLocation().getYaw()), new Angle(ent.getLocation().getPitch()), 0, (short) 0, (short) 0, (short) 0});
            for (Player player : getPlayers()) {
                player.sendPacket(spawnEntity);
            }
        }
        return ent;
    }

    public Entity dropItem(ItemStack is, Location location) {
        return dropItem(getUnusedEID(), is, location);
    }

    private Entity dropItem(int unusedEID, ItemStack is, Location location) {
        Entity ent = new Entity(unusedEID, location, EntityType.ITEM);
        entities.add(ent);
        OutboundPacket spawnEntity = new OutboundPacket(PacketType.SPAWN_ENTITY, new Object[]{new VarInt(ent.getEntityID()), ent.getUniqueID(),
                new VarInt(ent.getType().getTypeID()), ent.getLocation().getX(), ent.getLocation().getY(), ent.getLocation().getZ(),
                new Angle(ent.getLocation().getYaw()), new Angle(ent.getLocation().getPitch()), 0, (short) 0, (short) 0, (short) 0});
        NBTCompound itemmeta = new NBTCompound();
        OutboundPacket metaData = new OutboundPacket(PacketType.ENTITY_METADATA, new Object[]{
                new VarInt(ent.getEntityID()), new MetaData().add(6,new Slot((short)10, (byte) 2,(short)0,itemmeta))
        });
        for (Player player : getPlayers()) {
            player.sendPacket(spawnEntity);
            player.sendPacket(metaData);
        }
        return ent;
    }


    private List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        for (Player player : Omega.getPlayers()) {
            if (player.getWorld() == this)
                players.add(player);
        }
        return players;
    }

    public int getUnusedEID() {
        int id = entities.size();
        loop:
        while (true) {
            for (Entity e : entities) {
                if (e.getEntityID() == id) {
                    id++;
                    continue loop;
                }
            }
            break loop;
        }
        return id;
    }
}
