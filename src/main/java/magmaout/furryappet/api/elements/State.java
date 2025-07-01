package magmaout.furryappet.api.elements;

import magmaout.furryappet.api.utils.AbstractData;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;

public class State extends AbstractData {
    private NBTBase value = new NBTTagByte((byte) 0);

    public void setValue(NBTBase value) {
        this.value = value;
    }

    public NBTBase getValue() {
        return value;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("name", getName());
        tag.setTag("value", getValue());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        setName(nbt.getString("name"));
        setValue(nbt.getTag("value"));
    }
}
