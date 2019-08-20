package fr.badblock.api;

import fr.badblock.api.handler.Handler;
import fr.badblock.api.handler.impl.ModuleHandler;
import fr.badblock.api.module.Module;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class BadBlockAPI extends JavaPlugin {

    private List<Handler> handlers;
    private ModuleHandler moduleHandler;
    private static BadBlockAPI instance;

    @Override
    public void onEnable() {
        instance = this;
        moduleHandler = new ModuleHandler(this);
        enableModules();
    }

    @Override
    public void onDisable() {
        moduleHandler.getModules().forEach(this::disableModule);
    }


    public static BadBlockAPI getPluginInstance() {
        return instance;
    }

    /* MODULE SECTION */

    private void enableModules() {

    }

    private void enableModule(Module module) {
        moduleHandler.addModule(module);
        getModule(module.getModuleName()).enable();
    }

    private void disableModule(Module module) {
        getModule(module.getModuleName()).disable();
        moduleHandler.removeModule(module);
    }

    private Module getModule(String module) {
        return ((ModuleHandler) getHandler("modules")).getModule(module);
    }

    /* HANDLER SECTION */
    private Handler getHandler(String handlerName) {
        return handlers.stream()
                .filter(handler -> handler.getHandlerName().equalsIgnoreCase(handlerName))
                .findFirst()
                .orElse(null);
    }


}
