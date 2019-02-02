package fr.customentity.badblockwarps;

import fr.customentity.badblockwarps.commands.CEWCommand;
import fr.customentity.badblockwarps.commands.WarpCommand;
import fr.customentity.badblockwarps.configuration.WarpsConfiguration;
import fr.customentity.badblockwarps.data.Warp;
import fr.customentity.badblockwarps.listeners.WarpListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class BadBlockWarps extends JavaPlugin {

    private static BadBlockWarps instance;
    private String prefix;

    private WarpsConfiguration warpsConfiguration;

    private HashMap<String, Warp> selectionHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        getLogger().log(Level.INFO, "BadBlockWarps > Enabled !");

        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("warp").setAliases(Arrays.asList("warps"));

        getCommand("createemptyworld").setExecutor(new CEWCommand());
        getCommand("createemptyworld").setAliases(Arrays.asList("cew"));
        warpsConfiguration = new WarpsConfiguration();
        warpsConfiguration.setup();
        warpsConfiguration.loadWarps();

        prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.prefix-warp"));

        Bukkit.getPluginManager().registerEvents(new WarpListener(), this);
        updateSigns();
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "BadBlockWarps > Disabled !");

        warpsConfiguration.saveWarps();
    }

    public WarpsConfiguration getWarpsConfiguration() {
        return warpsConfiguration;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages." + path).replace("%prefix%", prefix));
    }

    public HashMap<String, Warp> getSelectionHashMap() {
        return selectionHashMap;
    }

    public static BadBlockWarps getInstance() {
        return instance;
    }

    public void updateSigns() {
        for (Warp warp : Warp.warps) {
            updateSigns(warp);
        }
    }

    public void updateSigns(Warp warp) {
        boolean isEnabled = warp.getLocation().getWorld() != null && warp.isEnabled();
        for (Location location : warp.getSigns()) {
            String state = isEnabled ? ChatColor.translateAlternateColorCodes('&', getConfig().getString("signs-state.enabled")) : ChatColor.translateAlternateColorCodes('&', getConfig().getString("signs-state.disabled"));
            Sign sign = (Sign) location.getBlock().getState();
            List<String> translated = translateColorInList(getConfig().getStringList("signs"));
            int i = 0;
            for (String str : translated) {
                sign.setLine(i, str.replace("%state%", state).replace("%warp%", warp.getName()));
                i++;
            }
            sign.update();
        }
    }


    public List<String> translateColorInList(List<String> list) {
        List<String> exportList = new ArrayList<>();
        for (String str : list) {
            exportList.add(ChatColor.translateAlternateColorCodes('&', str.replace("%prefix%", prefix)));
        }
        return exportList;
    }

    public String serializeLocation(Location location) {
        return location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
    }

    public Location deserializeLocation(String str) {
        String[] parts = str.split(":");
        String world = parts[0];
        int x = Integer.valueOf(parts[1]);
        int y = Integer.valueOf(parts[2]);
        int z = Integer.valueOf(parts[3]);

        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public String serializeLocationWithEye(Location location) {
        return location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    public String serializeWarpWithEye(Warp location) {
        return location.getWorld() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    public Location deserializeLocationWithEye(String str) {
        String[] parts = str.split(":");
        String world = parts[0];
        double x = Double.valueOf(parts[1]);
        double y = Double.valueOf(parts[2]);
        double z = Double.valueOf(parts[3]);
        float pitch = Float.valueOf(parts[4]);
        float yaw = Float.valueOf(parts[5]);

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }
}