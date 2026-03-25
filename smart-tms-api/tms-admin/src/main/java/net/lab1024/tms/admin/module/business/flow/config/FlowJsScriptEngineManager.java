package net.lab1024.tms.admin.module.business.flow.config;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/17 16:44
 */
public class FlowJsScriptEngineManager {

    private ScriptEngine javaScriptEngine;
    private ScriptEngineManager scriptEngineManager;
    private static final FlowJsScriptEngineManager INSTANCE = new FlowJsScriptEngineManager();

    private FlowJsScriptEngineManager() {
        this.scriptEngineManager = new ScriptEngineManager();
        this.javaScriptEngine = this.scriptEngineManager.getEngineByName("JavaScript");
    }

    public static final FlowJsScriptEngineManager getInstance() {
        return INSTANCE;
    }

    public ScriptEngine getJavaScriptEngine() {
        return javaScriptEngine;
    }

}
