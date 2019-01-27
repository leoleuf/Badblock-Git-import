package fr.customentity.badblockwarps;

import fr.customentity.badblockwarps.commands.WarpCommand;
import fr.customentity.badblockwarps.configuration.WarpsConfiguration;
import fr.customentity.badblockwarps.data.Warp;
import fr.customentity.badblockwarps.listeners.WarpListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

public class BadBlockWarps extends JavaPlugin {

    private static BadBlockWarps instance;
    private String prefix;

    private WarpsConfiguration warpsConfiguration;

    private HashMap<String, Warp> selectionHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        getLogger().log(Level.INFO, "BadBlockWarps > Enabled !");

        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("warp").setAliases(Arrays.asList("warps"));

        warpsConfiguration = new WarpsConfiguration();
        warpsConfiguration.setup();
        warpsConfiguration.loadWarps();

        prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.prefix-warp"));

        Bukkit.getPluginManager().registerEvents(new WarpListener(), this);
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
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages." + path));
    }

    public HashMap<String, Warp> getSelectionHashMap() {
        return selectionHashMap;
    }

    public static BadBlockWarps getInstance() {
        return instance;
    }

    public void updateSigns() {
        for(Warp warp : Warp.warps) {
            for(Location location : warp.getSigns()) {
                //TODO: Update Signs
            }
        }
    }

    public void copyWorld(File source, File target){
        try {
            ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                        target.mkdirs();
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyWorld(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
