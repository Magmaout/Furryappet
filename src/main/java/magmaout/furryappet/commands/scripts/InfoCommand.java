package magmaout.furryappet.commands.scripts;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.scripts.ScriptsAPI;
import magmaout.furryappet.api.scripts.engine.FurryappetCompileException;
import magmaout.furryappet.api.scripts.engine.LoadedScript;
import mchorse.mclib.commands.McCommandBase;
import mchorse.mclib.commands.utils.L10n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class InfoCommand extends McCommandBase {
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
        ScriptsAPI scriptsAPI = (ScriptsAPI) Furryappet.furryappetAPIManager.getAPI("scripts");
        sender.sendMessage(new TextComponentString("Scripts info: "));
        for (String script : scriptsAPI.getScriptStorage().getDataNames()) {
            LoadedScript loadedScript = scriptsAPI.getLoadedScript(script);
            if(loadedScript == null) {
                FurryappetCompileException compileError = scriptsAPI.getErrorManager().getCompileError(script);
                sender.sendMessage(new TextComponentString("  " + script + ": " + (compileError == null ? "unloaded" : "load failed :<")));
                continue;
            }
            sender.sendMessage(new TextComponentString("  " + script + ": yaaaaay :3"));
        }
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "info";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length > 1) {
            return super.getTabCompletions(server, sender, args, targetPos);
        }
        return new ArrayList<>(Furryappet.furryappetAPIManager.getScriptsAPI().getScriptStorage().getDataNames());
    }
}
