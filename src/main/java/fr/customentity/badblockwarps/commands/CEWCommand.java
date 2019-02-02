package fr.customentity.badblockwarps.commands;

import fr.customentity.badblockwarps.BadBlockWarps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by CustomEntity on 27/01/2019 for BadBlockWarps.
 */
public class CEWCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("badblock.admin")) return true;
        if(args.length == 0) {
            sender.sendMessage(BadBlockWarps.getInstance().getMessage("incorrect-arguments"));
        } else {
            String name = args[0];
            if(Bukkit.getWorld(name) != null) {
                sender.sendMessage("§cMonde déjà existant !");
                return true;
            }
            if(name.equalsIgnoreCase("MondeVide")) {
                sender.sendMessage("§cBien essayé Sulfique mais cette fois ça ne passera pas !");
                return true;
            }
            File from = new File(BadBlockWarps.getInstance().getDataFolder() + "/maps/MondeVide/");
            File to = new File(Bukkit.getServer().getWorldContainer() + "/" + name);

            copyWorld(from, to);
            Bukkit.createWorld(new WorldCreator(name));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv import " + name + " normal");
            ((Player)sender).teleport(new Location(Bukkit.getWorld(name), 0,50,0));
        }
        return false;
    }

    private void copyWorld(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
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
