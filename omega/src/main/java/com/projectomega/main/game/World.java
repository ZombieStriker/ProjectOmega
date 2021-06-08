package com.projectomega.main.game;

import com.projectomega.main.game.entity.Entity;
import com.projectomega.main.game.entity.EntityType;
import com.projectomega.main.game.inventory.ItemStack;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.PacketUtil;
import com.projectomega.main.packets.datatype.*;
import com.projectomega.main.versions.ProtocolManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.nullicorn.nedit.type.NBTCompound;

import java.util.ArrayList;
import java.util.List;

public class World {

    private final List<Region> regions = new ArrayList<>();
    private final String name;
    private final List<Entity> entities = new ArrayList<>();
    private Location spawn = Location.at(-5, 16, -5, this);

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
        Chunk chunk = region.getOrLoadChunk(x, z);
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
        int length = createChunkSectionStructure(chunk,16,player, data);
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            b[i] = data.getByte(i);
        }

        NBTCompound blockentities = new NBTCompound();
        //  player.sendPacket(new OutboundPacket(PacketType.CHUNK_DATA, new Object[]{x,z,false,new VarInt(255),heightmap,new VarInt(length),b,new VarInt(0)}));

        player.sendPacket(new OutboundPacket(PacketType.CHUNK_DATA, x, z, true, new VarInt(65535), heightmap, new VarInt(biomes.length), biomes, new VarInt(length), b, new VarInt(0)));
        //sendBlocks(chunk,player);


        //player.sendPacket(new OutboundPacket(PacketType.CHUNK_DATA, new Object[]{x, z, false, new VarInt(255), heightmap, new VarInt(length), b, new VarInt(0)}));

    }

    private void sendBlocks(Chunk chunk, Player player) {
        List<VarLong> varLongs = new ArrayList<>();
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    varLongs.add(PacketUtil.encodeBlockToBlocksArray(player.getProtocolVersion(), chunk.getBlockAtChunkRelative(x, y, z)));
                }
            }
        }
        OutboundPacket multiblockChange = new OutboundPacket(PacketType.MULTI_BLOCK_CHANGE, PacketUtil.getChunkSectionPositionAsALong(chunk, 0), false, new VarInt(varLongs.size()), varLongs.toArray(new VarLong[varLongs.size()]));
        player.sendPacket(multiblockChange);
    }

    private int createChunkSectionStructure(Chunk chunk, int sections, Player player, ByteBuf data) {
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
                OutboundPacket spawnLivingEntity = new OutboundPacket(PacketType.SPAWN_LIVING_ENTITY, new VarInt(ent.getEntityID()), ent.getUniqueID(),
                        ent.getType(), ent.getLocation().getX(), ent.getLocation().getY(), ent.getLocation().getZ(),
                        new Angle(ent.getLocation().getYaw()), new Angle(ent.getLocation().getPitch()), new Angle(ent.getLocation().getPitch()), (short) 0, (short) 0, (short) 0);
                player.sendPacket(spawnLivingEntity);
            }
        } else {
            for (Player player : getPlayers()) {
                OutboundPacket spawnEntity = new OutboundPacket(PacketType.SPAWN_ENTITY, new VarInt(ent.getEntityID()), ent.getUniqueID(),
                        ent.getType(), ent.getLocation().getX(), ent.getLocation().getY(), ent.getLocation().getZ(),
                        new Angle(ent.getLocation().getYaw()), new Angle(ent.getLocation().getPitch()), 0, (short) 0, (short) 0, (short) 0);
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
        OutboundPacket spawnEntity = new OutboundPacket(PacketType.SPAWN_ENTITY, new VarInt(ent.getEntityID()), ent.getUniqueID(),
                ent.getType(), ent.getLocation().getX(), ent.getLocation().getY(), ent.getLocation().getZ(),
                new Angle(ent.getLocation().getYaw()), new Angle(ent.getLocation().getPitch()), 0, (short) 0, (short) 0, (short) 0);
        NBTCompound itemmeta = new NBTCompound();
        OutboundPacket metaData = new OutboundPacket(PacketType.ENTITY_METADATA, new VarInt(ent.getEntityID()), new MetaData().add(6, new Slot((short) 10, (byte) 2, (short) 0, itemmeta)));
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