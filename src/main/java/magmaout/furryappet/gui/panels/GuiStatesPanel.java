package magmaout.furryappet.gui.panels;

import magmaout.furryappet.api.elements.State;
import magmaout.furryappet.gui.GuiDashboard;
import net.minecraft.client.Minecraft;

public class GuiStatesPanel extends GuiSidebarPanel<State> {

    public GuiStatesPanel(Minecraft mc, GuiDashboard dashboard) {
        super(mc, dashboard);
    }

    @Override
    public String getName() {
        return "States";
    }
}
