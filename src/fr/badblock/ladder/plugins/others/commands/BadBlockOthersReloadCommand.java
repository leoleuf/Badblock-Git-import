package fr.badblock.ladder.plugins.others.commands;

import java.io.File;
import java.io.IOException;

import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.config.Configuration;
import fr.badblock.ladder.api.config.ConfigurationProvider;
import fr.badblock.ladder.api.config.YamlConfiguration;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.plugins.others.BadBlockOthers;
import fr.badblock.ladder.plugins.others.utils.I18N;

public class BadBlockOthersReloadCommand extends Command {

	public BadBlockOthersReloadCommand() {
		super("bbothers", "badblock.others");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			BadBlockOthers instance = BadBlockOthers.getInstance();
			instance.reloadConfig();
			File configFile = new File(instance.getDataFolder(), "config.yml");
			try {
				instance.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Configuration configuration = instance.configuration;
			instance.help = configuration.getStringList("help");
			sender.sendMessages(I18N.getTranslatedMessages("config.reloaded"));
		}
	}

}
