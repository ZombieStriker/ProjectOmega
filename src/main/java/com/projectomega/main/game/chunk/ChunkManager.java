package com.projectomega.main.game.chunk;

import com.projectomega.main.game.Chunk;
import com.projectomega.main.game.ChunkPosition;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.task.Duration;
import com.projectomega.main.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChunkManager {

    private static HashMap<UUID, List<Chunk>> chunksLoadedByPlayer = new HashMap<>();

    public static List<Chunk> getChunksLoadedByPlayer(Player player) {
        return chunksLoadedByPlayer.get(player.getUuid());
    }

    public static void loadChunk(Chunk chunk, Player player) {
        List<Chunk> loadedChunks = chunksLoadedByPlayer.get(player.getUuid());
        if (loadedChunks != null) {
            if (loadedChunks.contains(chunk))
                return;
        }
        chunk.getWorld().sendChunkData(new ChunkPosition(chunk.getX(), chunk.getZ()), player);

        Omega.getTaskManager().getMainThread().runTaskLater(new Task() {
            @Override
            protected void run() {
                chunk.getWorld().sendChunkData(new ChunkPosition(chunk.getX(), chunk.getZ()), player);
            }
        }, Duration.seconds(1));
        if (loadedChunks == null) {
            loadedChunks = new ArrayList<>();
            chunksLoadedByPlayer.put(player.getUuid(), loadedChunks);
        }
        loadedChunks.add(chunk);
    }

    public static void unloadChunk(Chunk chunk, Player player) {
        List<Chunk> loadedChunks = chunksLoadedByPlayer.get(player.getUuid());
        if (loadedChunks != null) {
            if (!loadedChunks.contains(chunk))
                return;
        }
        loadedChunks.remove(chunk);
        OutboundPacket packet = new OutboundPacket(PacketType.UNLOAD_CHUNK,chunk.getX(),chunk.getZ());
        player.sendPacket(packet);

    }

    public static void removePlayer(Player player) {
        chunksLoadedByPlayer.remove(player.getUuid());
    }
}
