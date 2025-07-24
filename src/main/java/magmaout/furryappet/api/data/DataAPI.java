package magmaout.furryappet.api.data;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.BaseAPI;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

public class DataAPI extends BaseAPI {
    private final HashMap<String, FilesContainer> fileContainers = new HashMap<>();
    private final HashMap<String, PlayerContainer<? extends INBTData>> playerContainers = new HashMap<>();


    private volatile Throwable lastTimerError = null;

    public DataAPI() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new ContainerCheckerTask(this), 0, 1000);
    }
    public <T extends INBTData & IDirtyCheck & INamedData> FilesContainer<T> registerFilesContainer(String subDir, Supplier<T> instanceCreator) {
        if(fileContainers.containsKey(subDir)) {
            throw new RuntimeException("Container with sub directory " + subDir + " already exists");
        }
        FilesContainer<T> filesContainer = new FilesContainer<>(Furryappet.furryappetAPIManager.getFurryappetDir().resolve(subDir), instanceCreator);
        fileContainers.put(subDir, filesContainer);
        return filesContainer;
    }
    public <T extends INBTData> PlayerContainer<T> registerPlayerContainer(String containerID, Supplier<T> instanceCreator) {
        if(playerContainers.containsKey(containerID)) {
            throw new RuntimeException("Container with id " + containerID + " already exists");
        }

        PlayerContainer<T> playerContainer = new PlayerContainer<>(instanceCreator);
        playerContainers.put(containerID, playerContainer);
        return playerContainer;
    }

    @Override
    public void update() {
        if(lastTimerError != null) {
            throw new RuntimeException("Exception in background container check", lastTimerError);
        }
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
    public static class FurryappetPlayerDataStorage {
        private final Map<PlayerContainer<? extends INBTData>, INBTData> dataMap = new HashMap<>();
        public <T extends INBTData> T getDataForContainer(PlayerContainer<T> container) {
            return (T) dataMap.computeIfAbsent(container, (cont) -> cont.instanceCreator.get());
        }

        public NBTTagCompound serializeNBT() {
            NBTTagCompound compound = new NBTTagCompound();
            for (Map.Entry<String, PlayerContainer<? extends INBTData>> entry : Furryappet.furryappetAPIManager.getDataAPI().playerContainers.entrySet()) {
                String key = entry.getKey();
                PlayerContainer<? extends INBTData> container = entry.getValue();
                if(!dataMap.containsKey(container)){
                    continue;
                }
                INBTData data = getDataForContainer(container);
                compound.setTag(key, data.toNBT());
            }
            return compound;
        }

        public void deserializeNBT(NBTTagCompound nbt) {
            for (String key : nbt.getKeySet()) {
                PlayerContainer<? extends INBTData> container = Furryappet.furryappetAPIManager.getDataAPI().playerContainers.get(key);
                if(container == null) {
                    continue;
                }
                NBTBase tag = nbt.getTag(key);
                INBTData data = container.instanceCreator.get();
                data.fromNBT(tag);

                dataMap.put(container, data);
            }
        }
    }


}
