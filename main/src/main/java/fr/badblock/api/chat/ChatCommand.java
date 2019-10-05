package fr.badblock.api.chat;

import fr.badblock.api.BadBlockAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommand implements CommandExecutor {

    public static boolean isActivated = true;

    /** Chat Commands **/
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("wpmchat.admin"))
                if (label.equals("wpmchat")) {
                    if (args.length == 0) {
                        p.sendMessage("§9§m------------§r§bWPMChat§9§m------------");
                        p.sendMessage("§bSalut à toi §e" + p.getName() + "§b voici une liste des commandes !");
                        p.sendMessage("§9§l/wpmchat clear §c-> §bSupprime le chat !");
                        p.sendMessage("§9§l/wpmchat enable §c-> §bActive le chat !");
                        p.sendMessage("§9§l/wpmchat disable §c-> §bDésactive le chat !");
                        p.sendMessage("§9§m------------§r§bWPMChat§9§m------------");
                        return true;
                    }
                    /** Chat clear **/
                    if (args[0].equalsIgnoreCase("clear")) {
                        int lines = BadBlockAPI.getPluginInstance().getConfig().getInt("chat.lines");
                        for (int i = 0; i < lines; i++) {
                            Bukkit.broadcastMessage(" ");
                        }
                        String message = BadBlockAPI.getPluginInstance().getConfig().getString("chat.messageonclear");
                        Bukkit.broadcastMessage(ChatUtilities.f(message));
                        return true;
                    }
                    /** Chat enabling **/
                    if (args[0].equalsIgnoreCase("enable")) {
                        String enabled = BadBlockAPI.getPluginInstance().getConfig().getString("chat.msgenable");
                        p.sendMessage(ChatUtilities.f(enabled));
                        isActivated = true;
                        return true;
                    }
                    /** Chat disabling **/
                    if (args[0].equalsIgnoreCase("disable")) {
                        String disabled = BadBlockAPI.getPluginInstance().getConfig().getString("chat.msgdisable");
                        p.sendMessage(ChatUtilities.f(disabled));
                        isActivated = false;
                        return true;
                    }
                } else {
                    /** Sending message when the player doesn't have the permission **/
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