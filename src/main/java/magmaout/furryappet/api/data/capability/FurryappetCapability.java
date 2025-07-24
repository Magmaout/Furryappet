package magmaout.furryappet.api.data.capability;

import magmaout.furryappet.api.data.DataAPI;
import magmaout.furryappet.api.states.States;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FurryappetCapability implements ICapabilitySerializable<NBTTagCompound> {
    @CapabilityInject(DataAPI.FurryappetPlayerDataStorage.class)
    public static Capability<DataAPI.FurryappetPlayerDataStorage> FUR_CAP = null;
    private final EntityPlayerMP player;
    public FurryappetCapability(EntityPlayerMP owner) {
        player = owner;
    }

    public DataAPI.FurryappetPlayerDataStorage storage = new DataAPI.FurryappetPlayerDataStorage();

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
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("lastKnownName", player.getName());
        compound.setTag("data", storage.serializeNBT());
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        storage.deserializeNBT(nbt.getCompoundTag("data"));
    }
}