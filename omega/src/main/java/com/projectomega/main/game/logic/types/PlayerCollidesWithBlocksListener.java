package com.projectomega.main.game.logic.types;

import com.projectomega.main.events.EventListener;
import com.projectomega.main.events.types.PlayerMoveEvent;

public class PlayerCollidesWithBlocksListener {

    @EventListener
    public void onMove(PlayerMoveEvent event){
        if(!event.getPlayer().getLocation().getBlock().getType().isSolid() && event.getNewLocation().getBlock().getType().isSolid()){
            event.setCancelled(true);
        }
    }
}
