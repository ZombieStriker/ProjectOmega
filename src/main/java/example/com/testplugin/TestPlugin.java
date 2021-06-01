package example.com.testplugin;

import com.projectomega.main.events.EventBus;
import com.projectomega.main.packets.PacketManager;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.plugin.OmegaPlugin;

public class TestPlugin extends OmegaPlugin {

    @Override
    public void onEnable() {
        EventBus.INSTANCE.register(new TestListener());
        EventBus.INSTANCE.register(new TestCommandListener());
        PacketManager.registerPacketListener(PacketType.CLICK_WINDOW, new TestClickPacketListtener());
    }

    @Override
    public void onDisable() {

    }
}
