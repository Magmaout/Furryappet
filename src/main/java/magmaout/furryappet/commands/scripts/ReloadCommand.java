package magmaout.furryappet.commands.scripts;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.scripts.ScriptsAPI;
import mchorse.mclib.commands.McCommandBase;
import mchorse.mclib.commands.utils.L10n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends McCommandBase {
    @Override
    public L10n getL10n() {
        return Furryappet.l10n;
    }

    @Override
    public String getSyntax() {
        return "";
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        ScriptsAPI scripts = (ScriptsAPI) Furryappet.APIManager.getAPI("scripts");
        if(!scripts.loadScript(args[0])) {
            throw new CommandException(scripts.getErrorManager().getCompileError(args[0]).getMessage().replace("\r", ""));
        }
        sender.sendMessage(new TextComponentTranslation("furryappet.commands.reload.success", args[0]));
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "reload <script>";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length > 1) {
            return super.getTabCompletions(server, sender, args, targetPos);
        }
        return new ArrayList<>(Furryappet.APIManager.getScriptsAPI().getScriptStorage().getDataNames());
    }
}
