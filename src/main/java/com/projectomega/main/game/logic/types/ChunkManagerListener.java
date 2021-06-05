package com.projectomega.main.game.logic.types;

import com.projectomega.main.events.EventListener;
import com.projectomega.main.events.types.PlayerMoveEvent;
import com.projectomega.main.events.types.PlayerQuitEvent;
import com.projectomega.main.game.Chunk;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.chunk.ChunkManager;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.task.Duration;

import java.util.ArrayList;

public class ChunkManagerListener {

    @EventListener
    public void onPlayerMove(PlayerMoveEvent event) {
        Chunk newLocationChunk = event.getNewLocation().getChunk();
        Chunk playerChunk = event.getPlayer().getLocation().getChunk();
        if (newLocationChunk.getX() != playerChunk.getX()
                || newLocationChunk.getZ() != playerChunk.getZ()
        ) {

            int renderdistance = 5;

            if (ChunkManager.getChunksLoadedByPlayer(event.getPlayer()) != null) {
                for (Chunk loadedChunksByPlayer : new ArrayList<>(ChunkManager.getChunksLoadedByPlayer(event.getPlayer()))) {
                    if (loadedChunksByPlayer.getX() - newLocationChunk.getX() < -renderdistance || loadedChunksByPlayer.getX() - newLocationChunk.getX() > renderdistance ||
                            loadedChunksByPlayer.getZ() - newLocationChunk.getZ() < -renderdistance || loadedChunksByPlayer.getZ() - newLocationChunk.getZ() > renderdistance
                    ) {
                        ChunkManager.unloadChunk(loadedChunksByPlayer, event.getPlayer());
                    }

                }
            }
            System.out.println("Chunk different");
            for (int x = -renderdistance; x <= renderdistance; x++) {
                for (int z = -renderdistance; z <= renderdistance; z++) {
                    ChunkManager.loadChunk(playerChunk.getWorld().getChunkAt(newLocationChunk.getX() + x, newLocationChunk.getZ() + z), event.getPlayer());
                }
            }
            Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                event.getPlayer().sendPacket(new OutboundPacket(PacketType.UPDATE_VIEW_POSITION, new VarInt(newLocationChunk.getX()), new VarInt(newLocationChunk.getZ())));
            }, Duration.ticks(5));
        }
    }

    @EventListener
    public void onQuit(PlayerQuitEvent event) {
        ChunkManager.removePlayer(event.getPlayer());
    }
}
