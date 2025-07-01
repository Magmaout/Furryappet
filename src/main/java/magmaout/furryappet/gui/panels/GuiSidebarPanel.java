package magmaout.furryappet.gui.panels;

import magmaout.furryappet.SaveHandler;
import magmaout.furryappet.api.utils.AbstractData;
import magmaout.furryappet.gui.GuiDashboard;
import magmaout.furryappet.gui.utils.GuiFolderList;
import magmaout.furryappet.gui.utils.GuiSearchElement;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.modals.GuiConfirmModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiPromptModal;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDrawable;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanel;
import mchorse.mclib.client.gui.utils.GuiUtils;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.io.FilenameUtils;
import org.lwjgl.input.Keyboard;

import java.util.List;

public abstract class GuiSidebarPanel<T extends AbstractData> extends GuiDashboardPanel<GuiDashboard> {
    public static final IKey KEYS_CATEGORY = IKey.lang("mappet.gui.panels.keys.category");

    public GuiElement iconBar;
    public GuiIconElement toggleSidebar;
    public GuiElement sidebar;

    public GuiElement buttons;
    public GuiIconElement add;
    public GuiIconElement dupe;
    public GuiIconElement rename;
    public GuiIconElement remove;
    public GuiSearchElement names;
    public GuiFolderList namesList;
    public GuiElement editor;

    protected boolean update;
    protected T data;
    protected boolean allowed;
    protected boolean save;

    public GuiSidebarPanel(Minecraft mc, GuiDashboard dashboard) {
        super(mc, dashboard);

        buttons = new GuiElement(mc);
        sidebar = new GuiElement(mc);
        sidebar.flex().relative(this).x(1F).w(200).h(1F).anchorX(1F);
        iconBar = new GuiElement(mc);
        iconBar.flex().relative(sidebar).x(-20).w(20).h(1F).column(0).stretch();

        toggleSidebar = new GuiIconElement(mc, Icons.RIGHTLOAD, (element) -> toggleSidebar());
        iconBar.add(toggleSidebar);

        add = new GuiIconElement(mc, Icons.ADD, this::addNewData);
        add.context(() -> {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(mc);
            menu.action(Icons.ADD, IKey.lang("mappet.gui.panels.context.add_folder"), this::addFolder);
            return menu.shadow();
        });

        dupe = new GuiIconElement(mc, Icons.DUPE, this::dupeData);
        rename = new GuiIconElement(mc, Icons.EDIT, this::renameData);
        rename.context(() -> {
            if (namesList.getPath().isEmpty()) return null;
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(mc);
            menu.action(Icons.EDIT, IKey.lang("mappet.gui.panels.context.rename_folder"), this::renameFolder);
            return menu.shadow();
        });

        remove = new GuiIconElement(mc, Icons.REMOVE, this::removeData);
        remove.context(() -> {
            if (namesList.getPath().isEmpty()) return null;
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(mc);
            menu.action(Icons.REMOVE, IKey.lang("mappet.gui.panels.context.remove_folder"), this::removeFolder);
            return menu.shadow();
        });

        GuiDrawable drawable = new GuiDrawable((context) -> font.drawStringWithShadow(I18n.format(getName()), names.area.x, area.y + 10, 0xffffff));

        names = new GuiSearchElement(mc, (list) -> pickData(list.get(0)));
        namesList = (GuiFolderList) names.list;
        names.label(IKey.lang("mappet.gui.search"));
        names.flex().relative(this.sidebar).xy(10, 25).w(1F, -20).h(1F, -35);
        names.list.context(() -> {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(mc);
            if (data != null) menu.action(Icons.COPY, IKey.lang("mappet.gui.panels.context.copy"), this::copy);

            try {
                NBTTagCompound tag = JsonToNBT.getTagFromJson(GuiScreen.getClipboardString());
                if (tag.getString("_ContentType").equals(getName()))
                    menu.action(Icons.PASTE, IKey.lang("mappet.gui.panels.context.paste"), () -> paste(tag));
            } catch (Exception e) {}

            if (mc.isSingleplayer()) {
                menu.action(Icons.FOLDER, IKey.lang("mappet.gui.panels.context.open_folder"), () -> {
                    //  SAVING TO WORLD PATH
                    //String path = SaveHandler.getWorldPath() + "/" + namesList.getPath();
                    //GuiUtils.openFolder(path);
                });
            }
            return menu.actions.getList().isEmpty() ? null : menu.shadow();
        });
        sidebar.add(drawable, names, buttons);

        editor = new GuiElement(mc);
        editor.flex().relative(this).wTo(iconBar.area).h(1F);

        buttons.flex().relative(names).x(1F).y(-20).anchorX(1F).row(0).resize();
        buttons.add(add, dupe, rename, remove);

        markContainer();
        add(sidebar, iconBar, editor);

        keys().register(IKey.lang("mappet.gui.panels.keys.toggle_sidebar"), Keyboard.KEY_N, () -> toggleSidebar.clickItself(GuiBase.getCurrent())).category(KEYS_CATEGORY);
    }

