package fr.badblock.bungee.utils.commands;

import fr.badblock.bungee.utils.BungeeUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command{
	public ReloadCommand(){
		super("randomhub");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("randomhub.reload")) return;
		BungeeUtils.instance.loadConfig();		
		sender.sendMessage(ChatColor.GREEN + "Rechargé !");
	}
	
}
