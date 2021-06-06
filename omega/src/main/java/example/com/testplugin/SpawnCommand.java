package example.com.testplugin;

import com.projectomega.main.command.CommandException;
import com.projectomega.main.command.ExecutableCommand;
import com.projectomega.main.command.OmegaCommand;
import com.projectomega.main.game.ChunkPosition;
import com.projectomega.main.game.CommandSender;
import com.projectomega.main.game.Location;
import com.projectomega.main.game.Player;
import com.projectomega.main.plugin.OmegaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpawnCommand extends ExecutableCommand {

    @Override protected OmegaCommand createCommand() {
        return OmegaCommand.named("spawn").make();
    }

    @Override public void execute(@NotNull CommandSender sender, @NotNull List<String> args, @NotNull OmegaPlugin plugin) throws CommandException {
        if (!sender.isPlayer()) {
            throw new CommandException("You must be a player in order to use this command!");
        }
        Player player = (Player) sender;
        player.getWorld().sendChunkData(new ChunkPosition(0, 0), player);
        player.getWorld().sendChunkData(new ChunkPosition(-1, 0), player);
        player.getWorld().sendChunkData(new ChunkPosition(0, -1), player);
        player.getWorld().sendChunkData(new ChunkPosition(-1, -1), player);
        player.teleport(Location.at(8, 32, 8, player.getWorld()));
    }
}
