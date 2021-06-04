package example.com.testplugin;

import com.projectomega.main.command.CommandException;
import com.projectomega.main.command.ExecutableCommand;
import com.projectomega.main.command.OmegaCommand;
import com.projectomega.main.command.permission.Permission;
import com.projectomega.main.game.CommandSender;
import com.projectomega.main.plugin.OmegaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestCommand extends ExecutableCommand {

    @Override protected OmegaCommand createCommand() {
        return OmegaCommand.named("test")
                .description("Run a test command")
                .usage("/test <number>")
                .executor(this) // optional
                .permission(Permission.everyone("testplugin.test"))
                .addAlias("toast")
                .make();
    }

    @Override public void execute(@NotNull CommandSender sender, @NotNull List<String> args, @NotNull OmegaPlugin plugin) throws CommandException {
        if (args.isEmpty()) {
            throw new CommandException("Invalid command usage!");
        }
        int number = Integer.parseInt(args.get(0));
        for (int i = 0; i < number; i++) {
            sender.sendMessage("Test &a#" + i);
        }
    }
}
