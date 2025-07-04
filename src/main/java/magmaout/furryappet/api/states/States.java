package magmaout.furryappet.api.states;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public class States implements INBTSerializable<NBTTagCompound> {
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

    public Object getUnknownState(String key) {
        State rawState = getRawState(key);
        return rawState == null ? null : rawState.value;
    }
    public StateType getStateType(String key) {
        State rawState = getRawState(key);
        return rawState == null ? null : rawState.type;
    }

    //use map directly only in downer methods, raw methods too

    public void setState(String key, StateType type, Object value) {
        if(value == null) {
            setRawState(key, null);
            return;
        }
        if(!type.getClassType().isInstance(value)) {
            throw new IllegalArgumentException("Value is not an instance of " + type.getClassType().getName());
        }

        State rawState = getRawState(key);
        if(rawState == null || !rawState.type.equals(type)) {
           setRawState(key, new State(value, type));
           return;
        }
        rawState.value = value;
    }
    public Object getState(String key, StateType type) {
        State rawState = getRawState(key);
        if(rawState == null || !rawState.type.equals(type)) {
            return null;
        }
        return rawState.value;
    }
    private State getRawState(String key) {
        return map.get(key);
    }
    private void setRawState(String key, State value) {
        if(value == null) {
            map.remove(key);
            return;
        }
        State existing = getRawState(key);
        if(existing != null && existing.type.equals(value.type)) {
            existing.value = value.value;
            return;
        }
        map.put(key, value);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        for(Map.Entry<String, State> entry : map.entrySet()) {
            String key = entry.getKey();
            State state = entry.getValue();
            NBTBase serializedState = state.type.toNBT(state.value, key);
            if(serializedState == null) {
                continue;
            }
            NBTTagCompound nbtState = new NBTTagCompound();
            nbtState.setByte("type", (byte) state.type.ordinal());
            nbtState.setTag("data", serializedState);
            tag.setTag(key, nbtState);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        for (String key : nbt.getKeySet()) {
            NBTTagCompound nbtState = (NBTTagCompound) nbt.getTag(key);
            StateType type = StateType.values()[nbtState.getByte("type")];
            Object value = type.fromNBT(nbtState.getTag("data"), key);

            map.put(key, new State(value, type));
        }

    }
    private static class State {
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