package fr.customentity.badblockwarps.data;

import fr.customentity.badblockwarps.BadBlockWarps;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CustomEntity on 27/01/2019 for BadBlockWarps.
 */
public class Warp {

    public static List<Warp> warps = new ArrayList<>();

    private String name;
    private boolean enabled;
    private List<Location> signs;
    private Location location;

    public Warp(String name, boolean enabled, List<Location> signs, Location location) {
        this.name = name;
        this.enabled = enabled;
        this.signs = signs;
        this.location = location;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public static boolean existWarp(String name) {
        for(Warp warp : warps) {
            if(warp.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void createWarp(String name, boolean enabled, List<Location> signs, Location location) {
        warps.add(new Warp(name, enabled, signs, location));
        BadBlockWarps.getInstance().getWarpsConfiguration().get().set("warps." + name, 0);
    }

    public static void deleteWarp(Warp warp) {
        BadBlockWarps.getInstance().getWarpsConfiguration().get().set("warps." + warp.getName(), null);
        warps.remove(warp);
    }

    public static List<Warp> getEnabledWarps() {
        List<Warp> enabledWarps = new ArrayList<>();
        warps.stream().filter(Warp::isEnabled).forEach(enabledWarps::add);
        return enabledWarps;
    }

    public static List<Warp> getDisabledWarps() {
        List<Warp> enabledWarps = new ArrayList<>();
        warps.stream().filter(warp -> !warp.isEnabled()).forEach(enabledWarps::add);
        return enabledWarps;
    }

    public List<Location> getSigns() {
        return signs;
    }

    public static void enableWarp(Warp warp) {
        warp.setEnabled(true);
    }

    public static void disableWarp(Warp warp) {
        warp.setEnabled(false);
    }

    public void teleportPlayer(Player player) {
        player.teleport(location);
    }

    public static Warp getWarpByName(String name) {
        for(Warp warp : warps) {
            if(warp.getName().equals(name)) {
                return warp;
            }
        }
        return null;
    }
}
