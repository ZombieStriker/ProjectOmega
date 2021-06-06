package com.projectomega.main.game.packetlogic;

import com.projectomega.main.events.EventBus;
import com.projectomega.main.events.types.PlayerJoinEvent;
import com.projectomega.main.game.Chunk;
import com.projectomega.main.game.ChunkPosition;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.packets.*;
import com.projectomega.main.packets.datatype.UnsignedByte;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.task.Duration;
import com.projectomega.main.utils.MojangAPI;
import com.projectomega.main.utils.NBTTagUtil;
import me.nullicorn.nedit.type.NBTCompound;

import java.util.UUID;

public class PlayerLoginPacketLogicLogic implements PacketListener {
    @Override
    public void onCall(InboundPacket packet) {
        String name = (String) packet.getData(4);
        if (name == null || name.length() < 3)
            return;
        String mojangAPIUUID = MojangAPI.getUUIDFromName(name);
        if (mojangAPIUUID == null) {
            System.out.println("Failed to retrieve UUID");
            mojangAPIUUID = UUID.randomUUID().toString();
        }
        UUID uuid = UUID.fromString(mojangAPIUUID);

        Player player = new Player(name, uuid, packet.getChannel(), (Integer) packet.getData(0), Omega.getSpawnWorld().getSpawn());
        PlayerJoinEvent joinEvent = new PlayerJoinEvent(player);
        if (!EventBus.INSTANCE.post(joinEvent).isCancelled()) {
            if (player.getProtocolVersion() < 750) {
            } else {
                OutboundPacket outboundPacket = new OutboundPacket(PacketType.LOGIN_SUCCESS, uuid, name);
                PacketUtil.writePacketToOutputStream(packet.getChannel(), outboundPacket);

                Omega.addPlayerConnection(player);

                Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                    NBTCompound dimensionCodec = NBTTagUtil.generateDimensionCodec();
                    OutboundPacket joingame = new OutboundPacket(PacketType.JOIN_GAME, player.getEntityID(), true, new UnsignedByte((byte) 0), (byte) -1, new VarInt(1), "overworld", dimensionCodec, NBTTagUtil.generateDimensionType(), "overworld", 0l, new VarInt(32), new VarInt(10), false, true, false, false);
                    player.sendPacket(joingame);

                    Chunk playerchunk = player.getLocation().getChunk();

                    Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                        for (int x = -6; x <= 6; x++) {
                            for (int z = -6; z <= 6; z++) {
                                player.getWorld().sendChunkData(new ChunkPosition(playerchunk.getX() + x, playerchunk.getZ() + z), player);
                            }
                        }
                    }, Duration.ticks(3));

                    Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                        player.sendPacket(new OutboundPacket(PacketType.UPDATE_VIEW_POSITION, new VarInt(playerchunk.getX()), new VarInt(playerchunk.getZ())));
                    }, Duration.ticks(4));

                    Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                        player.sendPacket(new OutboundPacket(PacketType.PLAYER_POSITION_AND_LOOK,
                                player.getEntity().getLocation().getX(),
                                player.getEntity().getLocation().getY(),
                                player.getEntity().getLocation().getZ(),
                                player.getEntity().getLocation().getYaw(),
                                player.getEntity().getLocation().getPitch(),
                                (byte) 0, new VarInt(1)));
                    }, Duration.ticks(20));

                    Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                        player.sendPacket(new OutboundPacket(PacketType.PLAYER_POSITION_AND_LOOK,
                                player.getEntity().getLocation().getX(),
                                player.getEntity().getLocation().getY(),
                                player.getEntity().getLocation().getZ(),
                                player.getEntity().getLocation().getYaw(),
                                player.getEntity().getLocation().getPitch(),
                                (byte) 0, new VarInt(1)));
                    }, Duration.ticks(20 * 3));
                }, Duration.ticks(1));
            }
        }
    }
}
