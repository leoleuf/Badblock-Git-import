package fr.badblock.api.module;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public abstract class Module {

    private String moduleName;
    private boolean enabled = false;
    private final Set<Listener> listeners = new HashSet<>();

    private JavaPlugin instance;

    public Module(String moduleName, JavaPlugin instance) {
        this.instance = instance;
        this.moduleName = moduleName;

        instance.getLogger().info(String.format("Module '%s' correctement enregistrer !", moduleName));
    }

    /**
     * Cette fonction permet d'activer le module
     * et de register les listeners si il y en a.
     */
    public void enable() {
        onEnable();
        setEnabled(true);
        listeners.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, instance));
        instance.getLogger().info(String.format("Module '%s' correctement activer", moduleName));
    }

    /**
     * Cette fonction permet de désactiver le module
     * et de détruire tous les listeners qu'il contient.
     */
    public void disable() {
        listeners.forEach(HandlerList::unregisterAll);
        listeners.clear();
        onDisable();
        setEnabled(false);

        instance.getLogger().info(String.format("Module '%s' correctement desactiver", moduleName));
    }


    abstract void onEnable();

    abstract void onDisable();

    private String getModuleStatus() {
        return isEnabled() ? "§aActivé" : "§cDésactivé";
    }

    public String getModuleName() {
        return moduleName;
    }


    private void setEnabled(boolean statue) {
        this.enabled = statue;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
