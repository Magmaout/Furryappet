package magmaout.furryappet.client.gui.elements;

import magmaout.furryappet.api.states.States;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.Comparator;

public class GuiStatesList extends GuiScrollElement {
    private States states;

    public GuiStatesList(Minecraft mc) {
        super(mc);
        flex().column(5).vertical().stretch().scroll().padding(10);
    }

    public States get() {
        return this.states;
    }

    public GuiStatesList set(States states) {
        this.states = states;
        removeAll();

        if (states != null)
            for (String key : states.getKeys())
                add(new GuiState(mc, key, states));

        sortElements();
        resize();
        return this;
    }

    private void sortElements() {
        getChildren().sort(Comparator.comparing(state -> ((GuiState) state).getName()));
    }

    public void addNew() {
        if (states == null) return;

        int index = states.getKeys().size() + 1;
        String key = "state_" + index;

        while (states.getKeys().contains(key)) {
            index += 1;
            key = "state_" + index;
        }

        states.setNumber(key, 0);
        add(new GuiState(mc, key, states));

        sortElements();
        getParentContainer().resize();
    }

    @Override
    public void draw(GuiContext context) {
        super.draw(context);

        if (states != null && states.getKeys().isEmpty()) {
            int w = this.area.w / 2;
            int x = this.area.mx(w);

            GuiDraw.drawMultiText(font, IKey.lang("furryappet.gui.empty").get(), x, area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }
    }
}