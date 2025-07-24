package magmaout.furryappet.api.data;

import net.minecraft.nbt.NBTBase;

public interface INBTData {
    NBTBase toNBT();
    void fromNBT(NBTBase nbt);
    boolean isValid(NBTBase nbt);
}
