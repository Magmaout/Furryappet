 package magmaout.furryappet.api.scripts;

import magmaout.furryappet.api.APIManager;
import magmaout.furryappet.api.BaseAPI;
import magmaout.furryappet.api.data.FilesContainer;
import magmaout.furryappet.api.scripts.engine.ErrorManager;
import magmaout.furryappet.api.scripts.engine.FurryappetCompileException;
import magmaout.furryappet.api.scripts.engine.LoadedScript;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

 public class ScriptsAPI extends BaseAPI {
    private final Map<String, LoadedScript> loadedScripts;
    private final FilesContainer<Script> scripts;
    private final ErrorManager errorManager;

    public ScriptsAPI(APIManager api) {
        loadedScripts = new HashMap<>();
        scripts = api.getDataAPI().registerFilesContainer("scripts", Script::new);
        errorManager = new ErrorManager();

        for (String scriptName : scripts.getDataNames()) {
            loadScript(scriptName);
        }
    }

    public LoadedScript getLoadedScript(String name) {
        return loadedScripts.get(name);
    }
    public Set<String> getAllLoadedScripts() {
        return loadedScripts.keySet();
    }

    public boolean loadScript(String name) {
        errorManager.setCompileError(name, null);
        Script script = scripts.getData(name);
        if (script == null) {
            errorManager.setCompileError(name, new FurryappetCompileException(name + " Expected file '" + name + ".dat' but found nothing", -1, -1));
            return false;
        }
        try {
            loadedScripts.put(name, new LoadedScript(script.name, script.code));
        } catch (FurryappetCompileException e) {
            errorManager.setCompileError(name, e);
            return false;
        }
        return true;
    }
    public boolean unloadScript(String name) {
        errorManager.setCompileError(name, null);
        return loadedScripts.remove(name) != null;
    }

    public ErrorManager getErrorManager() {
        return errorManager;
    }
    public FilesContainer<Script> getScriptStorage() {
        return scripts;
    }
}
