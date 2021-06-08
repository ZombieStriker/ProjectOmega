package example.com.testplugin;

import com.projectomega.main.command.CommandException;
import com.projectomega.main.command.ExecutableCommand;
import com.projectomega.main.command.OmegaCommand;
import com.projectomega.main.game.CommandSender;
import com.projectomega.main.game.Player;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.Position;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.plugin.OmegaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockDebugCommandListener extends ExecutableCommand {

    @Override protected OmegaCommand createCommand() {
        return OmegaCommand.named("debugblock").make();
    }

    @Override public void execute(@NotNull CommandSender sender, @NotNull List<String> args, @NotNull OmegaPlugin plugin) throws CommandException {
        if (!sender.isPlayer()) {
            throw new CommandException("You must be a player to use this command!");
        }
        Player player = (Player) sender;
       // player.sendPacket(new OutboundPacket(PacketType.BLOCK_CHANGE, new Position(1, 17, 1), new VarInt(32)));
        for (int x = 0; x < 32; x++) {
            for (int z = 0; z < 256; z++) {
                //   event.getPlayer().sendPacket(new OutboundPacket(PacketType.BLOCK_CHANGE, new Position(x,17,z), new VarInt(x + z*32)));
            }
        }
    }
}
