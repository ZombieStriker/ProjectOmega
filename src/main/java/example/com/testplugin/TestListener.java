package example.com.testplugin;

import com.projectomega.main.events.EventListener;
import com.projectomega.main.events.types.PlayerJoinEvent;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.chat.BossBar;
import com.projectomega.main.game.chat.BossBarColor;
import com.projectomega.main.game.chat.BossBarDivisions;

public class TestListener {

    @EventListener
    public void onPlayerJoin(PlayerJoinEvent event) {
        BossBar bossBar = Omega.createBossBar("Test", 1.0f, BossBarColor.PURPLE, BossBarDivisions.SIX_NOTCHES);
        System.out.println("Sending boss bar");
        bossBar.sendBossbarPacketToPlayer(event.getPlayer());
    }
}
