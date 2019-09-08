package fr.badblock.api.handler;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class Handler {

    private String handlerName;

    private JavaPlugin instance;

    public Handler(String handlerName, JavaPlugin instance) {
        this.instance = instance;
        this.handlerName = handlerName;

        instance.getLogger().info(String.format("Handler '%s' correctement enregistrer !", handlerName));
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public void enable() {
        onEnable();
        instance.getLogger().info(String.format("Handler '%s' correctement activer !", this.handlerName));
    }

    public void disable() {
        onDisable();
        instance.getLogger().info(String.format("Handler '%s' correctement desactiver !", this.handlerName));
    }

    public String getHandlerName() {
        return handlerName;
    }

}
