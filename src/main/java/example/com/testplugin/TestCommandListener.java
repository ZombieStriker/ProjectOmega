package example.com.testplugin;

import com.projectomega.main.events.*;
import com.projectomega.main.events.types.*;
import com.projectomega.main.game.ChunkPosition;
import com.projectomega.main.game.Location;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;

import java.util.UUID;

public class TestCommandListener {

    @EventListener(priority = Priority.LOWEST)
    public void onPlayerChat3(PlayerChatEvent event) {
        System.out.println("Chat3: "+event.getMessage());
    }

    @EventListener(priority = Priority.NORMAL)
    public void onPlayerChat(PlayerChatEvent event) {
        System.out.println("Chat2: "+event.getMessage());
    }

    @EventListener(priority = Priority.LOW)
    public void onPlayerChat2(PlayerChatEvent event) {
        System.out.println("Chat: "+event.getMessage());
    }

    @EventListener
    public void onPlayerSendCommand(PlayerSendCommandEvent event) {
        if(event.getCommand().equalsIgnoreCase("/spawn")){
            event.getPlayer().getWorld().sendChunkData(new ChunkPosition(0, 0), event.getPlayer());
            event.getPlayer().getWorld().sendChunkData(new ChunkPosition(-1,0),event.getPlayer());
            event.getPlayer().getWorld().sendChunkData(new ChunkPosition(0,-1),event.getPlayer());
            event.getPlayer().getWorld().sendChunkData(new ChunkPosition(-1,-1),event.getPlayer());
            event.getPlayer().teleport(Location.at(8,32,8,event.getPlayer().getWorld()));
            return;
        }
        if (event.getCommand().equalsIgnoreCase("/spawnentity")) {
            if (event.getArgumentsLength() < 1) {
                event.getPlayer().sendMessage("Usage: /spawn <entityID>");
                return;
            }
            int id = Integer.parseInt(event.getArgument(0));

            byte pitch = (byte) (256 * 0.0);
            byte yaw = (byte) (256 * 0.0);
            byte headpitch = (byte) (256 * 0.0);
            int data = 0;

            OutboundPacket spawnentity = new OutboundPacket(PacketType.SPAWN_ENTITY, new VarInt(12), UUID.randomUUID(), new VarInt(id), 5d, 0d, 0d, pitch, yaw, data, (short) 0, (short) 0, (short) 0);
            event.getPlayer().sendPacket(spawnentity);

            OutboundPacket spawnLivingentity = new OutboundPacket(PacketType.SPAWN_LIVING_ENTITY, new VarInt(13), UUID.randomUUID(), new VarInt(id), 5d, 0d, 0d, pitch, yaw, headpitch, (short) 0, (short) 0, (short) 0);
            event.getPlayer().sendPacket(spawnLivingentity);
        }
    }
}
