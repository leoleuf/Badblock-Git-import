package fr.customentity.badblockwarps.configuration;

import fr.customentity.badblockwarps.BadBlockWarps;
import fr.customentity.badblockwarps.data.Warp;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        for(String str : get().getConfigurationSection("warps").getKeys(false)) {
            List<Location> locations = new ArrayList<>();
            for(String signsLocations : get().getStringList("warps." + str + ".signslocation")) {
                locations.add(BadBlockWarps.getInstance().deserializeLocation(signsLocations));
            }
            boolean state = get().getBoolean("warps." + str + ".state");
            Location location = BadBlockWarps.getInstance().deserializeLocationWithEye(get().getString("warps." + str + ".location"));

            Warp.createWarp(str, state, locations, location);
        }
    }

    public void saveWarps() {
        for(Warp warp : Warp.warps) {
            get().set("warps." + warp.getName() + ".location", BadBlockWarps.getInstance().serializeLocationWithEye(warp.getLocation()));
            get().set("warps." + warp.getName() + ".state", warp.isEnabled());
            List<String> locations = new ArrayList<>();
            for(Location location : warp.getSigns()) {
                locations.add(BadBlockWarps.getInstance().serializeLocation(location));
            }
            get().set("warps." + warp.getName() + ".signslocation", locations);
        }
        BadBlockWarps.getInstance().getWarpsConfiguration().save();
    }
}
