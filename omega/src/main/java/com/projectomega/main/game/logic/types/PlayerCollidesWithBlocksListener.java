package com.projectomega.main.game.logic.types;

import com.projectomega.main.events.EventListener;
import com.projectomega.main.events.types.PlayerMoveEvent;
import com.projectomega.main.game.chunk.ChunkManager;

public class PlayerCollidesWithBlocksListener {

    @EventListener
    public void onMove(PlayerMoveEvent event){
        if(event.getPlayer().getLocation().getBlock()==null || event.getNewLocation().getBlock()==null)
            return;
        if(!event.getPlayer().getLocation().getBlock().getType().isSolid() && event.getNewLocation().getBlock().getType().isSolid()){
            event.setCancelled(true);
            ChunkManager.unloadChunk(event.getNewLocation().getChunk(), event.getPlayer());
            ChunkManager.loadChunk(event.getNewLocation().getChunk(), event.getPlayer());
        }
    }
}
