package magmaout.furryappet.commands.scripts;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.scripts.ScriptsAPI;
import magmaout.furryappet.api.scripts.engine.FurryappetCompileException;
import magmaout.furryappet.api.scripts.engine.FurryappetScriptException;
import magmaout.furryappet.api.scripts.engine.LoadedScript;
import mchorse.mclib.commands.McCommandBase;
import mchorse.mclib.commands.utils.L10n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ExecuteCommand extends McCommandBase {
    @Override
    public String getName() {
        return "exec";
    }

    @Override
    public L10n getL10n() {
        return Furryappet.l10n;
    }
    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }
    @Override
    public String getSyntax() {
        return "";
    }

    @Override
    public void executeCommand(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] args) throws CommandException {
        ScriptsAPI scripts = (ScriptsAPI) Furryappet.APIManager.getAPI("scripts");
        LoadedScript loadedScript = scripts.getLoadedScript(args[0]);
        String function = args.length > 1 ? args[1] : "main";
        if(loadedScript == null) {
            FurryappetCompileException compileError = scripts.getErrorManager().getCompileError(args[0]);
            if(compileError == null) {
                throw new CommandException("furryappet.commands.scripts.exec.not_found");
            }

            throw new CommandException(compileError.getMessage() + "\nCheck furryappet menu to get more information");
        }
        try {
            loadedScript.invokeFunction(function);
        } catch (FurryappetScriptException e) {
            throw new CommandException(e.getMessage());
        }

    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length == 0) {
            return new ArrayList<>(Furryappet.APIManager.getScriptsAPI().getScriptStorage().getDataNames());
        }
        if(args.length == 1) {
            return new ArrayList<>(Furryappet.APIManager.getScriptsAPI().getLoadedScript(args[0]).getFunctions());
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
