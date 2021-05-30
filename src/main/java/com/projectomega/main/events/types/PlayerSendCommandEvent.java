package com.projectomega.main.events.types;

import com.projectomega.main.events.Cancellable;
import com.projectomega.main.events.Event;
import com.projectomega.main.game.Player;

@Cancellable
public class PlayerSendCommandEvent extends Event {

    private final Player player;
    private final String command;
    private final String[] segments;

    public PlayerSendCommandEvent(Player player, String command) {
        this.player = player;
        this.command = command;
        segments = command.split(" ");
    }

    public String getFullCommand() {
        return command;
    }

    public Player getPlayer() {
        return player;
    }

    public String getCommand() {
        return segments[0];
    }

    public int getArgsLength() {
        return segments.length - 1;
    }

    public String getArg(int arg) {
        return segments[1 + arg];
    }
}
