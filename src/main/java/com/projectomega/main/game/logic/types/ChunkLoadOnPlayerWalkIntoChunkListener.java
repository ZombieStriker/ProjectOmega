package com.projectomega.main.game.logic.types;

import com.projectomega.main.events.EventListener;
import com.projectomega.main.events.types.PlayerMoveEvent;
import com.projectomega.main.game.Chunk;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.chunk.ChunkManager;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.task.Duration;

public class ChunkLoadOnPlayerWalkIntoChunkListener {

    @EventListener
    public void onPlayerMove(PlayerMoveEvent event) {
        Chunk newLocationChunk = event.getNewLocation().getChunk();
        Chunk playerChunk = event.getPlayer().getLocation().getChunk();
        if (newLocationChunk.getX() != playerChunk.getX()
                || newLocationChunk.getZ() != playerChunk.getZ()
        ) {
            System.out.println("Chunk different");
            for (int x = -5; x <= 5; x++) {
                for (int z = -5; z <= 5; z++) {
                    ChunkManager.loadChunk(playerChunk.getWorld().getChunkAt(newLocationChunk.getX() + x, newLocationChunk.getZ() + z), event.getPlayer());
                    System.out.println("Loading Chunk "+x+" "+z);
                }
            }
            Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                event.getPlayer().sendPacket(new OutboundPacket(PacketType.UPDATE_VIEW_POSITION, new VarInt(newLocationChunk.getX()), new VarInt(newLocationChunk.getZ())));
            }, Duration.ticks(20));
        }
    }
}
