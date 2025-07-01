package magmaout.furryappet.api.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class AbstractData implements INBTSerializable<NBTTagCompound> {
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}