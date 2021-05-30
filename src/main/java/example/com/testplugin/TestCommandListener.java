package example.com.testplugin;

import com.projectomega.main.events.EventListener;
import com.projectomega.main.events.types.PlayerChatEvent;
import com.projectomega.main.events.types.PlayerSendCommandEvent;
import com.projectomega.main.game.entity.EntityType;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;

import java.util.UUID;

public class TestCommandListener extends EventListener<PlayerSendCommandEvent> {

    @Override
    public void onCall(PlayerSendCommandEvent event) {
        if(event.getCommand().equals("/spawn")){
            if(event.getArgsLength() < 1){
                event.getPlayer().sendMessage("Usage: /spawn <entityID>");
                return;
            }
            int id = Integer.parseInt(event.getArg(0));

            byte pitch = (byte) (256*0.0);
            byte yaw = (byte) (256*0.0);
            byte headpitch = (byte) (256*0.0);
            int data = 0;

            OutboundPacket spawnentity = new OutboundPacket(PacketType.SPAWN_ENTITY, new Object[]{new VarInt(12), UUID.randomUUID(),new VarInt(id),5d,0d,0d,pitch,yaw,data,(short)0,(short)0,(short)0});
            event.getPlayer().sendPacket(spawnentity);

            OutboundPacket spawnLivingentity = new OutboundPacket(PacketType.SPAWN_LIVING_ENTITY, new Object[]{new VarInt(13), UUID.randomUUID(),new VarInt(id),5d,0d,0d,pitch,yaw,headpitch,(short)0,(short)0,(short)0});
            event.getPlayer().sendPacket(spawnLivingentity);
        }
    }
}