    private void copy() {
        NBTTagCompound tag = data.serializeNBT();
        tag.setString("_ContentType", getName());
        GuiScreen.setClipboardString(tag.toString());
    }

    private void paste(NBTTagCompound tag) {
//        T data = (T) getType().getManager().create("", tag);
//        addNewData(add, data);
    }

    private void toggleSidebar() {
        sidebar.toggleVisible();
        toggleSidebar.both(sidebar.isVisible() ? Icons.RIGHTLOAD : Icons.LEFTLOAD);
        if (sidebar.isVisible()) {
            toggleWithSidebar();
            iconBar.flex().relative(sidebar).x(-20);
        } else {
            toggleFull();
            iconBar.flex().relative(this).x(1F, -20);
        }
        resize();
    }

    protected void toggleWithSidebar()
    {
        editor.flex().wTo(iconBar.area);
    }

    protected void toggleFull()
    {
        editor.flex().wTo(iconBar.area);
    }

    /**
     * Get the content type of this panel
     */
    public abstract String getName();

    public void pickData(String id) {
        save();
        //Dispatcher.sendToServer(new PacketContentRequestData(this.getType(), id));
    }

    /* CRUD */

    protected void addNewData(GuiIconElement element) {
        addNewData(element, null);
    }

    protected void addNewData(GuiIconElement element, T data) {
        GuiModal.addFullModal(sidebar, () -> new GuiPromptModal(mc, IKey.lang("mappet.gui.panels.modals.add"), (name) -> addNewData(namesList.getPath(name), data)).filename());
    }

    protected void addNewData(String name, T data) {
//        if (!namesList.hasInHierarchy(name)) {
//            save();
//            //Dispatcher.sendToServer(new PacketContentData(this.getType(), name, data == null ? new NBTTagCompound() : data.serializeNBT()));
//            namesList.addFile(name);
//            if (data == null) {
//                data = (T) getType().getManager().create(name);
//                fillDefaultData(data);
//                getType().getManager().create(data.getName(), data.serializeNBT());
//            } else data.setName(name);
//            fill(data);
//        }
    }

    private void addFolder() {
        GuiModal.addFullModal(sidebar, () -> new GuiPromptModal(mc, IKey.lang("mappet.gui.panels.modals.add_folder"), this::addFolder).filename());
    }

    private void addFolder(String name) {
        //Dispatcher.sendToServer(new PacketContentFolder(this.getType(), name, this.namesList.getWorldPath("")));
    }

    private void renameFolder() {
        if (namesList.getPath().isEmpty()) return;
        String name = FilenameUtils.getBaseName(namesList.getPath());
        GuiModal.addFullModal(sidebar, () -> new GuiPromptModal(mc, IKey.lang("mappet.gui.panels.modals.rename_folder"), this::renameFolder).filename().setValue(name));
    }

    private void renameFolder(String name) {
        String path = namesList.getPath("");
        //Dispatcher.sendToServer(new PacketContentFolder(this.getType(), "", path.substring(0, path.length() - 1)).rename(name));
        fill(null);
    }

    private void removeFolder() {
        if (!namesList.getPath().isEmpty()) return;
        GuiModal.addFullModal(sidebar, () -> new GuiConfirmModal(mc, IKey.lang("mappet.gui.panels.modals.remove_folder"), this::removeFolder));
    }

    private void removeFolder(Boolean isDelete) {
        if (isDelete) {
            String path = namesList.getPath("");
            //Dispatcher.sendToServer(new PacketContentFolder(this.getType(), "", path.substring(0, path.length() - 1)).delete());
        }
    }

    protected void fillDefaultData(T data) {

    }

