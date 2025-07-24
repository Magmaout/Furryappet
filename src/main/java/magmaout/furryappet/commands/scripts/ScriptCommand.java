package magmaout.furryappet.commands.scripts;

import magmaout.furryappet.Furryappet;
import mchorse.mclib.commands.SubCommandBase;
import mchorse.mclib.commands.utils.L10n;
import net.minecraft.command.ICommandSender;

public class ScriptCommand extends SubCommandBase {
    public ScriptCommand() {
        this.add(new ExecuteCommand());
        this.add(new ReloadCommand());
        this.add(new UnloadCommand());
        this.add(new InfoCommand());
    }
    @Override
    public L10n getL10n() {
        return Furryappet.l10n;
    }

    @Override
    public String getName() {
        return "scripts";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "scripts";
    }
}
