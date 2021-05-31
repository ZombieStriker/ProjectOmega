package me.zombie_striker.mork;

import com.projectomega.main.events.EventListener;
import com.projectomega.main.events.types.PlayerSendCommandEvent;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;
import me.zombie_striker.mork.game.GameState;

import java.util.UUID;

public class MorkChatListener {

    private MorkPlugin plugin;

    public MorkChatListener(MorkPlugin morkPlugin) {
        this.plugin = morkPlugin;
    }

    @EventListener
    public void onPlayerSendCommand(PlayerSendCommandEvent event) {
        if (event.getCommand().equalsIgnoreCase("/mork")) {
            if (event.getArgsLength() < 1) {
                event.getPlayer().sendMessage("Usage: /mork start - Starts the game for the player");
                event.getPlayer().sendMessage("Usage: /mork <action> - Performs action");
                event.getPlayer().sendMessage("Usage: /mork listactions - Lists all actions");
                return;
            }
            String arg = event.getArg(0);
            if (arg.equalsIgnoreCase("start")) {
                plugin.games.put(event.getPlayer(), new GameState());
                return;
            }
            GameState state = plugin.games.get(event.getPlayer());
            if (state == null) {
                event.getPlayer().sendMessage("You have not started a game! Use /mork start");
                return;
            }
            if (arg.equalsIgnoreCase("listactions")) {
                event.getPlayer().sendMessage("Actions:");
                event.getPlayer().sendMessage("-up  - Go up");
                event.getPlayer().sendMessage("-down  - Go down");
                event.getPlayer().sendMessage("-left  - Go left");
                event.getPlayer().sendMessage("-right  - Go right");
                event.getPlayer().sendMessage("-view  - View map");
            }

            if (arg.equalsIgnoreCase("up")) {
                state.goUp();
            }
            if (arg.equalsIgnoreCase("down")) {
                state.goDown();
            }
            if (arg.equalsIgnoreCase("left")) {
                state.goLeft();
            }
            if (arg.equalsIgnoreCase("right")) {
                state.goRight();
            }
            if (arg.equalsIgnoreCase("view")) {
                for (int y = 0; y < 20; y++) {
                    StringBuilder sb = new StringBuilder();
                    for (int x = 0; x < 20; x++) {
                        if (x == state.getPlayerx() && y == state.getPlayery()) {
                            sb.append('&');
                        } else {
                            sb.append(state.getTileAt(x, y));
                        }
                    }
                }
            }

        }
    }
}
