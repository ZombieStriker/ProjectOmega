package com.projectomega.main.game.chunk;

import com.projectomega.main.game.Chunk;
import com.projectomega.main.game.ChunkPosition;
import com.projectomega.main.game.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChunkManager {

    private static HashMap<UUID, List<Chunk>> chunksLoadedByPlayer = new HashMap<>();

    public static void loadChunk(Chunk chunk, Player player){
        List<Chunk> loadedChunks = chunksLoadedByPlayer.get(player.getUuid());
        if(loadedChunks !=null) {
            if(loadedChunks.contains(chunk))
                return;
        }
        chunk.getWorld().sendChunkData(new ChunkPosition(chunk.getX(), chunk.getZ()), player);
        if(loadedChunks==null){
            loadedChunks = new ArrayList<>();
            chunksLoadedByPlayer.put(player.getUuid(),loadedChunks);
        }
        loadedChunks.add(chunk);
    }
    public static void unloadChunk(Chunk chunk, Player player){
        List<Chunk> loadedChunks = chunksLoadedByPlayer.get(player.getUuid());
        if(loadedChunks !=null) {
            if(!loadedChunks.contains(chunk))
                return;
        }
        loadedChunks.remove(chunk);


    }
}
