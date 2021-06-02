package com.projectomega.main.events.types;

import com.projectomega.main.events.Cancellable;
import com.projectomega.main.events.Event;
import com.projectomega.main.game.Player;

import java.util.*;

@Cancellable
public class PlayerSendCommandEvent extends Event {

    private final Player player;
    private final String command;
    private final String label;
    private final String[] args;

    public PlayerSendCommandEvent(Player player, String command) {
        this.player = player;
        this.command = command;
        String[] split = command.split("\\s+");
        this.label = split[0];
        this.args = Arrays.copyOfRange(split, 1, split.length);
    }

    public String getFullCommand() {
        return command;
    }

    public Player getPlayer() {
        return player;
    }

    public String getCommand() {
        return label;
    }

    public int getArgumentsLength() {
        return args.length;
    }

    public String getArgument(int arg) {
        return args[arg];
    }

    public String[] getArguments() {
        return args;
    }
}
