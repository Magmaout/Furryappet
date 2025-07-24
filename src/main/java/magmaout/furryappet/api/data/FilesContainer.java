package magmaout.furryappet.api.data;

import magmaout.furryappet.api.scripts.Script;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FilesContainer<T extends INBTData & IDirtyCheck & INamedData> {
    private final Map<String, T> datas;
    private final Map<String, Long> datasTimestamps;
    private final Path baseDir;
    private final Supplier<T> instanceCreator;

    public FilesContainer(Path dir, Supplier<T> instanceCreator) {
        dir.toFile().mkdirs();
        this.baseDir = dir;
        this.datas = new HashMap<>();
        this.datasTimestamps = new HashMap<>();
        this.instanceCreator = instanceCreator;
        loadFiles();
    }

    public T getData(String name) {
        Path path = baseDir.resolve(name + ".dat");
        if (!Files.exists(path)) {
            datas.remove(name);
            datasTimestamps.remove(name);
            return null;
        }

        try {
            long currentTimestamp = Files.getLastModifiedTime(path).toMillis();

            if (isUpToDate(name, currentTimestamp)) {
                return loadFile(path, name, currentTimestamp);
            }
        } catch (IOException e) {
            return null;
        }

        return datas.get(name);
    }
    public boolean createData(String name) {
        if (!datas.containsKey(name)) return false;
        datas.put(name, instanceCreator.get());
        return saveData(name);
    }
    public boolean deleteData(String name) {
        datas.remove(name);
        datasTimestamps.remove(name);

        File file = baseDir.resolve(name + ".dat").toFile();
        return file.exists() && file.delete();
    }
    public boolean duplicateData(String name, String newName) {
        T original = datas.get(name);
        if (original == null || datas.containsKey(newName)) return false;

        T copy = instanceCreator.get();
        copy.fromNBT(original.toNBT());
        copy.setName(newName);
        datas.put(newName, copy);
        return saveData(newName);
    }
    public boolean renameData(String oldName, String newName) {
        if (!datas.containsKey(oldName) || datas.containsKey(newName)) return false;

        T script = datas.remove(oldName);
        datasTimestamps.remove(oldName);

        File oldFile = baseDir.resolve(oldName + ".dat").toFile();
        File newFile = baseDir.resolve(newName + ".dat").toFile();

        if (oldFile.exists() && oldFile.renameTo(newFile)) {
            script.setName(newName);
            datas.put(newName, script);
            return saveData(newName);
        } else {
            datas.put(oldName, script);
            return false;
        }
    }
    public Set<String> getDataNames() {
        return datas.keySet();
    }

    protected void checkChanges() {
        for (Map.Entry<String, T> entry : datas.entrySet()) {
            T data = entry.getValue();
            if (data.isDirty()) {
                saveData(data.getName());
            }
        }
        loadFiles();
    }
    protected void loadFiles() {
        try (Stream<Path> paths = Files.walk(baseDir, FileVisitOption.FOLLOW_LINKS)) {
            Set<String> loadedNames = new HashSet<>();

            paths.filter(path -> path.toFile().isFile() && path.toString().endsWith(".dat"))
                    .forEach((path -> {
                        try {
                            long lastModified = Files.getLastModifiedTime(path).toMillis();
                            String fileName = baseDir.relativize(path).toString().replace("\\", "/");
                            String dataName = fileName.substring(0, fileName.length() - 4);
                            loadedNames.add(dataName);


                            if (isUpToDate(dataName, lastModified)) {
                                return;
                            }

                            loadFile(path, dataName, lastModified);
                        } catch (Exception e) {
                            System.err.println("Data load error: " + path.getFileName());
                            e.printStackTrace();
                        }

                    }));

            datas.keySet().removeIf(name -> !loadedNames.contains(name));
            datasTimestamps.keySet().removeIf(name -> !loadedNames.contains(name));

        } catch (IOException e) {
            throw new RuntimeException("Datas load error", e);
        }
    }
    protected boolean saveData(String name) {
        T data = getData(name);
        if(data == null) return false;
        try {
            saveFile(baseDir.resolve(name + ".dat"), data);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean isUpToDate(String id, long timestamp) {
        Long knownTimestamp = datasTimestamps.get(id);
        return knownTimestamp != null && knownTimestamp.equals(timestamp);
    }
    private T loadFile(Path path, String name, long timestamp) throws IOException {
        NBTTagCompound nbt = CompressedStreamTools.read(new DataInputStream(Files.newInputStream(path)));
        T data = instanceCreator.get();

        if (!data.isValid(nbt)) {
            throw new IOException("Invalid data");
        }
        data.fromNBT(nbt);
        data.setName(name);

        datas.put(name, data);
        datasTimestamps.put(name, timestamp);

        return data;
    }
    private void saveFile(Path path, T data) throws IOException {
        NBTTagCompound nbt = (NBTTagCompound) data.toNBT();
        CompressedStreamTools.write(nbt, new DataOutputStream(Files.newOutputStream(path)));
        data.markClean();
        datas.put(data.getName(), data);
        datasTimestamps.put(data.getName(), Files.getLastModifiedTime(path).toMillis());
    }

}