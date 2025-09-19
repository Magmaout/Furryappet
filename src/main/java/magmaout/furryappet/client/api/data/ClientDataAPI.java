package magmaout.furryappet.client.api.data;

import magmaout.furryappet.api.data.IDirtyCheck;
import magmaout.furryappet.api.data.INamedData;
import magmaout.furryappet.api.data.PlayerContainer;
import magmaout.furryappet.client.api.ClientBaseAPI;
import magmaout.furryappet.api.data.INBTData;
import magmaout.furryappet.network.api.data.FilesContainerSyncPacket;
import magmaout.furryappet.network.api.data.PlayerContainerSyncPacket;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ClientDataAPI extends ClientBaseAPI {
    protected final Map<String, Supplier<INBTData>> idToTypeMap = new HashMap<>();
    private final Map<String, ClientSyncContainer<? extends INBTData>> syncContainers = new HashMap<>();
    private final Map<String, WrapperFilesContainer<? extends INBTData>> filesWrapperContainers = new HashMap<>();
    private final Map<String, WrapperPlayerContainer<? extends INBTData>> playerWrapperContainers = new HashMap<>();

    public <T extends INBTData & IDirtyCheck> ClientSyncContainer<T> registerSyncContainer(String containerID, T instance) {
        if (syncContainers.containsKey(containerID))
            throw new RuntimeException("Container with id " + containerID + " already exists");

        ClientSyncContainer<T> syncContainer = new ClientSyncContainer<>(instance);
        syncContainers.put(containerID, syncContainer);
        return syncContainer;
    }
    public <T extends INBTData & INamedData> WrapperFilesContainer<T> registerFilesWrapperContainer(String containerID, Supplier<T> instanceCreator) {
        if (filesWrapperContainers.containsKey(containerID))
            throw new RuntimeException("Container with id " + containerID + " already exists");

        WrapperFilesContainer<T> filesWrapperContainer = new WrapperFilesContainer<>(containerID, instanceCreator);
        filesWrapperContainers.put(containerID, filesWrapperContainer);
        return filesWrapperContainer;
    }
    public <T extends INBTData> WrapperPlayerContainer<T> registerPlayerWrapperContainer(String containerID, Supplier<T> instanceCreator) {
        if (playerWrapperContainers.containsKey(containerID))
            throw new RuntimeException("Container with id " + containerID + " already exists");

        WrapperPlayerContainer<T> playerWrapperContainer = new WrapperPlayerContainer<>(containerID, instanceCreator);
        playerWrapperContainers.put(containerID, playerWrapperContainer);
        return playerWrapperContainer;
    }


    public void handleSyncData(String id, NBTTagCompound data) {
        ClientSyncContainer<?> clientSyncContainer = syncContainers.get(id);
        if(clientSyncContainer == null) {
            throw new RuntimeException("Container with id " + id + " does not exist");
        }
        clientSyncContainer.readNBT(data);
    }
    public WrapperFilesContainer<? extends INBTData> getFilesWrapperData(String id) {
        WrapperFilesContainer<? extends INBTData> wrapperFilesContainer = filesWrapperContainers.get(id);
        if(wrapperFilesContainer == null) {
            throw new RuntimeException("Container with id " + id + " does not exist");
        }
        return wrapperFilesContainer;
    }
    public WrapperPlayerContainer<? extends INBTData> getPlayerWrapperData(String id) {
        WrapperPlayerContainer<? extends INBTData> playerFilesContainer = playerWrapperContainers.get(id);
        if(playerFilesContainer == null) {
            throw new RuntimeException("Container with id " + id + " does not exist");
        }
        return playerFilesContainer;
    }
}
