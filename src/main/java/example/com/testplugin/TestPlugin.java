package example.com.testplugin;

import com.projectomega.main.events.EventManager;
import com.projectomega.main.events.types.PlayerChatEvent;
import com.projectomega.main.events.types.PlayerJoinEvent;
import com.projectomega.main.events.types.PlayerSendCommandEvent;
import com.projectomega.main.packets.PacketManager;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.plugins.OmegaPlugin;

public class TestPlugin extends OmegaPlugin {
    @Override
    public String getName() {
        return "Test Plugin";
    }

    @Override
    public String getVersion() {
        return "v1.0.0";
    }

    @Override
    public void onEnable() {
        EventManager.registerEventListener(PlayerJoinEvent.class, new TestListener());
        EventManager.registerEventListener(PlayerSendCommandEvent.class, new TestCommandListener());
        PacketManager.registerPacketListener(PacketType.CLICK_WINDOW,new TestClickPacketListtener());
    }

    @Override
    public void onDisable() {

    }
}
