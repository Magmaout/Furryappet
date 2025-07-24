package magmaout.furryappet.commands.states;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.commands.scripts.ExecuteCommand;
import magmaout.furryappet.commands.scripts.InfoCommand;
import magmaout.furryappet.commands.scripts.ReloadCommand;
import magmaout.furryappet.commands.scripts.UnloadCommand;
import mchorse.mclib.commands.SubCommandBase;
import mchorse.mclib.commands.utils.L10n;
import net.minecraft.command.ICommandSender;

public class StatesCommand extends SubCommandBase {
    public StatesCommand() {
        this.add(new ListCommand());
    }
    @Override
    public L10n getL10n() {
        return Furryappet.l10n;
    }

    @Override
    public String getName() {
        return "states";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }
}
