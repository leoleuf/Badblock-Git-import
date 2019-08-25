package fr.badblock.api.command;

import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.data.player.PlayerBean;
import fr.badblock.api.data.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.hasPermission("admin.perm")){
                if (args.length == 0) {
                    //TODO send help message(s)
                    p.sendMessage("");
                    return true;
                }
                if (args[0].equalsIgnoreCase("user")) {
                    if(args.length == 1){
                        //TODO send help message(s)
                        p.sendMessage("nique ta mere");
                        return true;
                    }
                    if(args[1].equalsIgnoreCase("add")){

                    }
                    if(args[1].equalsIgnoreCase("remove")){

                    }
                    if(args[1].equalsIgnoreCase("list")) {
                        if (args.length == 2) {
                            String target = args[2];
                            if (target == null) {

                            }
                            PlayerData playerData = BadBlockAPI.getPluginInstance().getPlayerManager().getPlayerData(target);
                        }
                    } else {
                        p.sendMessage("");
                        return true;
                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("rank")) {

                    return true;
                }
            } else {
                String str = BadBlockAPI.getPluginInstance().getConfig().getString("commannd.nopermission");
                p.sendMessage(str);
                return true;
            }
        } else {
            System.out.println("[BadBlockGamesAPI]You've to be a player to use this command.");
            return true;
        }
        return false;
    }
}
