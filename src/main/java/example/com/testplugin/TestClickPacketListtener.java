package example.com.testplugin;

import com.projectomega.main.game.*;
import com.projectomega.main.game.inventory.ItemStack;
import com.projectomega.main.game.sound.Sound;
import com.projectomega.main.game.sound.SoundCategory;
import com.projectomega.main.game.utils.ExperiencePointsUtil;
import com.projectomega.main.packets.InboundPacket;
import com.projectomega.main.packets.PacketListener;

public class TestClickPacketListtener implements PacketListener {

    @Override
    public void onCall(InboundPacket packet) {
        byte windowid = (byte) packet.getData(0);
        short slot = (short) packet.getData(1);
        Player player = Omega.getPlayerByChannel(packet.getChannel());
        if (player != null) {
            player.getInventory().setItem(slot, new ItemStack(Material.STONE));
        }

        player.setHotbarSlot(4);
        player.setXP(0.5f, 69, ExperiencePointsUtil.getTotalXP(69));

        World world = Omega.getWorlds().get(0);
        player.playSound(Sound.SOUND_TEST, SoundCategory.MASTER, Location.at(0, 0, 0, player.getWorld()), 1f, 1f);
        player.sendTitle("Test", "Message", 20, 60, 20);
        world.sendChunkData(new ChunkPosition(0, 0), player);
        //world.sendChunkData(new ChunkPosition(1,2),player);

        //OutboundPacket spawnentity = new OutboundPacket(PacketType.SPAWN_ENTITY, new Object[]{new VarInt(12), UUID.randomUUID(),new VarInt(EntityType.CREEPER.getTypeID()),5d,0d,0d,pitch,yaw,data,(short)0,(short)0,(short)0});
        //player.sendPacket(spawnentity);

        //OutboundPacket spawnLivingentity = new OutboundPacket(PacketType.SPAWN_LIVING_ENTITY, new Object[]{new VarInt(13), UUID.randomUUID(),new VarInt(EntityType.CREEPER.getTypeID()),5d,0d,0d,pitch,yaw,headpitch,(short)0,(short)0,(short)0});
        //player.sendPacket(spawnLivingentity);
        // player.setHealth(5f);
        // player.setFood((player.getFood()+1)%20);
    }
}
