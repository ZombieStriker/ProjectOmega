package com.projectomega.main.command;

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
        if (!commands.containsKey(cmd.getName()) && byAlias.containsKey(cmd.getName())) {
            commands.put(cmd.getName(), cmd);
        }
        for (String alias : cmd.getAliases()) {
            byAlias.put(namespace + ":" + alias, cmd);
            if (!commands.containsKey(alias)) {
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
        // TODO: Post event and check for cancellation
        command.getExecutor().execute(sender, new ArrayList<>(args), command.getPlugin());
    }

}
