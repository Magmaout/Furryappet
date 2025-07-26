package magmaout.furryappet.api;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.data.DataAPI;
import magmaout.furryappet.api.scripts.ScriptsAPI;
import magmaout.furryappet.api.states.StatesAPI;
import net.minecraftforge.common.DimensionManager;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class APIManager {
    private final Map<String, BaseAPI> apis = new HashMap<>();

    private DataAPI dataAPI;
    private StatesAPI statesAPI;
    private ScriptsAPI scriptsAPI;

    private Path furryappetDir;

    public void init() {
        furryappetDir = DimensionManager.getCurrentSaveRootDirectory().toPath().resolve(Furryappet.MODID);

        dataAPI = new DataAPI();
        statesAPI = new StatesAPI();
        scriptsAPI = new ScriptsAPI();

        apis.put("data", dataAPI);
        apis.put("states", statesAPI);
        apis.put("scripts", scriptsAPI);
    }
    public void update() {
        apis.values().forEach(BaseAPI::update);
    }

    public DataAPI getDataAPI() {
        return dataAPI;
    }

    public StatesAPI getStatesAPI() {
        return statesAPI;
    }

    public ScriptsAPI getScriptsAPI() {
        return scriptsAPI;
    }

    public BaseAPI getAPI(String name) {
        return apis.get(name);
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseAPI> T getAPI(String name, Class<T> type) {
        BaseAPI api = apis.get(name);
        if (type.isInstance(api)) {
            return (T) api;
        }
        throw new IllegalArgumentException("API " + name + " is not of type " + type.getSimpleName());
    }

    public Path getFurryappetDir() {
        return furryappetDir;
    }
}