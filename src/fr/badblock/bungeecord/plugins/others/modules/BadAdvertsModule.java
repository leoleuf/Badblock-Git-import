package fr.badblock.bungeecord.plugins.others.modules;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.bungeecord.plugins.others.modules.abstracts.Module;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BadAdvertsModule extends Module {

	@Getter
	@Setter
	private static BadAdvertsModule instance;

	private List<String> advertError = new ArrayList<>();
	private List<String> blockedFirstTopLevelDomainsList = new ArrayList<>();
	private List<String> excludedWebsitesList = new ArrayList<>();
	private List<String> blockedWebsitesList = new ArrayList<>();

	public BadAdvertsModule() {
		instance = this;
		Configuration configuration = BadBlockBungeeOthers.getInstance().getConfiguration();
		boolean change = false;
		if (configuration.get("modules.badAdverts.blockedFirstTopLevelDomains") == null) {
			blockedFirstTopLevelDomainsList.add(".fr");
			configuration.set("modules.badAdverts.blockedFirstTopLevelDomains", blockedFirstTopLevelDomainsList);
			change = true;
		}
		if (configuration.get("modules.badAdverts.excludedWebsites") == null) {
			excludedWebsitesList.add("badblock");
			configuration.set("modules.badAdverts.excludedWebsites", excludedWebsitesList);
			change = true;
		}
		if (configuration.get("modules.badAdverts.blockedWebsites") == null) {
			blockedWebsitesList.add("ascentia");
			configuration.set("modules.badAdverts.blockedWebsites", blockedWebsitesList);
			change = true;
		}
		if (configuration.get("modules.badAdverts.advertError") == null) {
			configuration.set("modules.badAdverts.advertError", advertError);
			change = true;
		}
		if (change) {
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
		advertError = configuration.getStringList("modules.badAdverts.advertError");
		blockedFirstTopLevelDomainsList = configuration.getStringList("modules.badAdverts.blockedFirstTopLevelDomains");
		excludedWebsitesList = configuration.getStringList("modules.badAdverts.excludedWebsites");
		blockedWebsitesList = configuration.getStringList("modules.badAdverts.blockedWebsites");
	}

	@SuppressWarnings("deprecation")
	public void testAdvert(ProxiedPlayer proxiedPlayer, ChatEvent event, String filteredMessage, String message) {
		if (proxiedPlayer.hasPermission("chat.bypass"))
			return;
		if (hasIPv4IP(message) || hasIPv4IP(filteredMessage)) {
			event.setCancelled(true);
			proxiedPlayer.sendMessage(BadBlockBungeeOthers.getInstance().getMessage(advertError));
			return;
		}
		for (String blockedWebsite : blockedWebsitesList)
			if (message.contains(blockedWebsite) || filteredMessage.contains(blockedWebsite)) {
				event.setCancelled(true);
				proxiedPlayer.sendMessage(BadBlockBungeeOthers.getInstance().getMessage(advertError));
				return;
			}
		for (String blockedFirstTopLevelDomain : blockedFirstTopLevelDomainsList) {
			if (message.contains(blockedFirstTopLevelDomain)) {
				if (message.equalsIgnoreCase(blockedFirstTopLevelDomain)) {
					event.setCancelled(true);
					proxiedPlayer.sendMessage(BadBlockBungeeOthers.getInstance().getMessage(advertError));
					return;
				}
				String[] splitter = message.split(blockedFirstTopLevelDomain);
				String domain = splitter[0];
				boolean cancelled = true;
				for (String excludedWebsite : excludedWebsitesList) {
					if (domain.endsWith(excludedWebsite)) {
						cancelled = false;
						break;
					}
				}
				if (cancelled) {
					event.setCancelled(cancelled);
					proxiedPlayer.sendMessage(BadBlockBungeeOthers.getInstance().getMessage(advertError));
				}
			}
		}
	}

	public static boolean hasIPv4IP(String text) {
		Pattern p = Pattern.compile(
				"^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
		Matcher m = p.matcher(text);
		return m.find();
	}

}
