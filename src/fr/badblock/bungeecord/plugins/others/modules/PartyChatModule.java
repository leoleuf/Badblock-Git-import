package fr.badblock.bungeecord.plugins.others.modules;

import fr.badblock.bungeecord.BungeeCord;
import fr.badblock.bungeecord.api.connection.Connection;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.event.ChatEvent;
import fr.badblock.bungeecord.event.EventHandler;
import fr.badblock.bungeecord.event.EventPriority;
import fr.badblock.bungeecord.plugins.others.modules.abstracts.Module;

public class PartyChatModule extends Module {
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onAsyncChat(ChatEvent event) {
		Connection sender = event.getSender();
		if (!(sender instanceof ProxiedPlayer)) return;
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if (event.isCommand()) return;
		if (event.getMessage().startsWith("%")) {
			event.setCancelled(true);
			String finalMessage = event.getMessage().substring(1, event.getMessage().length());
			BungeeCord.getInstance().getPluginManager().dispatchCommand(player, "party msg " + finalMessage);
		}
	}
	
}
