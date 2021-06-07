package com.projectomega.main.game.chunk;

import com.projectomega.main.game.Chunk;
import com.projectomega.main.game.Material;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.task.Duration;
import com.projectomega.main.task.Task;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class BlockByBlockBuilder {

    private static final HashMap<UUID, LinkedList<BlockByBlockData>> chunks = new HashMap<>();
    private static final int MAX_BLOCKS_TO_SEND_PER_TICK = 500;

    public static void init() {
        Omega.getTaskManager().getMainThread().runTaskRepeatedly(new Task() {
            @Override
            protected void run() {
                for (Player player : Omega.getPlayers()) {
                    if (chunks.containsKey(player.getUuid())) {
                        BlockByBlockData data = null;
                        double distance = Double.MAX_VALUE;
                        byte prioritylevel = 61;
                        Chunk playerChunk = player.getLocation().getChunk();
                        for (BlockByBlockData blockbyblockdata : chunks.get(player.getUuid())) {
                            if (blockbyblockdata.getChunk() == playerChunk) {
                                data = blockbyblockdata;
                                distance=0;
                                prioritylevel=data.getPrioritylevel();
                                break;
                            }
                            int d = Math.abs(playerChunk.getX() - blockbyblockdata.getChunk().getX()) + Math.abs(playerChunk.getZ() - blockbyblockdata.getChunk().getZ());

                            if (prioritylevel > blockbyblockdata.getPrioritylevel()) {
                                prioritylevel = blockbyblockdata.getPrioritylevel();
                                distance = d;
                                data = blockbyblockdata;
                            }
                            if (data == null || distance > d) {
                                distance = d;
                                data = blockbyblockdata;
                            }
                        }
                        if (data != null) {
                            int sentBlocks = 0;
                            breakloop:
                            for (int y = Math.max(0, player.getLocation().getBlockY() - 1); y < 256; y++) {
                                for (int x = 0; x < 16; x++) {
                                    for (int z = 0; z < 16; z++) {
                                        if (data.timesSentPacket(x, y, z) <= data.getPrioritylevel()) {
                                            if (data.getChunk().getBlockAtChunkRelative(x, y, z).getType() == Material.AIR) {
                                                data.setAirAt(x, y, z);
                                                continue;
                                            }
                                            data.sendPacket(player, x, y, z);
                                            sentBlocks++;
                                        }
                                        if (sentBlocks >= MAX_BLOCKS_TO_SEND_PER_TICK)
                                            break breakloop;
                                    }
                                }
                            }
                            if (sentBlocks < MAX_BLOCKS_TO_SEND_PER_TICK) {
                                breakloop:
                                for (int y = 0; y < player.getLocation().getBlockY() - 1; y++) {
                                    for (int x = 0; x < 16; x++) {
                                        for (int z = 0; z < 16; z++) {
                                            if (data.timesSentPacket(x, y, z) <= data.getPrioritylevel()) {
                                                if (data.getChunk().getBlockAtChunkRelative(x, y, z).getType() == Material.AIR) {
                                                    data.setAirAt(x, y, z);
                                                    continue;
                                                }
                                                data.sendPacket(player, x, y, z);
                                                sentBlocks++;
                                           }
                                            if (sentBlocks >= MAX_BLOCKS_TO_SEND_PER_TICK)
                                                break breakloop;
                                        }
                                    }
                                }
                                if (sentBlocks < MAX_BLOCKS_TO_SEND_PER_TICK) {
                                    if(data.getPrioritylevel() < BlockByBlockData.TIMES_TO_SEND){
                                        data.setPrioritylevel((byte) (data.getPrioritylevel()+1));
                                    }else {
                                        chunks.get(player.getUuid()).remove(data);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }, Duration.ticks(1), Duration.ticks(1));
    }

    public static void buildChunkForPlayer(Player player, Chunk chunk) {
        if (chunks.containsKey(player.getUuid())) {
            for (BlockByBlockData d : chunks.get(player.getUuid())) {
                if (d.getChunk() == chunk) {
                    return;
                }
            }
        } else {
            chunks.put(player.getUuid(), new LinkedList<>());
        }
        BlockByBlockData data = new BlockByBlockData(chunk);
        LinkedList<BlockByBlockData> blocks = chunks.get(player.getUuid());
        blocks.add(data);
    }
}
