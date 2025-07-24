package magmaout.furryappet.api.scripts.engine;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptFunctionData;
import magmaout.furryappet.api.scripts.ScriptsAPI;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LoadedScript {
    private static final NashornScriptEngineFactory scriptEngineManager = new NashornScriptEngineFactory();
    public final String name;
    private final ScriptsAPI api;
    public final NashornScriptEngine engine;
    public LoadedScript(String name, String code, ScriptsAPI api) throws FurryappetCompileException {
        this.name = name;
        this.api = api;

        NashornScriptEngine engine = (NashornScriptEngine) scriptEngineManager.getScriptEngine("--language=es6", "-scripting");

        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);

        bindings.remove("load");
        bindings.remove("loadWithNewGlobal");
        bindings.remove("exit");
        bindings.remove("quit");

        engine.getContext().setAttribute("javax.script.filename", name, ScriptContext.ENGINE_SCOPE);

        try {
            engine.eval(code);
        } catch (ScriptException e) {
            throw new FurryappetCompileException(e.getMessage(), e.getLineNumber(), e.getColumnNumber());
        }

        this.engine = engine;
    }

    public Object invokeFunction(String function, Object... args) throws FurryappetScriptException {
        try {
            return engine.invokeFunction(function, args);
        } catch (NoSuchMethodException e) {
            throw new FurryappetScriptException("Method " + function + " is not found in script " + this.name);
        } catch (ScriptException e) {

            FurryappetScriptException exception = new FurryappetScriptException(e.getMessage());
            exception.setStackTrace(e.getCause().getStackTrace());
            simplifyStackTrace(exception);
            throw exception;
        } catch (Exception e) {
            simplifyStackTrace(e);
            throw e;
        }
    }

    private void simplifyStackTrace(Exception exception) {
        boolean delete = false;
        List<StackTraceElement> list = new ArrayList<>();
        for (StackTraceElement e : exception.getStackTrace()) {
            if(e.getClassName().startsWith("jdk.nashorn.internal.scripts.Script")) {
                e = new StackTraceElement("furryappet.scripts." + name, e.getMethodName(), name, e.getLineNumber());
                delete = false;
            }
            if((e.getClassName().startsWith("jdk.nashorn.internal.scripts.Script") && e.getMethodName().equals(":program")) || (e.getClassName().startsWith(ScriptFunctionData.class.getName()) && e.getMethodName().equals("invoke")) || (e.getClassName().startsWith(ECMAErrors.class.getName()) && e.getMethodName().equals("error"))) {
                delete = true;
            }
            if(e.getClassName().equals(LoadedScript.class.getName()) && e.getMethodName().equals("invokeFunction")) {
                delete = false;
            }


            if(!delete) {
                list.add(e);
            }
        }
        exception.setStackTrace(list.toArray(new StackTraceElement[0]));
    }

    public Set<String> getFunctions() {
        return (Set<String>) engine.getBindings(ScriptContext.ENGINE_SCOPE);
    }
}