    protected void dupeData(GuiIconElement element) {
        if (data == null) return;
        GuiModal.addFullModal(sidebar, () -> {
            GuiPromptModal promptModal = new GuiPromptModal(mc, IKey.lang("mappet.gui.panels.modals.dupe"), this::dupeData);
            return promptModal.setValue(data.getName()).filename();
        });
    }

    protected void dupeData(String name) {
//        if (!namesList.hasInHierarchy(name)) {
//            save();
//            //Dispatcher.sendToServer(new PacketContentData(this.getType(), name, this.data.serializeNBT()));
//            namesList.addFile(name);
//            T data = (T) getType().getManager().create(name, this.data.serializeNBT());
//            fill(data);
//        }
    }

    protected void renameData(GuiIconElement element) {
        if (data == null) return;
        GuiModal.addFullModal(sidebar, () -> {
            GuiPromptModal promptModal = new GuiPromptModal(mc, IKey.lang("mappet.gui.panels.modals.rename"), this::renameData);
            return promptModal.setValue(data.getName().substring(data.getName().lastIndexOf('/') + 1)).filename();
        });
    }

    protected void renameData(String name) {
        if (!namesList.hasInHierarchy(name)) {
            String path = this.getDataPath();
            //Dispatcher.sendToServer(new PacketContentData(this.getType(), this.data.getId(), this.data.serializeNBT()).rename(path + name));

            namesList.removeFile(this.data.getName());
            namesList.addFile(path + name);

            data.setName(path + name);
        }
    }

    protected String getDataPath() {
        String output = "";
        int index = data.getName().lastIndexOf('/');
        if (index != -1)
            output = data.getName().substring(0, index + 1);
        return output;
    }

    protected void removeData(GuiIconElement element) {
        if (data == null) return;
        GuiModal.addFullModal(sidebar, () -> new GuiConfirmModal(mc, IKey.lang("mappet.gui.panels.modals.remove"), this::removeData));
    }

    protected void removeData(boolean confirm) {
        if (data != null && confirm) {
            //Dispatcher.sendToServer(new PacketContentData(this.getType(), this.data.getId(), null));
            namesList.removeFile(data.getName());
            fill(null);
        }
    }

    /* Data population */

    public final void fill(T data) {
        fill(data, true);
    }

    public void fill(T data, boolean allowed) {
        this.data = data;
        this.allowed = allowed;

        editor.setEnabled(allowed);
        remove.setEnabled(allowed);
        rename.setEnabled(allowed);
    }

    public void fillNames(List<String> names) {
        String value = data == null ? null : data.getName();

        namesList.fill(names);
        namesList.sort();
        namesList.setCurrentFile(value);
    }

    protected GuiScrollElement createScrollEditor() {
        GuiScrollElement scrollEditor = new GuiScrollElement(mc);
        scrollEditor.flex().relative(editor).wh(1F, 1F).column(5).stretch().vertical().scroll().padding(10);
        return scrollEditor;
    }

    @Override
    public void open() {
        super.open();
        update = true;
        save = true;
    }

    @Override
    public void appear() {
        super.appear();

        if (update) {
            update = false;

            requestDataNames();
        }
        if (data != null) {
            //Dispatcher.sendToServer(new PacketContentRequestData(this.getType(), this.data.getId()));
        }
    }

    public void requestDataNames() {
        //Dispatcher.sendToServer(new PacketContentRequestNames(this.getType()));
    }

    @Override
    public void disappear() {
        super.disappear();
        if (save) save();
    }

    @Override
    public void close() {
        super.close();
        if (save) save();
    }

    public void save() {
        if (!update && data != null && editor.isEnabled()) {
            preSave();
            //Dispatcher.sendToServer(new PacketContentData(this.getType(), this.data.getId(), this.data.serializeNBT()));
        }
    }

    protected void preSave() {

    }

    @Override
    public void draw(GuiContext context) {
        iconBar.area.draw(0x77000000);
        GuiDraw.drawHorizontalGradientRect(iconBar.area.x - 6, iconBar.area.y, iconBar.area.x, iconBar.area.ey(), 0, 0x29000000);
        if (sidebar.isVisible()) sidebar.area.draw(0xaa000000);

        super.draw(context);
        if (!editor.isEnabled() && data != null) GuiDraw.drawLockedArea(editor);
    }
}