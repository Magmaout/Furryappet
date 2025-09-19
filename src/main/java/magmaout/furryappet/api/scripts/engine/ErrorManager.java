package magmaout.furryappet.api.scripts.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ErrorManager {
    //TODO make something with this
    private final Map<String, FurryappetCompileException> compileExceptions = new HashMap<>();
    public void setCompileError(String script, FurryappetCompileException exception) {
        if(exception == null) {
            compileExceptions.remove(script);
            return;
        }
        compileExceptions.put(script, exception);
    }
    public void addScriptError(String script, FurryappetScriptException exception) {

    }

    public Set<String> getScriptsWithCompileError() {
        return compileExceptions.keySet();
    }
    public FurryappetCompileException getCompileError(String script) {
        return compileExceptions.get(script);
    }
}
