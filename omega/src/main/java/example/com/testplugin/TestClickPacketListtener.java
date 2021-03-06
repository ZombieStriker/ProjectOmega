package example.com.testplugin;

import com.projectomega.main.game.*;
import com.projectomega.main.game.inventory.ItemStack;
import com.projectomega.main.game.sound.Sound;
import com.projectomega.main.game.sound.SoundCategory;
import com.projectomega.main.utils.ExperiencePointsUtil;
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
        player.teleport(Location.at(-5,200,-5,world));
        player.sendTitle("Title", "Subtitle", 20, 60, 20);

        player.getInventory().setItem(10, new ItemStack(Material.STONE));
        player.getInventory().setItem(11, new ItemStack(Material.IRON_BARS));
        player.getInventory().setItem(12, new ItemStack(Material.DROPPER));
        player.getInventory().setItem(13, new ItemStack(Material.LILAC));
        player.getInventory().setItem(14, new ItemStack(Material.LILY_PAD));
        player.getInventory().setItem(15, new ItemStack(Material.DIAMOND_AXE));
        player.getInventory().setItem(16, new ItemStack(Material.MAP));
        player.getInventory().setItem(17, new ItemStack(Material.GILDED_BLACKSTONE));

       // Entity droppeditem = world.dropItem(new ItemStack(Material.STONE),Location.at(0,0,0,world));
       // droppeditem.setCustomName("testing");
       // droppeditem.setCustomNameVisable(true);
        /* Entity creeper = world.spawnEntity(EntityType.PIG, Location.at(0,0,0,world));
        creeper.setCustomName("Pig-le Rick");
        creeper.setCustomNameVisable(true);*/


        //OutboundPacket spawnentity = new OutboundPacket(PacketType.SPAWN_ENTITY, new Object[]{new VarInt(12), UUID.randomUUID(),new VarInt(EntityType.CREEPER.getTypeID()),5d,0d,0d,pitch,yaw,data,(short)0,(short)0,(short)0});
        //player.sendPacket(spawnentity);

        //OutboundPacket spawnLivingentity = new OutboundPacket(PacketType.SPAWN_LIVING_ENTITY, new Object[]{new VarInt(13), UUID.randomUUID(),new VarInt(EntityType.CREEPER.getTypeID()),5d,0d,0d,pitch,yaw,headpitch,(short)0,(short)0,(short)0});
        //player.sendPacket(spawnLivingentity);
        // player.setHealth(5f);
        // player.setFood((player.getFood()+1)%20);
    }
}
