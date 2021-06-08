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
import com.projectomega.main.packets.types.PacketJoinGame;
import com.projectomega.main.packets.types.PacketLoginSuccess;
import com.projectomega.main.packets.types.PacketPlayerPositionAndLook;
import com.projectomega.main.packets.types.PacketUpdateViewPosition;
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
                PacketUtil.writePacketToOutputStream(packet.getChannel(), new PacketLoginSuccess(player));

                Omega.addPlayerConnection(player);

                Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                    player.sendPacket(new PacketJoinGame(player));

                    Chunk playerchunk = player.getLocation().getChunk();

                    Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                        for (int x = -6; x <= 6; x++) {
                            for (int z = -6; z <= 6; z++) {
                                player.getWorld().sendChunkData(new ChunkPosition(playerchunk.getX() + x, playerchunk.getZ() + z), player);
                            }
                        }
                    }, Duration.ticks(3));


                    Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                        player.sendPacket(new PacketUpdateViewPosition(player));//new OutboundPacket(PacketType.UPDATE_VIEW_POSITION, new VarInt(playerchunk.getX()), new VarInt(playerchunk.getZ())));
                    }, Duration.ticks(4));

                    Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                        player.sendPacket(new PacketPlayerPositionAndLook(player));
                    }, Duration.ticks(20));

                    Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                        player.sendPacket(new PacketPlayerPositionAndLook(player));
                    }, Duration.ticks(20 * 3));
                }, Duration.ticks(1));
            }
        }
    }
}
