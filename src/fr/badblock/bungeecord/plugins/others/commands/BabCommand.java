package fr.badblock.bungeecord.plugins.others.commands;

import java.io.File;
import java.io.IOException;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.bungeecord.plugins.others.modules.BadInsultModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BabCommand extends Command {
	
	public BabCommand() {
		super("boaddspooker", "others.spymsg", "boaddspook", "boaspook", "boaspooker", "boas");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		try {
			if (args.length != 1) {
				sender.sendMessage("§cUtilisation: /boas <spooker>");
				return;
			}
			BadInsultModule.instance.insultsList.add(BadInsultModule.instance.applyFilter(args[0]));
			BadBlockBungeeOthers.getInstance().getConfiguration().set("modules.badInsult.insults", BadInsultModule.instance.insultsList);
			try {
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(BadBlockBungeeOthers.getInstance().getConfiguration(), new File(BadBlockBungeeOthers.getInstance().getDataFolder(), "config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}	
			sender.sendMessage("§aInsulte '" + BadInsultModule.instance.applyFilter(args[0]) + "' ajoutée.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}