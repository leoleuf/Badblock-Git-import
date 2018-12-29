package fr.badblock.bungee.modules.staff.listeners;

import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.events.PlayerLoggedEvent;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PlayerLoginChatStaffListener extends BadListener
{
	
	public PlayerLoginChatStaffListener(Plugin plugin)
	{
		super(plugin);
	}


	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerLoggedEvent event) {
		// We get the BadPlayer object
		BadPlayer badPlayer = event.getBadPlayer();

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
				"bungee.commands.chatstaff.messageonline", new int[] { 0 }, rawChatPrefix, badPlayer.getName());

	}

}