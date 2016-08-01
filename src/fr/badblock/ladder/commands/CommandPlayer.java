package fr.badblock.ladder.commands;

import java.util.UUID;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;

public class CommandPlayer extends Command {
	public CommandPlayer() {
		super("player", "ladder.command.player");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if(args.length < 3){
			sender.sendMessage(ChatColor.RED + "/player <player> repairuuid|onlinemode (<exactname/onlinemode>)"); return;
		}

		OfflinePlayer player = Ladder.getInstance().getOfflinePlayer(args[0]);

		if(player.getLastAddress() == null){
			sender.sendMessage(ChatColor.RED + "Le joueur ne s'est jamais connecté sur BadBlock !"); return;
		}

		if(args[1].equalsIgnoreCase("repairuuid")){
			UUID uniqueId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + args[2]).getBytes());
			if (!player.getData().has("uniqueId"))
				player.getData().addProperty("uniqueId", uniqueId.toString());
			player.saveData();
		} else if(args[1].equalsIgnoreCase("onlinemode")){
			boolean onlineMode = Boolean.parseBoolean(args[2]);

			player.getData().addProperty("onlineMode", onlineMode);
			player.saveData();
		}
	}
}
