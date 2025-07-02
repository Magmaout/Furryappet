package magmaout.furryappet.client.gui.panels;

import magmaout.furryappet.client.gui.GuiDashboard;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanel;
import net.minecraft.client.Minecraft;

public class GuiStatesPanel extends GuiDashboardPanel<GuiDashboard> {
    public GuiElement iconBar;
    public GuiIconElement toggleSidebar;
    public GuiElement sidebar;

    public GuiStatesPanel(Minecraft mc, GuiDashboard dashboard) {
        super(mc, dashboard);
    }

    @Override
    public String getName() {
        return "States";
    }
}
