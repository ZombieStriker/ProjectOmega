package com.projectomega.main.game;

import com.projectomega.main.game.entity.Entity;
import com.projectomega.main.game.entity.EntityType;
import com.projectomega.main.game.inventory.ItemStack;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.PacketUtil;
import com.projectomega.main.packets.datatype.*;
import com.projectomega.main.packets.types.PacketEntityMetaData;
import com.projectomega.main.packets.types.PacketTypeSpawnEntity;
import com.projectomega.main.packets.types.PacketTypeSpawnLivingEntity;
import com.projectomega.main.versions.ProtocolManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.nullicorn.nedit.type.NBTCompound;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class World {

    private final List<Region> regions = new ArrayList<>();
    private final String name;
    private final File worldFolder;
    private final List<Entity> entities = new ArrayList<>();
    private Location spawn = Location.at(-5, 16, -5, this);

    public World(String name, File worldFolder) {
        this.name = name;
        this.worldFolder = worldFolder;
    }

    public File getWorldFolder() {
        return worldFolder;
    }

    public String getName() {
        return name;
    }

    public void sendChunkData(ChunkPosition position, Player player) {
        Region region = getRegion(position.getRegionX(), position.getRegionZ());
        int x = position.getX();
        int z = position.getZ();
        Chunk chunk = region.getOrLoadChunk(x, z);
      //  player.sendPacket(new OutboundPacket(PacketType.CHUNK_DATA, x, z, true, new VarInt(65535), heightmap, new VarInt(biomes.length), biomes, new VarInt(length), b, new VarInt(0)));

    }


    public static int createChunkSectionStructure(Chunk chunk, int sections, Player player, ByteBuf data) {
        int offset = 0;
        for(int section = 0; section < sections; section++) {
            byte bitsperblock = 8;

            int palletelength = 0;
            int[] pallet = new int[256];
            int[] indexes = new int[16 * 16 * 16];
            List<Material> materials = new ArrayList<>();
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        Block block = chunk.getBlockAtChunkRelative(x, y + (section * 16), z);
                        if (!materials.contains(block.getType())) {
                            materials.add(block.getType());
                        }
                        indexes[x + (z * 16) + (y * 16 * 16)] = materials.indexOf(block.getType());
                    }
                }
            }
            for (int i = 0; i < Math.min(256, materials.size()); i++) {
                pallet[i] = ProtocolManager.getBlockIDByType(player.getProtocolVersion(), materials.get(i));
            }
            palletelength = materials.size();
            if (bitsperblock <= 4) {
            } else if (bitsperblock <= 8) {
            } else {
                //Direct
                //No fields
            }
            int dataarraylength = 4096 * bitsperblock / 64;
            long[] dataarray = new long[4096 * bitsperblock / 64];
            for (int i = 0; i < dataarray.length; i++) {
                dataarray[i] = 0;
                for (int j = 0; j < 64 / bitsperblock; j++) {
                    dataarray[i] = dataarray[i] << bitsperblock | (indexes[(i * (64 / bitsperblock) + j)]);
                }
            }
            offset += PacketUtil.addShortToByteArray(data, offset, (short) 4096);
            offset += PacketUtil.addByteToByteArray(data, offset, bitsperblock);
            offset += PacketUtil.writeVarInt(data, offset, palletelength);
            for (int i = 0; i < palletelength; i++) {
                offset += PacketUtil.writeVarInt(data, offset, pallet[i]);
            }
            offset += PacketUtil.writeVarInt(data, offset, dataarraylength);
            for (long d : dataarray) {
                offset += PacketUtil.writeLong(data, offset, d);
            }
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
            for (Player player : getPlayers()) {
                player.sendPacket(new PacketTypeSpawnLivingEntity(player,ent));
            }
        } else {
            for (Player player : getPlayers()) {
                player.sendPacket(new PacketTypeSpawnEntity(player,ent));
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
        NBTCompound itemmeta = new NBTCompound();
        for (Player player : getPlayers()) {
            OutboundPacket spawnEntity = new PacketTypeSpawnEntity(player,ent);
            player.sendPacket(spawnEntity);
            OutboundPacket metaData = new PacketEntityMetaData(player,ent,new MetaData().add(6, new Slot((short) 10, (byte) 2, (short) 0, itemmeta)));
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
            break;
        }
        return id;
    }

    public Location getSpawn() {
        return spawn;
    }

    public Chunk getChunkAt(int x, int z) {
        int regionx = x / 32;
        if (regionx < 0)
            regionx -= 1;
        int regionz = z / 32;
        if (regionz < 0)
            regionz -= 1;
        return getRegion(regionx, regionz).getOrLoadChunk(x, z);
    }
}