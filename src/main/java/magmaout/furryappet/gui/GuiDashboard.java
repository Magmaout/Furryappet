package magmaout.furryappet.gui;

import magmaout.furryappet.gui.panels.GuiStatesPanel;
import mchorse.mclib.client.gui.mclib.GuiAbstractDashboard;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanels;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.LangKey;
import net.minecraft.client.Minecraft;

public class GuiDashboard extends GuiAbstractDashboard {
    private static GuiDashboard GUI;

    public static GuiDashboard get(Minecraft mc) {
        if (GUI == null) GUI = new GuiDashboard(mc);
        return GUI;
    }

    public GuiStatesPanel states;

    private GuiDashboard(Minecraft mc) {
        super(mc);
    }

    @Override
    protected GuiDashboardPanels createDashboardPanels(Minecraft mc) {
        return new GuiDashboardPanels(mc);
    }

    @Override
    protected void registerPanels(Minecraft mc) {
        states = new GuiStatesPanel(mc, this);

        panels.registerPanel(states, new LangKey("States"), Icons.GEAR);
    }
}
