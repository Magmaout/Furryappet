package magmaout.furryappet.client.api.data;

import magmaout.furryappet.api.data.IDirtyCheck;
import magmaout.furryappet.api.data.INBTData;
import net.minecraft.nbt.NBTTagCompound;

public class ClientSyncContainer<T extends INBTData & IDirtyCheck> {
    private final T data;

    protected ClientSyncContainer(T instance) {
        this.data = instance;
    }
    public T getData() {
        return data;
    }
    protected void readNBT(NBTTagCompound tag) {
        data.fromNBT(tag);
    }
}
