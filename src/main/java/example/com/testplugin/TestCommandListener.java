package example.com.testplugin;

import com.projectomega.main.events.*;
import com.projectomega.main.events.types.*;
import com.projectomega.main.game.ChunkPosition;
import com.projectomega.main.game.Location;
import com.projectomega.main.game.Material;
import com.projectomega.main.game.inventory.ItemStack;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.Slot;
import com.projectomega.main.packets.datatype.VarInt;
import me.nullicorn.nedit.type.NBTCompound;

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
            ItemStack is = new ItemStack(Material.STONE);
            event.getPlayer().sendPacket(new OutboundPacket(PacketType.SET_SLOT,(byte)0,(short)18,new Slot((short)1,(byte)1,(short)1,new NBTCompound())));
            if (event.getArgumentsLength() < 1) {
                event.getPlayer().sendMessage("Usage: /spawn <entityID>");
                return;
            }
            System.out.println("event args length "+event.getArgumentsLength());
            event.getPlayer().getWorld().dropItem(is,Location.at(8,17,8,event.getPlayer().getWorld()));
        }
    }
}
