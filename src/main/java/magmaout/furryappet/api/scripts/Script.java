package magmaout.furryappet.api.scripts;

import magmaout.furryappet.api.data.IDirtyCheck;
import magmaout.furryappet.api.data.INBTData;
import magmaout.furryappet.api.data.INamedData;
import net.minecraft.nbt.NBTTagCompound;

public class Script implements INBTData, IDirtyCheck, INamedData {
    public String name;
    public String code;

    private int lastHash = -1;

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("name", name);
        nbtTagCompound.setString("code", code);
        return nbtTagCompound;
    }

    @Override
    public void fromNBT(NBTTagCompound nbt) {
        this.name = nbt.getString("name");
        this.code = nbt.getString("code");
    }

    @Override
    public boolean isValid(NBTTagCompound nbt) {
        return !nbt.getString("name").isEmpty() && !nbt.getString("code").isEmpty();
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
