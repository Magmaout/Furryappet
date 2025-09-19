package magmaout.furryappet.api.data;

import magmaout.furryappet.network.Dispatcher;
import magmaout.furryappet.network.api.data.SyncContainerSyncPacket;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.function.Supplier;

public class SyncContainer<T extends INBTData & IDirtyCheck> extends PlayerContainer<T> {
    private final String id;
    protected SyncContainer(Supplier<T> instanceCreator, String id) {
        super(instanceCreator);
        this.id = id;
    }
    public void sync(EntityPlayerMP player) {
        Dispatcher.INSTANCE.sendTo(new SyncContainerSyncPacket(id, getData(player).toNBT()), player);
    }
}
