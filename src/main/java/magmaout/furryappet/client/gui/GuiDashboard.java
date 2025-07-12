package magmaout.furryappet.client.gui;

import magmaout.furryappet.client.gui.panels.GuiGeneralPanel;
import mchorse.mclib.client.gui.mclib.GuiAbstractDashboard;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanels;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiDashboard extends GuiAbstractDashboard {
    private static GuiDashboard GUI;

    public static GuiDashboard get(Minecraft mc) {
        if (GUI == null) GUI = new GuiDashboard(mc);
        return GUI;
    }

    public GuiGeneralPanel general;

    private GuiDashboard(Minecraft mc) {
        super(mc);
    }

    @Override
    protected GuiDashboardPanels createDashboardPanels(Minecraft mc) {
        return new GuiDashboardPanels(mc);
    }

    @Override
    protected void registerPanels(Minecraft mc) {
        general = new GuiGeneralPanel(mc, this);

        panels.registerPanel(general, IKey.lang("furryappet.gui.general"), Icons.GEAR);
    }
}
