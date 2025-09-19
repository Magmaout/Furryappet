package magmaout.furryappet.client.api.data;

import magmaout.furryappet.api.data.INBTData;
import magmaout.furryappet.network.Dispatcher;
import magmaout.furryappet.network.api.data.FilesContainerSyncPacket;
import magmaout.furryappet.network.api.data.PlayerContainerSyncPacket;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WrapperPlayerContainer<T extends INBTData> {
    private final String id;
    public final Supplier<T> instanceCreator;
    private final Map<Byte, Consumer<?>> messages = new HashMap<>();
    private byte counter = 0;
    public WrapperPlayerContainer(String id, Supplier<T> instanceCreator) {
        this.id = id;
        this.instanceCreator = instanceCreator;
    }

    public void loadData(String playerName, Consumer<T> callback) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("name", playerName);
        messages.put(counter, callback);
        Dispatcher.INSTANCE.sendToServer(new PlayerContainerSyncPacket(id, PlayerContainerSyncPacket.Action.LOAD, tag, counter));
        counter++;
    }
    public void saveData(String playerName, T data) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("name", playerName);
        tag.setTag("data", data.toNBT());
        Dispatcher.INSTANCE.sendToServer(new PlayerContainerSyncPacket(id, PlayerContainerSyncPacket.Action.SAVE, tag, counter));
        counter++;
    }
    public <R> void handleMessage(byte messageID, R data) {
        Consumer<R> objectCallable = (Consumer<R>) messages.get(messageID);
        objectCallable.accept(data);
    }
}
