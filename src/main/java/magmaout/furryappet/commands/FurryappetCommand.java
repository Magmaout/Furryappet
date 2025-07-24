package magmaout.furryappet.commands;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.commands.scripts.ScriptCommand;
import magmaout.furryappet.commands.states.StatesCommand;
import mchorse.mclib.commands.SubCommandBase;
import mchorse.mclib.commands.utils.L10n;
import net.minecraft.command.ICommandSender;

public class FurryappetCommand extends SubCommandBase {

    public FurryappetCommand() {
        this.add(new ScriptCommand());
        this.add(new StatesCommand());
    }

    @Override
    public String getName() {
        return "furryappet";
    }
    @Override
    public String getUsage(ICommandSender sender)
    {
        return "furryappet.commands.furryappet.help";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public L10n getL10n() {
        return Furryappet.l10n;
    }
}
