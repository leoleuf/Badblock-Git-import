package fr.badblock.api.commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.badblock.api.BPlugin;
import fr.badblock.api.kit.Kit;

public class AddKitCommand extends AbstractCommand{
	public AddKitCommand(){
		super("minigames.admin", "%gold%Utilisation : /addkit <nom>", false);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		if(args.length == 0){
			sendHelp(sender);
		} else {
			Player player = (Player) sender;
			Kit kit = new Kit(player.getInventory(), args[0].toLowerCase());
			File file = new File(BPlugin.getInstance().getDataFolder(), "kits/" + args[0].toLowerCase() + ".yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			kit.save(config);
			
			try {
				config.save(file);
			} catch (IOException unused) {}
			sendMessage(sender, "%green%Ajouté !");
		}
	}
}
