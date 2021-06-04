package com.projectomega.main.command;

import com.projectomega.main.events.EventBus;
import com.projectomega.main.events.types.SendCommandEvent;
import com.projectomega.main.game.CommandSender;
import com.projectomega.main.plugin.OmegaPlugin;
import lombok.NonNull;

import java.util.*;
import java.util.regex.Pattern;

public final class OmegaCommandHandler {

    private static final Pattern BY_SPACE = Pattern.compile(" ");
    private final Map<String, OmegaCommand> byAlias = new HashMap<>();
    private final Map<String, OmegaCommand> commands = new HashMap<>();

    public void register(@NonNull OmegaPlugin plugin, @NonNull ExecutableCommand command) {
        OmegaCommand cmd = command.getCommand();
        if (cmd.getExecutor() == OmegaCommand.VOID)
            cmd.setExecutor(command);
        if (cmd.getPlugin() == null)
            cmd.setPlugin(plugin);
        String namespace = plugin.getPluginMeta().getName().toLowerCase();
        commands.put(namespace + ":" + cmd.getName(), cmd);

        // if an alias is registered (presumably by another plugin) then give
        // our command higher priority and override it.
        if (!commands.containsKey(cmd.getName()) && byAlias.containsKey(cmd.getName())) {
            commands.put(cmd.getName(), cmd);
        }
        for (String alias : cmd.getAliases()) {
            byAlias.put(namespace + ":" + alias, cmd);
            if (!commands.containsKey(alias)) {
                // as long as we're not overriding a main command, we
                // can override aliases.
                byAlias.put(alias, cmd);
            }
        }
    }

    public void execute(@NonNull CommandSender sender, @NonNull String text) throws CommandException {
        String[] argsArray = BY_SPACE.split(text);
        if (argsArray.length == 0)
            throw new IllegalArgumentException("args cannot be empty!");
        LinkedList<String> args = new LinkedList<>();
        Collections.addAll(args, argsArray);
        String commandName = args.poll();
        OmegaCommand command = byAlias.get(commandName);
        if (command == null) {
            throw new CommandException("Invalid command. Run /help for a list of commands.");
        }
        SendCommandEvent event = new SendCommandEvent(sender, command, args, text);
        if (!EventBus.INSTANCE.post(event).isCancelled()) {
            command.getExecutor().execute(sender, new ArrayList<>(args), command.getPlugin());
        }
    }

}
