package com.projectomega.main.game.logic.types;

import com.projectomega.main.events.EventListener;
import com.projectomega.main.events.types.PlayerJoinEvent;
import com.projectomega.main.events.types.PlayerMoveEvent;
import com.projectomega.main.game.ChunkPosition;
import com.projectomega.main.game.Omega;
import com.projectomega.main.task.Duration;
import com.projectomega.main.task.Task;

public class LoadChunksOnJoinListener {

    @EventListener
    public void onMove(PlayerJoinEvent event) {
        int count = 0;
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                int finalX = x;
                int finalZ = z;
                Omega.getTaskManager().getMainThread().runTaskLater(new Task() {
                    @Override
                    protected void run() {
                        event.getPlayer().getWorld().sendChunkData(new ChunkPosition(finalX, finalZ), event.getPlayer());
                    }
                }, Duration.ticks(20+count));
                System.out.println("count= "+count);
                count++;
            }
        }
    }
}
