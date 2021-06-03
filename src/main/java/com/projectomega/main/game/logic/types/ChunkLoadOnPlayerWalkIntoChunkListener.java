package com.projectomega.main.game.logic.types;

import com.projectomega.main.events.EventListener;
import com.projectomega.main.events.types.PlayerMoveEvent;
import com.projectomega.main.game.Chunk;
import com.projectomega.main.game.ChunkPosition;
import com.projectomega.main.game.Omega;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.task.Duration;
import com.projectomega.main.task.Task;

public class ChunkLoadOnPlayerWalkIntoChunkListener {

    @EventListener
    public void onPlayerMove(PlayerMoveEvent event){
        Chunk newLocationChunk = event.getNewLocation().getChunk();
        Chunk playerChunk = event.getPlayer().getLocation().getChunk();
        if(newLocationChunk.getX() != playerChunk.getX()
        || newLocationChunk.getZ() != playerChunk.getZ()
        ){
            for(int x = -5; x <= 5; x++) {
                for(int z = -5; z <= 5; z++) {
                    event.getPlayer().getWorld().sendChunkData(new ChunkPosition(newLocationChunk.getX()+x, newLocationChunk.getZ()+z), event.getPlayer());
                }
            }
            Omega.getTaskManager().getMainThread().runTaskLater(new Task() {
                @Override
                protected void run() {
                    event.getPlayer().sendPacket(new OutboundPacket(PacketType.UPDATE_VIEW_POSITION,new VarInt(newLocationChunk.getX()), new VarInt(newLocationChunk.getZ())));
                }
            }, Duration.ticks(20));
        }
    }
}
