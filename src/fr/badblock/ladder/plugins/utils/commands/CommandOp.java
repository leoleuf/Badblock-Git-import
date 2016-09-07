package fr.badblock.ladder.plugins.utils.commands;

import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;

public class CommandOp extends Command {
	public CommandOp() {
		super("op", null, "bukkit:op", "minecraft:op");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			sender.sendMessage(ChatColor.RED + "Utilisation : /op <joueur>");
		} else sender.sendMessage("Opped " + args[0] + "! (ou pas)");
	}
	
	@Override
	public boolean isBypassable(){
		return true;
	}
}