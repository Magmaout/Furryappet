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
import net.minecraft.nbt.JsonToNBT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        name = new GuiTextElement(mc, this::rename);
        name.setText(key);
        remove = new GuiIconElement(mc, Icons.REMOVE, this::remove);
        convert = new GuiIconElement(mc, Icons.REFRESH, this::convert);

        flex().row(0).preferred(0);
        update();
    }

    public String name() {
        return name.field.getText();
    }

    private void rename(String input) {
        if (states.getKeys().contains(input) || input.isEmpty()) {
            name.field.setTextColor(ColorPalette.NEGATIVE);
            return;
        }

        StateType type = states.getStateType(key);
        Object value = states.removeState(key);
        name.field.setTextColor(ColorPalette.ENABLED);
        states.setState(input, type, value);
        this.key = input;
    }

    private void convert(GuiIconElement element) {
        StateType type = states.getStateType(key);
        if (type.equals(StateType.STRING)) {
            double number = 0;
            String string = states.getString(key);
            Pattern pattern = Pattern.compile("\\d+[.,]?\\d*");
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()) {
                string = matcher.group().replace(',', '.');
                try { number = Double.parseDouble(string);
                } catch (NumberFormatException e) {}
            }
            states.setState(key, StateType.NUMBER, number);
        } else if (type.equals(StateType.NUMBER)) {
            String string = String.valueOf(states.getNumber(key));
            states.setState(key, StateType.STRING, string);
        }
        update();
    }

    private void remove(GuiIconElement icon) {
        states.removeState(key);
        GuiElement parent = getParentContainer();
        removeFromParent();
        parent.resize();
    }

    private void update() {
        switch (states.getStateType(key)) {
            case OBJECT:
                value = new GuiLabel(mc, IKey.str(states.getUnknownState(key).toString())).anchor(0.0f, 0.5f);
                convert.setEnabled(false);
            break;
            case NBT:
                convert.setEnabled(false);
            case STRING:
                value = new GuiTextElement(mc, Furryappet.STRING_LENGTH, this::write);
                ((GuiTextElement) value).setText(states.getUnknownState(key).toString());
                break;
            case NUMBER:
                value = new GuiTrackpadElement(mc, (number) -> states.setNumber(key, number));
                ((GuiTrackpadElement) value).increment(1).setValue(states.getNumber(key));
                break;
        }
        removeAll();
        add(name, convert, value, remove);
        if (hasParent()) getParentContainer().resize();
    }

    private void write(String string) {
        if (states.getStateType(key).equals(StateType.STRING)) {
            states.setString(key, string);
        } else if (states.getStateType(key).equals(StateType.NBT)) try {
            ((GuiTextElement) value).field.setTextColor(ColorPalette.ENABLED);
            states.setNBT(key, JsonToNBT.getTagFromJson(string));
        } catch (Exception ignored) {
            ((GuiTextElement) value).field.setTextColor(ColorPalette.NEGATIVE);
        }
    }
}