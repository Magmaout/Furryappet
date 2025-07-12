package magmaout.furryappet.client.gui.elements;

import com.google.common.collect.Lists;
import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.states.States;
import magmaout.furryappet.client.gui.utils.ColorPalette;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.IGuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GuiStatesEditor extends GuiElement {
    public GuiIconElement statesAdd;
    public GuiTextElement searchField;
    public GuiScrollElement statesList;

    private States currentStates;

    public GuiStatesEditor(Minecraft mc) {
        super(mc);

        statesAdd = new GuiIconElement(mc, Icons.ADD, this::statesAdd);
        statesAdd.setEnabled(false);
        statesAdd.flex().relative(this)
            .anchorX(1.f).x(1.f, -5)
            .y(5);

        searchField = new GuiTextElement(mc, Furryappet.STRING_LENGTH, this::searchFieldEntry);
        searchField.setEnabled(false);
        searchField.flex().relative(this)
            .wTo(statesAdd.area).x(5)
            .y(5);

        statesList = new GuiScrollElement(mc);
        statesList.flex().relative(this)
            .w(1.f)
            .h(1.f, -25).y(25)
            .column(5).vertical().stretch().scroll().padding(5);

        add(statesAdd, searchField, statesList);
    }

    private void searchFieldEntry(String search) {
        if (currentStates == null) return;

        List<GuiState> list = createStatesList();
        list.sort(Comparator.comparing((state) -> state.name().toLowerCase()));
        if (!search.isEmpty()) list = list.stream()
            .filter((element) -> element.name().toLowerCase().contains(search.toLowerCase()))
            .collect(Collectors.toList());

        statesList.removeAll();
        statesList.add(list.toArray(new IGuiElement[0]));
        statesList.resize();
    }

    private void statesAdd(GuiIconElement icon) {
        if (currentStates == null) return;

        int index = currentStates.getKeys().size() + 1;
        String key = "state_" + index;
        while (currentStates.getKeys().contains(key)) {
            index += 1;
            key = "state_" + index;
        }
        currentStates.setNumber(key, 0);

        statesList.add(new GuiState(mc, key, currentStates));
        statesList.resize();
    }

    public void setupStates(States states) {
        currentStates = states;
        statesList.removeAll();
        if (states != null) {
            statesList.add(createStatesList().toArray(new IGuiElement[0]));
            searchField.setEnabled(true);
            statesAdd.setEnabled(true);
        } else {
            searchField.setEnabled(false);
            statesAdd.setEnabled(false);
        }
        statesList.resize();
    }

    private List<GuiState> createStatesList() {
        List<GuiState> list = Lists.newArrayList();
        if (currentStates != null)
            currentStates.getKeys().forEach((key) -> list.add(new GuiState(mc, key, currentStates)));
        list.sort(Comparator.comparing(GuiState::name));
        return list;
    }

    @Override
    public void draw(GuiContext context) {
        area.draw(0x77000000);
        super.draw(context);

        if (currentStates == null || currentStates.getKeys().isEmpty()) font.drawStringWithShadow(
            IKey.lang("furryappet.gui.empty").get(),
            statesList.area.mx(),
            statesList.area.my(),
            ColorPalette.DISABLED
        );

        if (!searchField.field.isFocused() && searchField.field.getText().isEmpty()) font.drawStringWithShadow(
            IKey.lang("blockbuster.gui.search").get(),
            searchField.area.x + 5,
            searchField.area.y + 6,
            ColorPalette.DISABLED
        );
    }
}
