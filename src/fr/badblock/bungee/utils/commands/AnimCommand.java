package fr.badblock.bungee.utils.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class AnimCommand extends Command{
	public AnimCommand(){
		super("anim");
	}

	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage(ChatColor.RED + "La commande n'existe pas !");
	}
}
