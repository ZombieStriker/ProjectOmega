package example.com.testplugin;

import com.projectomega.main.events.EventBus;
import com.projectomega.main.game.Omega;
import com.projectomega.main.manipulator.Chunk;
import com.projectomega.main.manipulator.ChunkPos;
import com.projectomega.main.manipulator.Region;
import com.projectomega.main.manipulator.RegionIO;
import com.projectomega.main.packets.PacketManager;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.plugin.OmegaPlugin;

import java.io.File;
import java.util.Map;

public class TestPlugin extends OmegaPlugin {

    @Override public void onEnable() {
        EventBus.INSTANCE.register(new TestListener());
        EventBus.INSTANCE.register(new BlockDebugCommandListener());
        PacketManager.registerPacketListener(PacketType.CLICK_WINDOW, new TestClickPacketListtener());
        registerCommand(new TestCommand());
        registerCommand(new SpawnCommand());
        registerCommand(new SpawnEntityCommand());

        System.out.println(Omega.getWorlds().get(0).getWorldFolder().getPath());
        for(File f : new File(Omega.getWorlds().get(0).getWorldFolder(),"region").listFiles()){
            Region region = RegionIO.readRegion(f);
            for(Map.Entry<ChunkPos, Chunk> k : region.entrySet()){
                System.out.println(k.getValue().getCompound().toString());
                return;
            }
        }
    }

    @Override
    public void onDisable() {

    }
}
