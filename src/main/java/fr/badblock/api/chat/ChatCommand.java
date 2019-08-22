package fr.badblock.api.chat;

import fr.badblock.api.BadBlockAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommand implements CommandExecutor {

    /** Chat Commands **/
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("wpmchat.admin"))
                if (label.equals("wpmchat")) {
                    if (args.length == 0) {
                        //TODO send help message(s)
                        p.sendMessage("");
                        return true;
                    }
                    /** Chat clear **/
                    if (args[0].equalsIgnoreCase("clear")) {
                        int lines = BadBlockAPI.getPluginInstance().getConfig().getInt("chat.lines");
                        for (int i = 0; i < lines; i++) {
                            Bukkit.broadcastMessage(" ");
                        }
                        String message = BadBlockAPI.getPluginInstance().getConfig().getString(ChatUtilities.f("chat.messageonclear"));
                        Bukkit.broadcastMessage(message);
                        return true;
                    }
                    /** Chat enabling **/
                    if (args[0].equalsIgnoreCase("enable")) {
                        String enabled = BadBlockAPI.getPluginInstance().getConfig().getString("chat.msgenable");
                        p.sendMessage(enabled);
                        BadBlockAPI.getPluginInstance().getConfig().set("chat.enabled", "true");
                        return true;
                    }
                    /** Chat disabling **/
                    if (args[0].equalsIgnoreCase("disable")) {
                        String disabled = BadBlockAPI.getPluginInstance().getConfig().getString("chat.msgdisable");
                        p.sendMessage(disabled);
                        BadBlockAPI.getPluginInstance().getConfig().set("chat.enabled", "false");
                        return true;
                    }
                } else {
                    String str = BadBlockAPI.getPluginInstance().getConfig().getString("commannd.nopermission");
                    p.sendMessage(str);
                    return true;
                }
        } else {
            System.out.println("[BadBlockMiniGameAPI]You have to be a player use this.");
        }
        return false;
    }
}
