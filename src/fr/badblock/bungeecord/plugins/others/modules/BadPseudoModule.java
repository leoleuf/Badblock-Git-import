package fr.badblock.bungeecord.plugins.others.modules;

import fr.badblock.bungeecord.plugins.others.modules.abstracts.Module;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.event.EventHandler;

public class BadPseudoModule extends Module {

	@Getter
	@Setter
	private static BadPseudoModule instance;

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
		if (playerName.contains(" ") || playerName.contains("§") || playerName.contains("&") || playerName.contains("[")
				|| playerName.contains("]") || playerName.contains("(") || playerName.contains(")")
				|| playerName.contains("/") || playerName.contains("\\") || playerName.contains("º")) {
			player.disconnect(new TextComponent("§cPseudonyme invalide."));
			return true;
		}
		return false;
	}

}
