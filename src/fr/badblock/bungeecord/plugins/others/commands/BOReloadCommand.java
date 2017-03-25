package fr.badblock.bungeecord.plugins.others.commands;

import java.io.File;
import java.io.IOException;

import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.plugin.Command;
import fr.badblock.bungeecord.config.Configuration;
import fr.badblock.bungeecord.config.ConfigurationProvider;
import fr.badblock.bungeecord.config.YamlConfiguration;
import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;

public class BOReloadCommand extends Command {
	
	public BOReloadCommand() {
		super("boreload", "bungeeothers.reload", "borl");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		try {
			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(BadBlockBungeeOthers.getInstance().getDataFolder(), "config.yml"));
			BadBlockBungeeOthers.getInstance().setConfiguration(configuration);
			sender.sendMessage("§aConfiguration rechargée !");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
