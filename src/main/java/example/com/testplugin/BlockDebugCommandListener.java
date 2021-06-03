package example.com.testplugin;

import com.projectomega.main.events.EventListener;
import com.projectomega.main.events.types.PlayerSendCommandEvent;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.PacketUtil;
import com.projectomega.main.packets.datatype.Position;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.utils.ByteUtils;

public class BlockDebugCommandListener {

    @EventListener
    public void onCommand(PlayerSendCommandEvent event){
        if(event.getCommand().equalsIgnoreCase("/debugblock")){
            event.getPlayer().sendPacket(new OutboundPacket(PacketType.BLOCK_CHANGE, new Position(1,17,1), new VarInt(1*32)));
            for(int x = 0; x < 32; x++){
                for(int z = 0; z < 256; z++){
                 //   event.getPlayer().sendPacket(new OutboundPacket(PacketType.BLOCK_CHANGE, new Position(x,17,z), new VarInt(x + z*32)));
                }
            }
        }
    }
}
