package com.projectomega.main.game.packetlogic;

import com.projectomega.main.events.EventBus;
import com.projectomega.main.events.types.PlayerJoinEvent;
import com.projectomega.main.game.ChunkPosition;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.packets.*;
import com.projectomega.main.packets.datatype.UnsignedByte;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.task.Duration;
import com.projectomega.main.utils.NBTTagUtil;
import me.nullicorn.nedit.type.NBTCompound;

import java.util.UUID;

public class SendLoginHandshake1PacketLogic implements PacketListener {

    @Override
    public void onCall(InboundPacket packet) {
        if (((int) packet.getData(3)) == 2) {
            //OutboundPacket outboundPacket = new OutboundPacket(PacketType.LOGIN_SUCCESS,new Object[]{UUID.randomUUID(),"Test"});
            String name = "Notch";
            UUID uuid = UUID.randomUUID();

            Player player = new Player(name, uuid, packet.getChannel(), (Integer) packet.getData(0), Omega.getSpawnWorld().getSpawn());
            PlayerJoinEvent joinEvent = new PlayerJoinEvent(player);
            if (!EventBus.INSTANCE.post(joinEvent).isCancelled()) {
                if (player.getProtocolVersion() < 750) {
                  /*  OutboundPacket outboundPacket = new OutboundPacket(PacketType.LOGIN_SUCCESS, new Object[]{uuid.toString(), name});
                    PacketUtil.writePacketToOutputStream(packet.getChannel(), outboundPacket);
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Outdated for 1.13-1.15
                    OutboundPacket joingame = new OutboundPacket(PacketType.JOIN_GAME_OLD, new Object[]{player.getEntityID(), new UnsignedByte((byte) 0), 0, 0l, new UnsignedByte((byte) 32), "flat", new VarInt(2), false, true});
                    PacketUtil.writePacketToOutputStream(packet.getChannel(), joingame);

                    Omega.addPlayerConnection(player);

                    player.sendPacket(new OutboundPacket(PacketType.PLAYER_POSITION_AND_LOOK_OLD, new Object[]{0d, 0d, 0d, 0f, 0f, (byte) 0, new VarInt(1)}));*/
                } else {
                    OutboundPacket outboundPacket = new OutboundPacket(PacketType.LOGIN_SUCCESS, uuid, name);
                    PacketUtil.writePacketToOutputStream(packet.getChannel(), outboundPacket);

                    Omega.addPlayerConnection(player);

                    Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                        NBTCompound dimensionCodec = NBTTagUtil.generateDimensionCodec();
                        OutboundPacket joingame = new OutboundPacket(PacketType.JOIN_GAME, player.getEntityID(), true, new UnsignedByte((byte) 0), (byte) -1, new VarInt(1), "overworld", dimensionCodec, NBTTagUtil.generateDimensionType(), "overworld", 0l, new VarInt(32), new VarInt(10), false, true, false, false);
                        player.sendPacket(joingame);
                        for (int x = -4; x <= 4; x++) {
                            for (int z = -4; z <= 4; z++) {
                                player.getWorld().sendChunkData(new ChunkPosition(x, z), player);
                            }
                        }

                        Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                            player.sendPacket(new OutboundPacket(PacketType.UPDATE_VIEW_POSITION, new VarInt(player.getLocation().getChunk().getX()), new VarInt(player.getLocation().getChunk().getZ())));
                        }, Duration.ticks(20));

                        Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                            player.sendPacket(new OutboundPacket(PacketType.PLAYER_POSITION_AND_LOOK,
                                    player.getEntity().getLocation().getX(),
                                    player.getEntity().getLocation().getY(),
                                    player.getEntity().getLocation().getZ(),
                                    player.getEntity().getLocation().getYaw(),
                                    player.getEntity().getLocation().getPitch(),
                                    (byte) 0, new VarInt(1)));
                        }, Duration.ticks(50));

                        Omega.getTaskManager().getMainThread().runTaskLater(() -> {
                            player.sendPacket(new OutboundPacket(PacketType.PLAYER_POSITION_AND_LOOK,
                                    player.getEntity().getLocation().getX(),
                                    player.getEntity().getLocation().getY(),
                                    player.getEntity().getLocation().getZ(),
                                    player.getEntity().getLocation().getYaw(),
                                    player.getEntity().getLocation().getPitch(),
                                    (byte) 0, new VarInt(1)));
                        }, Duration.ticks(20 * 10));
                    }, Duration.ticks(1));
                }
            }


            // OutboundPacket repsawn = new OutboundPacket(PacketType.RESPAWN, new Object[]{NBTTagUtil.generateDimensionTypeRegistryEntry("overworldandstuff",0),"overworld",0L,new UnsignedByte((byte) 0), new UnsignedByte((byte) 0),false,false,false});
            // PacketUtil.writePacketToOutputStream(packet.getChannel(),repsawn);


        }
    }
}
