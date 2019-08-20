package fr.badblock.api.tech;

import fr.badblock.api.BadBlockAPI;

public abstract class Service {

    private String name;
    private Settings settings;
    private boolean dead;

    public Service(String name, Settings settings) {
        setName(name);
        setSettings(settings);
        BadBlockAPI.getPluginInstance().getLogger().info("[BadBlock-API] Service enregistrer: " + name);
    }

    public abstract void remove();

    public boolean isAlive() {
        return !isDead();
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setSettings(Settings settings) {
        this.settings = settings;
    }

    private boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Settings getSettings() {
        return settings;
    }

    public String getName() {
        return name;
    }
}
