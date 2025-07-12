package magmaout.furryappet.api.states;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

@SuppressWarnings("unused")
public class States {
    private final Map<String, State> map = new HashMap<>();

    public void setString(String key, String value) {
        setState(key, StateType.STRING, value);
    }
    public String getString(String key) {
        return (String) getState(key, StateType.STRING);
    }
    public void setNumber(String key, double value) {
        setState(key, StateType.NUMBER, value);
    }
    public double getNumber(String key) {
        return (Double) getState(key, StateType.NUMBER);

    }
    public void setNBT(String key, NBTBase value) {
        setState(key, StateType.NBT, value);
    }
    public NBTBase getNBT(String key) {
        return (NBTBase) getState(key, StateType.NBT);
    }
    public void setObject(String key, Object value) {
        setState(key, StateType.OBJECT, value);
    }
    public Object getObject(String key) {
        return getState(key, StateType.OBJECT);
    }
    public Set<String> getKeys() {
        return map.keySet();
    }

    public Object removeState(String key) {
        return map.containsKey(key) ? map.remove(key).value : null;
    }

    public Object getUnknownState(String key) {
        return map.containsKey(key) ? map.get(key).value : null;
    }

    public StateType getStateType(String key) {
        return map.containsKey(key) ? map.get(key).type : null;
    }

    //use map directly only in downer methods, raw methods too

    public void setState(String key, StateType type, Object value) {
        if (value == null) {
            setRawState(key, null);
            return;
        }

        if (!type.getClassType().isInstance(value)) {
            throw new IllegalArgumentException("Value is not an instance of " + type.getClassType().getName());
        }

        if (!map.containsKey(key) || !map.get(key).type.equals(type)) {
            setRawState(key, new State(value, type));
            return;
        }
        map.get(key).value = value;
    }

    private Object getState(String key, StateType type) {
        if (!map.containsKey(key) || !map.get(key).type.equals(type)) return null;
        return map.get(key).value;
    }

    private void setRawState(String key, State value) {
        if (value == null) {
            map.remove(key);
            return;
        }

        State existing = map.get(key);
        if (existing != null && existing.type.equals(value.type)) {
            existing.value = value.value;
            return;
        }
        map.put(key, value);
    }
    public NBTTagCompound toNBT(boolean isClient) {
        NBTTagCompound tag = new NBTTagCompound();
        for (Map.Entry<String, State> entry : map.entrySet()) {
            String key = entry.getKey();
            State state = entry.getValue();
            NBTBase serializedState = state.type.toNBT(state.value, key, isClient);
            if (serializedState == null) continue;

            NBTTagCompound nbtState = new NBTTagCompound();
            nbtState.setByte("type", (byte) state.type.ordinal());
            nbtState.setTag("data", serializedState);
            tag.setTag(key, nbtState);
        }
        return tag;
    }
    public void fromNBT(NBTTagCompound nbt, boolean isClient) {
        for (String key : nbt.getKeySet()) {
            NBTTagCompound nbtState = (NBTTagCompound) nbt.getTag(key);
            StateType type = StateType.values()[nbtState.getByte("type")];
            Object value = type.fromNBT(nbtState.getTag("data"), key, isClient);

            map.put(key, new State(value, type));
        }
    }

    protected static class State {
        public Object value;
        public StateType type;

        public State(Object value, StateType type) {
            this.value = value;
            this.type = type;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        States states = (States) o;
        return Objects.equals(map, states.map);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(map.values());
    }
}