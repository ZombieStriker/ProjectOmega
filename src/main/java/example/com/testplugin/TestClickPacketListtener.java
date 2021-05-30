package example.com.testplugin;

import com.projectomega.main.game.Material;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.game.entity.EntityType;
import com.projectomega.main.game.inventory.ItemStack;
import com.projectomega.main.game.utils.ExperiencePointsUtil;
import com.projectomega.main.packets.InboundPacket;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketListener;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;

import java.util.UUID;

public class TestClickPacketListtener implements PacketListener {

    @Override
    public void onCall(InboundPacket packet) {
        byte windowid = (byte) packet.getData(0);
        short slot = (short) packet.getData(1);
        Player player = Omega.getPlayerByChannel(packet.getChannel());
        if(player!=null){
            player.getInventory().setItem(slot, new ItemStack(Material.STONE));
        }

        player.setHotbarSlot(4);
        player.setXP(0.5f,69, ExperiencePointsUtil.getTotalXP(69));

        byte pitch = (byte) (256*0.0);
        byte yaw = (byte) (256*0.0);
        byte headpitch = (byte) (256*0.0);
        int data = 0;

        OutboundPacket spawnentity = new OutboundPacket(PacketType.SPAWN_ENTITY, new Object[]{new VarInt(12), UUID.randomUUID(),new VarInt(EntityType.CREEPER.getTypeID()),5d,0d,0d,pitch,yaw,data,(short)0,(short)0,(short)0});
        player.sendPacket(spawnentity);

        OutboundPacket spawnLivingentity = new OutboundPacket(PacketType.SPAWN_LIVING_ENTITY, new Object[]{new VarInt(13), UUID.randomUUID(),new VarInt(EntityType.CREEPER.getTypeID()),5d,0d,0d,pitch,yaw,headpitch,(short)0,(short)0,(short)0});
        player.sendPacket(spawnLivingentity);
        // player.setHealth(5f);
       // player.setFood((player.getFood()+1)%20);
    }
}
