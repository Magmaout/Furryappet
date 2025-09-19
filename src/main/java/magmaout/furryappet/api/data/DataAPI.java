package magmaout.furryappet.api.data;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.BaseAPI;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

public class DataAPI extends BaseAPI {
    private final HashMap<String, FilesContainer> fileContainers = new HashMap<>();
    private final HashMap<String, PlayerContainer> playerContainers = new HashMap<>();

    private volatile Throwable lastTimerError = null;

    public DataAPI() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new ContainerCheckerTask(this), 0, 1000);
    }
    public <T extends INBTData & IDirtyCheck & INamedData> FilesContainer<T> registerFilesContainer(String subDir, Supplier<T> instanceCreator) {
        if (fileContainers.containsKey(subDir))
            throw new RuntimeException("Container with sub directory " + subDir + " already exists");

        FilesContainer<T> filesContainer = new FilesContainer<>(Furryappet.APIManager.getFurryappetDir().resolve(subDir), instanceCreator);
        fileContainers.put(subDir, filesContainer);
        return filesContainer;
    }

    public <T extends INBTData> PlayerContainer<T> getPlayerContainer(String containerID) {
        if (!playerContainers.containsKey(containerID))
            throw new RuntimeException("Container with id " + containerID + " does not exist");

        return playerContainers.get(containerID);
    }
    public <T extends INBTData & IDirtyCheck & INamedData> FilesContainer<T> getFilesContainer(String containerID) {
        if (!fileContainers.containsKey(containerID))
            throw new RuntimeException("Container with id " + containerID + " does not exist");

        return fileContainers.get(containerID);
    }
    public <T extends INBTData> PlayerContainer<T> registerPlayerContainer(String containerID, Supplier<T> instanceCreator) {
        if (playerContainers.containsKey(containerID))
            throw new RuntimeException("Container with id " + containerID + " already exists");

        PlayerContainer<T> playerContainer = new PlayerContainer<>(instanceCreator);
        playerContainers.put(containerID, playerContainer);
        return playerContainer;
    }
    public <T extends INBTData & IDirtyCheck> SyncContainer<T> registerSyncPlayerContainer(String containerID, Supplier<T> instanceCreator) {
        if (playerContainers.containsKey(containerID))
            throw new RuntimeException("Container with id " + containerID + " already exists");

        SyncContainer<T> playerContainer = new SyncContainer<>(instanceCreator, containerID);
        playerContainers.put(containerID, playerContainer);
        return playerContainer;
    }

    @Override
    public void update() {
        if (lastTimerError != null) throw new RuntimeException("Exception in background container check", lastTimerError);
        playerContainers.forEach((id, container) -> {
            if(container instanceof SyncContainer) {
                for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
                    IDirtyCheck data = ((SyncContainer<? extends IDirtyCheck>) container).getData(player);
                    if(data.isDirty()) {
                        data.markClean();
                        ((SyncContainer<? extends IDirtyCheck>) container).sync(player);
                    }
                }
            }
        });
    }

    private static class ContainerCheckerTask extends TimerTask {
        private final WeakReference<DataAPI> selfReference;

        public ContainerCheckerTask(DataAPI self) {
            this.selfReference = new WeakReference<>(self);
        }

        @Override
        public void run() {
            DataAPI self = selfReference.get();
            if (self == null) {
                cancel();
                return;
            }
            try {
                self.fileContainers.forEach((key, value) -> value.checkChanges());
            } catch (Throwable t) {
                self.lastTimerError = t;
            }
        }
    }
    public static class FurryDataStorage {
        private final Map<PlayerContainer<? extends INBTData>, INBTData> map = new HashMap<>();
        public <T extends INBTData> T getDataForContainer(PlayerContainer<T> container) {
            return (T) map.computeIfAbsent(container, (cont) -> cont.instanceCreator.get());
        }

        public NBTTagCompound serializeNBT() {
            NBTTagCompound compound = new NBTTagCompound();
            for (Map.Entry<String, PlayerContainer> entry : Furryappet.APIManager.getDataAPI().playerContainers.entrySet()) {
                String key = entry.getKey();
                PlayerContainer<? extends INBTData> container = entry.getValue();
                if(!map.containsKey(container)){
                    continue;
                }
                INBTData data = getDataForContainer(container);
                compound.setTag(key, data.toNBT());
            }
            return compound;
        }

        public void deserializeNBT(NBTTagCompound nbt) {
            for (String key : nbt.getKeySet()) {
                PlayerContainer<? extends INBTData> container = Furryappet.APIManager.getDataAPI().playerContainers.get(key);
                if (container == null) continue;

                NBTTagCompound tag = (NBTTagCompound) nbt.getTag(key);
                INBTData data = container.instanceCreator.get();
                data.fromNBT(tag);

                map.put(container, data);
            }
        }
    }
}