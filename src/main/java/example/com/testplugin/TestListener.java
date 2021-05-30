package example.com.testplugin;

import com.projectomega.main.events.EventListener;
import com.projectomega.main.events.types.PlayerJoinEvent;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.chat.BossBar;
import com.projectomega.main.game.chat.BossBarColor;
import com.projectomega.main.game.chat.BossBarDivisions;
import com.projectomega.main.game.inventory.Inventory;
import com.projectomega.main.game.inventory.InventoryType;

public class TestListener extends EventListener<PlayerJoinEvent> {

    @Override
    public void onCall(PlayerJoinEvent event) {
        BossBar bossBar = Omega.createBossBar("Test",1.0f, BossBarColor.BLUE, BossBarDivisions.NONE);
        System.out.println("Sending boss bar");
        bossBar.sendBossbarPacketToPlayer(event.getPlayer());

       // Inventory newinv = new Inventory(InventoryType.GENERIC_9x1,(byte)1);
       // event.getPlayer().openInventory(newinv);
    }
}
