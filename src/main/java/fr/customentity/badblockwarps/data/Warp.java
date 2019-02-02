package fr.customentity.badblockwarps.data;

import fr.customentity.badblockwarps.BadBlockWarps;
import org.bukkit.Bukkit;
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

    private String world;
    private double x, y, z;
    private float yaw, pitch;

    public Warp(String name, boolean enabled, List<Location> signs, Location location) {
        this.name = name;
        this.enabled = enabled;
        this.signs = signs;
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();

        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public Warp(String name, boolean enabled, List<Location> signs, String world, double x, double y, double z, float yaw, float pitch) {
        this.name = name;
        this.enabled = enabled;
        this.signs = signs;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;

        this.yaw = yaw;
        this.pitch = pitch;
    }

    public String getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public String getName() {
        return name;
    }

    public static boolean existWarp(String name) {
        return getWarpByName(name) != null;
    }

    public static void createWarp(String name, boolean enabled, List<Location> signs, Location location) {
        warps.add(new Warp(name, enabled, signs, location));
        BadBlockWarps.getInstance().getWarpsConfiguration().get().set("warps." + name, 0);
        BadBlockWarps.getInstance().getWarpsConfiguration().save();
    }

    public static void createWarp(String name, boolean enabled, List<Location> signs, String world, double x, double y, double z, float yaw, float pitch) {
        warps.add(new Warp(name, enabled, signs, world, x, y, z, yaw, pitch));
        BadBlockWarps.getInstance().getWarpsConfiguration().get().set("warps." + name, 0);
        BadBlockWarps.getInstance().getWarpsConfiguration().save();
    }

    public static void deleteWarp(Warp warp) {
        BadBlockWarps.getInstance().getWarpsConfiguration().get().set("warps." + warp.getName(), null);
        BadBlockWarps.getInstance().getWarpsConfiguration().save();
        warps.remove(warp);
    }

    public static List<Warp> getEnabledWarps() {
        List<Warp> enabledWarps = new ArrayList<>();
        warps.stream().filter(warp -> warp.isEnabled() && Bukkit.getWorld(warp.getWorld()) != null).forEach(enabledWarps::add);
        return enabledWarps;
    }

    public static List<Warp> getDisabledWarps() {
        List<Warp> enabledWarps = new ArrayList<>();
        warps.stream().filter(warp -> !warp.isEnabled() || Bukkit.getWorld(warp.getWorld()) == null).forEach(enabledWarps::add);
        return enabledWarps;
    }

    public List<Location> getSigns() {
        return signs;
    }

    public static void enableWarp(Warp warp) {
        warp.setEnabled(true);
        BadBlockWarps.getInstance().updateSigns(warp);
        BadBlockWarps.getInstance().getWarpsConfiguration().loadLocationWarp(warp);
    }

    public static void disableWarp(Warp warp) {
        warp.setEnabled(false);
        BadBlockWarps.getInstance().updateSigns(warp);
    }

    public void teleportPlayer(Player player) {
        player.teleport(getLocation());
    }

    public static Warp getWarpBySign(Location location) {
        for (Warp warp : warps) {
            for (Location locations : warp.getSigns()) {
                if (location.equals(locations)) {
                    return warp;
                }
            }
        }
        return null;
    }

    public static Warp getWarpByName(String name) {
        for (Warp warp : warps) {
            if (warp.getName().equalsIgnoreCase(name)) {
                return warp;
            }
        }
        return null;
    }
}
