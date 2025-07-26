package magmaout.furryappet.api.data.save;

import magmaout.furryappet.api.states.States;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class StatesSavedData extends WorldSavedData {
    public static final String ID = "states";
    public States states = new States();
    private int lastHash = 0;
    public StatesSavedData() {
        super(ID);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        states.fromNBT(tag, false);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        return states.toNBT(false);
    }

    @Override
    public boolean isDirty() {
        return lastHash != states.hashCode();
    }

    @Override
    public void setDirty(boolean isDirty) {
        if (!isDirty) lastHash = states.hashCode();
    }
}
