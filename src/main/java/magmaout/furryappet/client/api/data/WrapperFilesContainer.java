package magmaout.furryappet.client.api.data;

import magmaout.furryappet.api.data.INBTData;
import magmaout.furryappet.api.data.INamedData;
import magmaout.furryappet.network.Dispatcher;
import magmaout.furryappet.network.api.data.FilesContainerSyncPacket;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WrapperFilesContainer<T extends INBTData & INamedData> {
    private final String id;
    public final Supplier<T> instanceCreator;
    private final Map<Byte, Consumer<?>> messages = new HashMap<>();
    private byte counter = 0;
    public WrapperFilesContainer(String id, Supplier<T> instanceCreator) {
        this.id = id;
        this.instanceCreator = instanceCreator;
    }
    public void createData(String name, Consumer<Boolean> callback) {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("name", name);
        messages.put(counter, callback);
        Dispatcher.INSTANCE.sendToServer(new FilesContainerSyncPacket(id, FilesContainerSyncPacket.Action.CREATE, nbtTagCompound, counter));
        counter++;
    }
    public void deleteData(String name, Consumer<Boolean> callback) {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("name", name);
        messages.put(counter, callback);
        Dispatcher.INSTANCE.sendToServer(new FilesContainerSyncPacket(id, FilesContainerSyncPacket.Action.DELETE, nbtTagCompound, counter));
        counter++;
    }
    public void duplicateData(String name, String newName, Consumer<Boolean> callback) {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("name", name);
        nbtTagCompound.setString("newName", newName);
        messages.put(counter, callback);
        Dispatcher.INSTANCE.sendToServer(new FilesContainerSyncPacket(id, FilesContainerSyncPacket.Action.DUPLICATE, nbtTagCompound, counter));
        counter++;
    }
    public void renameData(String oldName, String newName, Consumer<Boolean> callback) {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("name", oldName);
        nbtTagCompound.setString("newName", newName);
        messages.put(counter, callback);
        Dispatcher.INSTANCE.sendToServer(new FilesContainerSyncPacket(id, FilesContainerSyncPacket.Action.RENAME, nbtTagCompound, counter));
        counter++;
    }
    public void getDataNames(Consumer<Set<String>> callback) {
        messages.put(counter, callback);
        Dispatcher.INSTANCE.sendToServer(new FilesContainerSyncPacket(id, FilesContainerSyncPacket.Action.NAMES, new NBTTagCompound(), counter));
        counter++;
    }

    public void loadData(String name, Consumer<T> callback) {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("name", name);
        messages.put(counter, callback);
        Dispatcher.INSTANCE.sendToServer(new FilesContainerSyncPacket(id, FilesContainerSyncPacket.Action.LOAD, nbtTagCompound, counter));
        counter++;
    }
    public void saveData(T data) {
        Dispatcher.INSTANCE.sendToServer(new FilesContainerSyncPacket(id, FilesContainerSyncPacket.Action.SAVE, data.toNBT(), counter));
        counter++;
    }
    public <R> void handleMessage(byte messageID, R data) {
        Consumer<R> objectCallable = (Consumer<R>) messages.get(messageID);
        objectCallable.accept(data);
    }
}
