package fr.badblock.bungee.modules.modo.listeners;

import fr.badblock.api.common.utils.bungee.Punished;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * The purpose of this class is to prevent muted players from being able to send
 * the message
 * 
 * @author xMalware
 *
 */
public class PunishmentMutedListener extends BadListener {

	public PunishmentMutedListener(Plugin plugin)
	{
		super(plugin);
	}
	
	/**
	 * When a message in the chat is sent
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChatEvent(ChatEvent event) {
		// We get the sender of the message
		Connection sender = event.getSender();

		// If the sender is not set
		if (sender == null) {
			// We stop there
			return;
		}

		// If the sender is not a player
		if (!(sender instanceof ProxiedPlayer)) {
			// Then we stop here.
			return;
		}

		// We get the player
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		// We get the BadPlayer object
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
		// We get punishment information
		Punished punished = badPlayer.getPunished();

		// If there is no punishment data
		if (punished == null) {
			// We stop there
			return;
		}

		// If the player is still muted
		if (punished.isMute()) {
			// So we cancel the message
			event.setCancelled(true);
			badPlayer.sendOutgoingMessage(badPlayer.getMuteMessage());
		}
	}

}