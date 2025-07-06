package magmaout.furryappet.client.gui.elements;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.states.StateType;
import magmaout.furryappet.api.states.States;
import magmaout.furryappet.client.gui.utils.ColorPalette;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiState extends GuiElement {
    public GuiTextElement name;
    public GuiIconElement convert;
    public GuiElement value;
    public GuiIconElement remove;

    public States states;
    public String key;

    public GuiState(Minecraft mc, String key, States states) {
        super(mc);
        this.states = states;
        this.key = key;

        name = new GuiTextElement(mc, 1000, this::rename);
        name.flex().w(120);
        name.setText(key);

        convert = new GuiIconElement(mc, Icons.REFRESH, this::convert);
        convert.flex().relative(name);

        switch (states.getStateType(key)) {
            case OBJECT:
                value = new GuiLabel(mc, IKey.str(states.getUnknownState(key).toString()));
                convert.setEnabled(false);
            break;
            case NBT:
                convert.setEnabled(false);
            case STRING:
                value = new GuiTextElement(mc, Furryappet.FIELD_LENGTH, this::stringFieldUpdate);
                ((GuiTextElement) value).setText(states.getUnknownState(key).toString());
            break;
            case NUMBER:
                value = new GuiTrackpadElement(mc, this::numberTrackpadUpdate);
                ((GuiTrackpadElement) value).setValue(states.getNumber(key));
            break;
        }
        value.flex().relative(convert).w(240);

        remove = new GuiIconElement(mc, Icons.REMOVE, this::remove);
        remove.flex().relative(value);

        flex().row(0).preferred(2);
        add(name, convert, value, remove);
    }

    public String getName() {
        return name.field.getText();
    }

    private void rename(String key) {
        if (states.getKeys().contains(key) || key.isEmpty()) {
            name.field.setTextColor(ColorPalette.NEGATIVE);
            return;
        }

        StateType type = states.getStateType(key);
        Object value = states.removeState(key);
        name.field.setTextColor(0xffffff);
        states.setState(key, type, value);
        this.key = key;
    }

    private void convert(GuiIconElement element) {
        StateType type = states.getStateType(key);
        if (type.equals(StateType.STRING)) {
            double number = Double.parseDouble(states.getString(key));
            value = new GuiTrackpadElement(mc, this::numberTrackpadUpdate);
            ((GuiTrackpadElement) value).setValue(number);
            states.setState(key, StateType.NUMBER, number);
        } else if (type.equals(StateType.NUMBER)) {
            String string = String.valueOf(states.getNumber(key));
            value = new GuiTextElement(mc, this::stringFieldUpdate);
            ((GuiTextElement) value).setText(string);
            states.setState(key, StateType.STRING, string);
        }
    }

    private void remove(GuiIconElement icon) {
        states.removeState(key);
        removeFromParent();
        getParentContainer().resize();
    }

    private void stringFieldUpdate(String string) {

    }

    private void numberTrackpadUpdate(Double number) {

    }
}