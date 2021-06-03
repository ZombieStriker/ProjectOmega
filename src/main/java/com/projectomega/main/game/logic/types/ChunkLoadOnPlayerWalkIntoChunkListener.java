package com.projectomega.main.game.logic.types;

import com.projectomega.main.events.EventListener;
import com.projectomega.main.events.types.PlayerMoveEvent;
import com.projectomega.main.game.Chunk;
import com.projectomega.main.game.ChunkPosition;

public class ChunkLoadOnPlayerWalkIntoChunkListener {

    @EventListener
    public void onPlayerMove(PlayerMoveEvent event){
        Chunk newLocationChunk = event.getNewLocation().getChunk();
        Chunk playerChunk = event.getPlayer().getLocation().getChunk();
        if(newLocationChunk.getX() != playerChunk.getX()
        || newLocationChunk.getZ() != playerChunk.getZ()
        ){
            for(int x = -16; x <= 16; x++) {
                for(int z = -16; z <= 16; z++) {
                    event.getPlayer().getWorld().sendChunkData(new ChunkPosition(x,z), event.getPlayer());
                }
            }
        }
    }
}
