package magmaout.furryappet.api.states.save;

import magmaout.furryappet.api.states.States;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StatesCapability implements ICapabilitySerializable<NBTTagCompound> {
    @CapabilityInject(States.class)
    public static Capability<States> STATES = null;

    public States states = new States();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == STATES;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == STATES ? STATES.cast(states) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return states.toNBT(false);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        states.fromNBT(nbt, false);
    }
}
