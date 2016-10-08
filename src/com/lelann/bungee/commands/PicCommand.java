package com.lelann.bungee.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

import com.lelann.bungee.BungeeUtils;

public class PicCommand extends Command{
	public PicCommand(){
		super("pic");
	}

	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("pic.viewpic")){
			sender.sendMessage(new ComponentBuilder("Vous n'avez pas la permission d'exécuter cette commande !").color(ChatColor.RED).create());
			return;
		}
		sender.sendMessage(new ComponentBuilder("Le pic de connecté aujourd'hui est de : " + BungeeUtils.CURRENT_TODAY + ".").color(ChatColor.GREEN).create());
		sender.sendMessage(new ComponentBuilder("Le record absolu est de : " + BungeeUtils.CURRENT_BEST + ".").color(ChatColor.GREEN).create());
	}
}
