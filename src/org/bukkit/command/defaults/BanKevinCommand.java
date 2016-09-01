package org.bukkit.command.defaults;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

@Deprecated
public class BanKevinCommand extends VanillaCommand {
	
	private static String		THE_KEVIN	= "sulfique";
	
    public BanKevinCommand() {
        super("bankevin");
        this.description = "Prevents the Kevin of ass from using this server";
        this.usageMessage = "/bankevin";
        this.setPermission("bukkit.command.bankevin.player");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        Bukkit.getBanList(BanList.Type.NAME).addBan(THE_KEVIN, "Kevin of ass", null, sender.getName());

        Player player = Bukkit.getPlayer(THE_KEVIN);
        if (player != null) {
            player.kickPlayer("Banned by admin.");
        }

        Command.broadcastCommandMessage(sender, "Banned player " + THE_KEVIN);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return ImmutableList.of();
    }
}
