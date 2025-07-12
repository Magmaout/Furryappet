package magmaout.furryappet.client.gui.utils;

import magmaout.furryappet.client.gui.GuiDashboard;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringSearchListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanel;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public abstract class GuiAbstractSidebar extends GuiDashboardPanel<GuiDashboard> {
    public GuiElement main;

    public GuiElement sidebar;
    public GuiElement sidebarToggle;
    public GuiIconElement sidebarToggleIcon;
    public GuiStringSearchListElement sidebarList;

    public GuiIconElement elementAdd;
    public GuiIconElement elementDupe;
    public GuiIconElement elementEdit;
    public GuiIconElement elementRemove;

    public GuiAbstractSidebar(Minecraft mc, GuiDashboard dashboard) {
        this(mc, dashboard, null);
    }

    public GuiAbstractSidebar(Minecraft mc, GuiDashboard dashboard, ResourceLocation path) {
        super(mc, dashboard);

        sidebar = new GuiElement(mc);
        sidebar.flex().relative(this)
            .w(20).x(1.f).anchorX(1.f)
            .h(1.f);

        sidebarToggle = new GuiElement(mc);
        sidebarToggle.flex().relative(sidebar)
            .w(20).anchorX(1.f)
            .h(1.f)
            .column(0).stretch();

        sidebarToggleIcon = new GuiIconElement(mc, Icons.RIGHTLOAD, this::sidebarToggle);
        sidebarToggle.add(sidebarToggleIcon);

        main = new GuiElement(mc);
        main.flex().relative(this)
            .wTo(sidebarToggle.area)
            .h(1.f);

        sidebarList = new GuiStringSearchListElement(mc, this::sidebarActionList);
        sidebarList.label = IKey.lang("blockbuster.gui.search");
        sidebarList.flex().relative(sidebar)
            .w(20)
            .h(1.f)
            .column(0).stretch();

        sidebar.add(sidebarList);

        if (path != null) {
            elementAdd = new GuiIconElement(mc, Icons.ADD, this::elementAdd);
            elementAdd.flex().relative(sidebar).x(-20).y(20);
            elementDupe = new GuiIconElement(mc, Icons.DUPE, this::elementDupe);
            elementDupe.flex().relative(sidebar).x(-20).y(40);
            elementEdit = new GuiIconElement(mc, Icons.EDIT, this::elementEdit);
            elementEdit.flex().relative(sidebar).x(-20).y(60);
            elementRemove = new GuiIconElement(mc, Icons.REMOVE, this::elementRemove);
            elementRemove.flex().relative(sidebar).x(-20).y(80);

            sidebar.add(elementAdd, elementDupe, elementEdit, elementRemove);
        }

        markContainer();
        add(main, sidebar, sidebarToggle);
    }

    public void sidebarToggle(GuiIconElement icon) {
        icon.both(sidebar.isVisible() ? Icons.LEFTLOAD : Icons.RIGHTLOAD);
        sidebar.toggleVisible();
        if (sidebar.isVisible()) {
            sidebarToggle.flex().relative(sidebar).x(0.f);
            sidebarListUpdate();
        } else {
            sidebarToggle.flex().relative(this).x(1.f);
        }
        main.resize();
        resize();
    }

    public void sidebarListUpdate() {
        List<String> list = sidebarCreateList();
        int width = 25;
        for (String value : list)
            if (font.getStringWidth(value) > width)
                width = font.getStringWidth(value);
        width += 15;
        sidebarList.flex().w(width);
        sidebarList.list.setList(list);
        sidebar.flex().w(width);
        resize();
    }

    protected abstract void sidebarActionList(List<String> list);

    protected abstract List<String> sidebarCreateList();

    public void elementAdd(GuiIconElement icon) {

    }

    public void elementDupe(GuiIconElement icon) {

    }

    public void elementEdit(GuiIconElement icon) {

    }

    public void elementRemove(GuiIconElement icon) {

    }

    @Override
    public void open() {
        sidebarListUpdate();
        super.open();
    }

    @Override
    public void draw(GuiContext context) {
        sidebarToggle.area.draw(0x77000000);
        GuiDraw.drawHorizontalGradientRect(sidebarToggle.area.x - 6, sidebarToggle.area.y, sidebarToggle.area.x, sidebarToggle.area.ey(), 0, 0x29000000);
        if (sidebar.isVisible()) sidebar.area.draw(0xaa000000);
        super.draw(context);
    }
}