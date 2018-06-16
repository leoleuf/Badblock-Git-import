package fr.badblock.bungeecord.plugins.others.modules;

import fr.badblock.bungeecord.plugins.others.modules.abstracts.Module;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PartyChatModule extends Module {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncChat(ChatEvent event) {
		Connection sender = event.getSender();
		if (!(sender instanceof ProxiedPlayer))
			return;
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if (event.isCommand())
			return;
		if (event.getMessage().startsWith("%")) {
			event.setCancelled(true);
			String finalMessage = event.getMessage().substring(1, event.getMessage().length());
			BungeeCord.getInstance().getPluginManager().dispatchCommand(player, "party msg " + finalMessage);
		}
	}

}
