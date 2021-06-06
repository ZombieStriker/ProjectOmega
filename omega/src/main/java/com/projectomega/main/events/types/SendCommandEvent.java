package com.projectomega.main.events.types;

import com.projectomega.main.command.OmegaCommand;
import com.projectomega.main.events.Cancellable;
import com.projectomega.main.events.Event;
import com.projectomega.main.game.CommandSender;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Cancellable
public class SendCommandEvent extends Event {

    private final CommandSender sender;
    private final OmegaCommand command;
    private final List<String> args;
    private final String text;

    public SendCommandEvent(@NonNull CommandSender sender,
                            @NotNull OmegaCommand command,
                            @NotNull List<String> args,
                            @NotNull String text) {
        this.sender = sender;
        this.command = command;
        this.args = args;
        this.text = text;
    }

    public CommandSender getSender() {
        return sender;
    }

    public OmegaCommand getCommand() {
        return command;
    }

    public List<String> getArgs() {
        return args;
    }

    public String getText() {
        return text;
    }
}
