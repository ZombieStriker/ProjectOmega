package example.com.testplugin;

import com.projectomega.main.game.Material;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.game.inventory.ItemStack;
import com.projectomega.main.packets.InboundPacket;
import com.projectomega.main.packets.PacketListener;

public class TestClickPacketListtener extends PacketListener {

    @Override
    public void onCall(InboundPacket packet) {
        for(int i = 0; i < packet.getDataLength(); i++) {
            System.out.println(packet.getData(i));
        }
        byte windowid = (byte) packet.getData(0);
        short slot = (short) packet.getData(1);
        Player player = Omega.getPlayerByChannel(packet.getChannel());
        if(player!=null){
            player.getInventory().setItem(slot, new ItemStack(Material.STONE));
        }

        player.setHotbarSlot(4);
        player.setHealth(5);
        player.setFood((player.getFood()+1)%20);
    }
}
