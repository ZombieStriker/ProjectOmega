package example.com.testplugin;

import com.projectomega.main.command.CommandException;
import com.projectomega.main.command.ExecutableCommand;
import com.projectomega.main.command.OmegaCommand;
import com.projectomega.main.game.CommandSender;
import com.projectomega.main.game.Location;
import com.projectomega.main.game.Material;
import com.projectomega.main.game.Player;
import com.projectomega.main.game.inventory.ItemStack;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.Slot;
import com.projectomega.main.plugin.OmegaPlugin;
import me.nullicorn.nedit.type.NBTCompound;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpawnEntityCommand extends ExecutableCommand {

    @Override protected OmegaCommand createCommand() {
        return OmegaCommand.named("spawnentity").make();
    }

    @Override public void execute(@NotNull CommandSender sender, @NotNull List<String> args, @NotNull OmegaPlugin plugin) throws CommandException {
        if (!sender.isPlayer()) {
            throw new CommandException("You must be a player in order to use this command!");
        }
        Player player = (Player) sender;
        ItemStack is = new ItemStack(Material.STONE);
        player.sendPacket(new OutboundPacket(PacketType.SET_SLOT, (byte) 0, (short) 18, new Slot((short) 1, (byte) 1, (short) 1, new NBTCompound())));
        if (args.size() < 1) {
            throw new CommandException("Usage: /spawn <entityID>");
        }
        player.getWorld().dropItem(is, Location.at(8, 17, 8, player.getWorld()));
    }
}
