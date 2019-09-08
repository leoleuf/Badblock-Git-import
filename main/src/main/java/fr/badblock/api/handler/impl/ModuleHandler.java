package fr.badblock.api.handler.impl;

import fr.badblock.api.handler.Handler;
import fr.badblock.api.utils.ClassUtility;
import fr.badblock.api.module.Module;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ModuleHandler extends Handler {

    private List<Module> modules = new ArrayList<>();

    public ModuleHandler(JavaPlugin instance) {
        super("modules", instance);
    }

    public ModuleHandler(String modulePath, JavaPlugin instance) {
        this(instance);
        loadModulesFromPackage(instance, modulePath);
    }

    private void loadModulesFromPackage(JavaPlugin plugin, String packageName) {
        ClassUtility.getClassesInPackage(plugin, packageName).stream()
                .filter(this::isModule)
                .forEach(aClass -> {
                    try {
                        addModule((Module) aClass.newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private boolean isModule(Class<?> clazz) {
        return Module.class.isAssignableFrom(clazz);
    }

    public void addModule(Module module) {
        modules.add(module);
    }

    public void removeModule(Module module) {
        modules.remove(module);
    }

    public Module getModule(String moduleName) {
        return modules.stream()
                .filter(module -> module.getModuleName().equalsIgnoreCase(moduleName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void onEnable() {
        this.getModules().stream()
                .filter(module -> !module.isEnabled())
                .forEach(Module::enable);
    }

    @Override
    public void onDisable() {
        this.getModules().stream()
                .filter(Module::isEnabled)
                .forEach(Module::disable);
    }

    public List<Module> getModules() {
        return modules;
    }
}
