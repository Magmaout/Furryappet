package magmaout.furryappet.api.data;

import magmaout.furryappet.api.data.capability.FurryCapability;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.function.Supplier;

public class PlayerContainer<T extends INBTData> {
    protected final Supplier<T> instanceCreator;

    protected PlayerContainer(Supplier<T> instanceCreator) {
        this.instanceCreator = instanceCreator;
    }
    public T getData(EntityPlayerMP player) {
        if (player == null) return null;
        DataAPI.FurryDataStorage capability = player.getCapability(FurryCapability.FUR_CAP, null);
        return capability == null ? null : capability.getDataForContainer(this);
    }
}
