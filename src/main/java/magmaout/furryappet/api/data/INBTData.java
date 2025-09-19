package magmaout.furryappet.api.data;

import net.minecraft.nbt.NBTTagCompound;

public interface INBTData {
    NBTTagCompound toNBT();
    void fromNBT(NBTTagCompound nbt);
    boolean isValid(NBTTagCompound nbt);
}
