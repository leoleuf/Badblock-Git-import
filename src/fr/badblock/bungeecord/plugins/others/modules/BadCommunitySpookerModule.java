package fr.badblock.bungeecord.plugins.others.modules;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.bungeecord.plugins.others.modules.abstracts.Module;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BadCommunitySpookerModule extends Module {

	@Getter
	@Setter
	public static BadCommunitySpookerModule instance = null;

	public List<String> badWordsList = new ArrayList<>();

	public BadCommunitySpookerModule() {
		instance = this;
		Configuration configuration = BadBlockBungeeOthers.getInstance().getConfiguration();
		if (configuration.get("modules.badCommunitySpooker.badWords") == null) {
			badWordsList.add("ramasse");
			badWordsList.add("rammasse");
			badWordsList.add("forcefield");
			badWordsList.add(" ff");
			badWordsList.add("ff ");
			badWordsList.add("force field");
			badWordsList.add("field");
			badWordsList.add("aura");
			badWordsList.add("killaura");
			badWordsList.add("kill aura");
			badWordsList.add("modo");
			badWordsList.add("ez");
			badWordsList.add("easy");
			badWordsList.add("ptn");
			badWordsList.add("putain");
			badWordsList.add("izi");
			badWordsList.add("spam");
			badWordsList.add("bow");
			badWordsList.add("rage");
			badWordsList.add("cheater");
			badWordsList.add("cheat");
			badWordsList.add("chit");
			badWordsList.add("cheta");
			configuration.set("modules.badCommunitySpooker.badWords", badWordsList);
			try {
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration,
						new File(BadBlockBungeeOthers.getInstance().getDataFolder(), "config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				configuration = ConfigurationProvider.getProvider(YamlConfiguration.class)
						.load(new File(BadBlockBungeeOthers.getInstance().getDataFolder(), "config.yml"));
				BadBlockBungeeOthers.getInstance().setConfiguration(configuration);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		badWordsList = configuration.getStringList("modules.badCommunitySpooker.badWords");
		List<String> strings = new ArrayList<>();
		badWordsList.forEach(word -> strings.add(BadInsultModule.instance.applyFilter(word.toLowerCase())));
		badWordsList = strings;
	}

	public boolean testSpooking(ProxiedPlayer proxiedPlayer, ChatEvent event) {
		if (event.isCommand())
			return false;
		if (proxiedPlayer.hasPermission("chat.bypass"))
			return false;
		String lowerMessage = BadInsultModule.instance.applyFilter(event.getMessage());
		for (String badword : badWordsList) {
			if (lowerMessage.contains(badword) || lowerMessage.equalsIgnoreCase(badword)) {
				event.setCancelled(true);
				return true;
			}
		}
		return false;
	}

}
