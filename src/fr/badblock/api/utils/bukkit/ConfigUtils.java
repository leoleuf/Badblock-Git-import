package fr.badblock.api.utils.bukkit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtils {
	public static Location loadLocation(ConfigurationSection config, String node){
		ConfigurationSection c = config.getConfigurationSection(node);
		if(c == null){
			saveLocation(config, node, getDefaultLocation());
			return getDefaultLocation();
		} else {
			try {
				Location l = new Location(
						Bukkit.getWorld(c.getString("world")),
						c.getDouble("x"),
						c.getDouble("y"),
						c.getDouble("z"),
						(float) c.getDouble("yaw"),
						(float) c.getDouble("pitch")
				);
				return l;
			} catch(Exception e){
				return getDefaultLocation();
			}
		}
	}
	public static void saveLocation(ConfigurationSection config, String node, Location location){
		config.set(node + ".world", location.getWorld().getName());
		config.set(node + ".x", location.getX());
		config.set(node + ".y", location.getY());
		config.set(node + ".z", location.getZ());
		config.set(node + ".yaw", location.getYaw());
		config.set(node + ".pitch", location.getPitch());
	}
	private static Location getDefaultLocation(){
		return Bukkit.getWorlds().get(0).getSpawnLocation();
	}
	public static List<Location> getLocationList(FileConfiguration config, String node) {
		List<Location> result = new ArrayList<Location>();

		for (String key : config.getConfigurationSection(node).getKeys(false)) {
			result.add(loadLocation(config, node + "." + key));
		}
		return result;
	}
}
