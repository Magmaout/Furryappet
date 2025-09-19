package magmaout.furryappet.client.gui.panels;

import com.google.common.collect.Lists;
import magmaout.furryappet.api.states.States;
import magmaout.furryappet.client.gui.GuiDashboard;
import magmaout.furryappet.client.gui.elements.GuiStatesEditor;
import magmaout.furryappet.client.gui.utils.GuiAbstractSidebar;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

public class GuiGeneralPanel extends GuiAbstractSidebar {
    public GuiStatesEditor statesEditor;

    public GuiGeneralPanel(Minecraft mc, GuiDashboard dashboard) {
        super(mc, dashboard);

        statesEditor = new GuiStatesEditor(mc);
        statesEditor.flex().relative(this)
            .w(0.5f, -5).x(5)
            .h(1.f, -10).y(5);

        main.add(statesEditor);
    }

    @Override
    protected void sidebarActionList(List<String> list) {
        int index = sidebarList.list.getIndex();
        if (index < 0) return;

        String name = sidebarList.list.getList().get(index);

        States states = new States();
        states.setString("name", "magnitofon");
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("uiiaiuia", true);
        tag.setIntArray("array", new int[]{1, 2, 3});
        states.setNBT("nbt", tag);
        states.setNumber("age", 5077);
        states.setObject("color", Color.BLACK);

        statesEditor.setupStates(states);
    }

    @Override
    protected List<String> sidebarCreateList() {
        List<String> list = Lists.newArrayList();
        list.addAll(
            mc.world.playerEntities.stream()
            .map(EntityPlayer::getName)
            .sorted(String.CASE_INSENSITIVE_ORDER)
            .collect(Collectors.toList())
        );
        return list;
    }
}
