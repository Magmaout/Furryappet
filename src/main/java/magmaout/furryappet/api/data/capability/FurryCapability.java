package magmaout.furryappet.api.data.capability;

import magmaout.furryappet.api.data.DataAPI;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FurryCapability implements ICapabilitySerializable<NBTTagCompound> {
    @CapabilityInject(DataAPI.FurryDataStorage.class)
    public static Capability<DataAPI.FurryDataStorage> FUR_CAP = null;

    public DataAPI.FurryDataStorage storage = new DataAPI.FurryDataStorage();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == FUR_CAP;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == FUR_CAP ? FUR_CAP.cast(storage) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return storage.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        storage.deserializeNBT(nbt);
    }
}