package magmaout.furryappet.client.api;

import magmaout.furryappet.client.api.data.ClientDataAPI;
import magmaout.furryappet.client.api.huds.ClientHUDsAPI;

import java.util.HashMap;
import java.util.Map;

public class ClientAPIManager {
    protected static ClientAPIManager instance;
    private final Map<String, ClientBaseAPI> apis = new HashMap<>();

    private ClientDataAPI dataAPI;
    private ClientHUDsAPI hudsAPI;

    public void init() {

        dataAPI = new ClientDataAPI();
        hudsAPI = new ClientHUDsAPI(this);

        apis.put("data", dataAPI);
        apis.put("huds", hudsAPI);
    }
    public ClientDataAPI getDataAPI() {
        return dataAPI;
    }
    public ClientHUDsAPI getHUDsAPI() {
        return hudsAPI;
    }

    public <T extends ClientBaseAPI> T getAPI(String name, Class<T> type) {
        ClientBaseAPI api = apis.get(name);
        if (type.isInstance(api)) {
            return (T) api;
        }
        throw new IllegalArgumentException("API " + name + " is not of type " + type.getSimpleName());
    }

    public static ClientAPIManager getInstance() {
        if(instance == null) {
            throw new RuntimeException("Tried to get ClientAPIManager before it was initialized");
        }
        return instance;
    }
}
