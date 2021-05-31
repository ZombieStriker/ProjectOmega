package me.zombie_striker.mork;

import com.projectomega.main.events.EventBus;
import com.projectomega.main.game.Player;
import com.projectomega.main.plugins.OmegaPlugin;
import me.zombie_striker.mork.game.GameState;

import java.util.HashMap;

public class MorkPlugin extends OmegaPlugin {
    public HashMap<Player, GameState> games = new HashMap<Player, GameState>();

    @Override
    public String getName() {
        return "Mork";
    }

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public void onEnable() {
        EventBus.INSTANCE.register(new MorkChatListener(this));
    }

    @Override
    public void onDisable() {

    }
}
