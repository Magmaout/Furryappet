package magmaout.furryappet.client.gui.elements;

import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import net.minecraft.client.Minecraft;

public class GuiState extends GuiElement {
    public GuiTextElement name;
    public GuiTextElement value;

    public GuiState(Minecraft mc) {
        super(mc);
    }
}
