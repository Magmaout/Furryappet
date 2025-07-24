package magmaout.furryappet.api.scripts;

import magmaout.furryappet.api.data.IDirtyCheck;
import magmaout.furryappet.api.data.INBTData;
import magmaout.furryappet.api.data.INamedData;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class Script implements INBTData, IDirtyCheck, INamedData {
    public String name;
    public String code;

    private int lastHash = -1;

    @Override
    public NBTBase toNBT() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("name", name);
        nbtTagCompound.setString("code", code);
        return nbtTagCompound;
    }

    @Override
    public void fromNBT(NBTBase nbt) {
        this.name = ((NBTTagCompound) nbt).getString("name");
        this.code = ((NBTTagCompound) nbt).getString("code");
    }

    @Override
    public boolean isValid(NBTBase nbt) {
        return nbt instanceof NBTTagCompound && !((NBTTagCompound) nbt).getString("name").isEmpty() && !((NBTTagCompound) nbt).getString("code").isEmpty();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isDirty() {
        return lastHash != code.hashCode();
    }

    @Override
    public void markClean() {
        lastHash = code.hashCode();
    }

}
