package magmaout.furryappet.gui.utils;

import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringSearchListElement;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.function.Consumer;

public class GuiSearchElement extends GuiStringSearchListElement {
    public GuiSearchElement(Minecraft mc, Consumer<List<String>> callback) {
        super(mc, callback);
    }

    protected GuiListElement<String> createList(Minecraft mc, Consumer<List<String>> callback) {
        return new GuiFolderList(mc, callback);
    }
}
