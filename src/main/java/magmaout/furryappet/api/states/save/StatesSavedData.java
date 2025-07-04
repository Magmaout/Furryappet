package magmaout.furryappet.api.states.save;

import magmaout.furryappet.api.states.States;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class StatesSavedData extends WorldSavedData {
    public States states = new States();
    private int lastHash = 0;
    public StatesSavedData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        states.deserializeNBT(nbt.getCompoundTag("states"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
       compound.setTag("states", states.serializeNBT());
       return compound;
    }

    @Override
    public boolean isDirty() {
        return lastHash != states.hashCode();
    }

    @Override
    public void setDirty(boolean isDirty) {
        if(!isDirty) {
            lastHash = states.hashCode();
        }
    }
}
