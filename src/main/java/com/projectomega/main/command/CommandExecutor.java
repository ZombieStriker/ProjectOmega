package com.projectomega.main.command;

import com.projectomega.main.game.CommandSender;
import com.projectomega.main.plugin.OmegaPlugin;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CommandExecutor {

    void execute(@NotNull CommandSender sender, @NotNull List<String> args, @NotNull OmegaPlugin plugin) throws CommandException;

    default CommandExecutor compose(@NonNull CommandExecutor other) {
        return (sender, args, plugin) -> {
            execute(sender, args, plugin);
            other.execute(sender, args, plugin);
        };
    }

}
