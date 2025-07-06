package magmaout.furryappet.client.gui.panels;

import com.google.common.collect.Lists;
import magmaout.furryappet.api.states.StatesManager;
import magmaout.furryappet.client.gui.GuiDashboard;
import magmaout.furryappet.client.gui.elements.GuiState;
import magmaout.furryappet.client.gui.elements.GuiStatesList;
import magmaout.furryappet.client.gui.utils.GuiAbstractSidebar;
import magmaout.furryappet.network.PacketWorldInfo;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.utils.Icons;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.List;
import java.util.stream.Collectors;

public class GuiStatesPanel extends GuiAbstractSidebar {
    public GuiIconElement statesAdd;
    public GuiStatesList statesList;

    public GuiStatesPanel(Minecraft mc, GuiDashboard dashboard) {
        super(mc, dashboard);

        statesAdd = new GuiIconElement(mc, Icons.ADD, this::statesAdd);
        statesAdd.flex().relative(main).anchorX(1f);

        statesList = new GuiStatesList(mc);
        statesList.flex().relative(main).wTo(statesAdd.area).h(1f).column(0).stretch();
        add(statesList);
    }

    private void statesAdd(GuiIconElement icon) {

    }

    private void statesActionList(List<GuiState> list) {

    }

    @Override
    protected void sidebarActionList(List<String> list) {
        int index = sidebarList.list.getIndex();
        if (index < 0) return;

        String name = sidebarList.list.getList().get(index);
        MinecraftServer server = mc.isIntegratedServerRunning() ? mc.getIntegratedServer() : mc.player.pla;
        statesList.set(
            name.equals(PacketWorldInfo.NAME) ?
            StatesManager.getServerStates() :
            StatesManager.getPlayerStates(server.getPlayerList().getPlayerByUsername(name))
        );
    }

    @Override
    protected List<String> sidebarCreateList() {
        List<String> list = Lists.newArrayList();
        list.add(PacketWorldInfo.NAME);
        list.addAll(
            mc.world.playerEntities.stream()
            .map(EntityPlayer::getName)
            .sorted(String.CASE_INSENSITIVE_ORDER)
            .collect(Collectors.toList())
        );
        return list;
    }
}
