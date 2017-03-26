package fr.badblock.bungeecord.plugins.others.commands;

import java.io.File;
import java.io.IOException;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

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
