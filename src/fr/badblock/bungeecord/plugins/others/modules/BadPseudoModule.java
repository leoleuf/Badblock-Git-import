package fr.badblock.bungeecord.plugins.others.modules;

import fr.badblock.bungeecord.api.chat.TextComponent;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.event.ServerConnectEvent;
import fr.badblock.bungeecord.event.EventHandler;
import fr.badblock.bungeecord.plugins.others.modules.abstracts.Module;
import lombok.Getter;
import lombok.Setter;

public class BadPseudoModule extends Module {
	
	@Getter @Setter private static BadPseudoModule instance;	

	public BadPseudoModule() {
		instance = this;
	}
	
	@EventHandler
	public void onServerConnect(ServerConnectEvent event) {
		if (check(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	public boolean check(ProxiedPlayer player) {
		String playerName = player.getName();
		if (playerName.contains(" ") || playerName.contains("§") || playerName.contains("&")
				|| playerName.contains("[") || playerName.contains("]") || playerName.contains("(")
				|| playerName.contains(")") || playerName.contains("/") || playerName.contains("\\") || playerName.contains("º")) {
			player.disconnect(new TextComponent("§cPseudonyme invalide."));
			return true;
		}
		return false;
	}
	
}
