package fr.badblock.bungee.modules.staff.listeners;

import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PlayerDisconnectChatStaffListener extends BadListener
{
	
	public PlayerDisconnectChatStaffListener(Plugin plugin)
	{
		super(plugin);
	}

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
		ProxiedPlayer player = event.getPlayer();
		
		// We get the BadPlayer object
		BadPlayer badPlayer = BadPlayer.get(player);

		if (badPlayer == null)
		{
			return;
		}

		if (!badPlayer.hasPermission("bungee.command.chatstaff")) {
			return;
		}

		// Set raw prefix
		String rawChatPrefix = badPlayer.getRawChatPrefix();

		BungeeManager.getInstance().targetedTranslatedBroadcast("bungee.command.chatstaff",
				"bungee.commands.chatstaff.messageoffline", new int[] { 0 }, rawChatPrefix, badPlayer.getName());

	}

}