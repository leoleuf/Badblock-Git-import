package fr.customentity.badblockwarps.configuration;

import fr.customentity.badblockwarps.BadBlockWarps;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by CustomEntity on 27/01/2019 for BadBlockWarps.
 */
public class WarpsConfiguration {

    public FileConfiguration warpsConfig;
    public File warpsFile;

    public void setup() {
        if (!BadBlockWarps.getInstance().getDataFolder().exists()) {
            BadBlockWarps.getInstance().getDataFolder().mkdir();
        }

        warpsFile = new File(BadBlockWarps.getInstance().getDataFolder(), "warps.yml");
        if(!warpsFile.exists()) {
            try {
                warpsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        warpsConfig = YamlConfiguration.loadConfiguration(warpsFile);
        if(!warpsConfig.contains("warps")) {
            warpsConfig.createSection("warps");
        }
        this.save();
    }

    public FileConfiguration get() {
        return warpsConfig;
    }

    public void save() {
        try {
            warpsConfig.save(warpsFile);
        } catch (IOException e) {
        }
    }

    public void reload() {
        warpsConfig = YamlConfiguration.loadConfiguration(warpsFile);
    }

    public void loadWarps() {

    }

    public void saveWarps() {

    }
}
