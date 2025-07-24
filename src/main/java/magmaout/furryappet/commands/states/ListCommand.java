package magmaout.furryappet.commands.states;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.states.States;
import magmaout.furryappet.api.states.StatesAPI;
import mchorse.mclib.commands.McCommandBase;
import mchorse.mclib.commands.utils.L10n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class ListCommand extends McCommandBase {
    @Override
    public L10n getL10n() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "";
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args[0].equals("~")) {
            printStates("~", Furryappet.furryappetAPIManager.getStatesAPI().getServerStates(), sender);
            return;
        }
        EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(args[0]);
        printStates(player.getName(), Furryappet.furryappetAPIManager.getStatesAPI().getPlayerStates(player), sender);
    }
    private void printStates(String name, States states, ICommandSender sender) {
        StringBuilder builder = new StringBuilder("States for " + name + ":\n");
        for (String key : states.getKeys()) {
            builder.append("   ").append(key).append(": ").append(states.getUnknownState(key)).append(" (").append(states.getStateType(key).name()).append(")\n");
        }
        sender.sendMessage(new TextComponentString(builder.toString()));
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return Arrays.asList(server.getPlayerList().getOnlinePlayerNames());
    }
}
