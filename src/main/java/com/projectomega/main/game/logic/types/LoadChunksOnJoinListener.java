package com.projectomega.main.game.logic.types;

import com.projectomega.main.events.EventListener;
import com.projectomega.main.events.types.PlayerJoinEvent;
import com.projectomega.main.events.types.PlayerMoveEvent;
import com.projectomega.main.game.ChunkPosition;
import com.projectomega.main.game.Omega;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.task.Duration;
import com.projectomega.main.task.Task;

public class LoadChunksOnJoinListener {

    @EventListener
    public void onJoin(PlayerJoinEvent event) {
        int count = 0;
        for (int x = -6; x <= 6; x++) {
            for (int z = -6; z <= 6; z++) {
                int finalX = x;
                int finalZ = z;
                Omega.getTaskManager().getMainThread().runTaskLater(new Task() {
                    @Override
                    protected void run() {
                        event.getPlayer().getWorld().sendChunkData(new ChunkPosition(finalX, finalZ), event.getPlayer());
                    }
                }, Duration.ticks(20+count));
                count++;
            }
        }
        Omega.getTaskManager().getMainThread().runTaskLater(new Task() {
            @Override
            protected void run() {
                event.getPlayer().sendPacket(new OutboundPacket(PacketType.UPDATE_VIEW_POSITION,new VarInt(event.getPlayer().getLocation().getChunk().getX()), new VarInt(event.getPlayer().getLocation().getChunk().getZ())));
            }
        }, Duration.ticks(20+count+2));

    }
}
