package fr.badblock.bungee.modules.modo.listeners;

import fr.badblock.api.common.utils.i18n.ChatColor;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.packets.event.InteractWindowEvent;
import fr.badblock.bungee.packets.window.Window;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class InventoryModWorkListener  extends BadListener {

	public InventoryModWorkListener(Plugin plugin)
	{
		super(plugin);
	}

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteractWindow(InteractWindowEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		Window window = event.getWindow();

		BadPlayer badPlayer = BadPlayer.get(player);

		if (!event.getWindow().equals(badPlayer.getOpenWindow()))
		{
			return;
		}

		String workTitle = badPlayer.getTranslatedMessage("bungee.commands.mod.work.inventory.name", null, "");

		if (!window.getTitle().getText().startsWith(workTitle))
		{
			return;
		}

		int slot = event.getSlot();
		String playerName = window.getTitle().getText().replace(workTitle, "");
		playerName = ChatColor.stripColor(playerName);

		BungeeManager bungeeManager = BungeeManager.getInstance();

		boolean online = bungeeManager.hasUsername(playerName);
		BadOfflinePlayer otherPlayer = bungeeManager.getBadOfflinePlayer(playerName);
		
		if (slot == 3)
		{
			badPlayer.closeInventory();
			ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "m records " + playerName);
			return;
		}

		if (slot == 1)
		{
			if (!online)
			{
				badPlayer.closeInventory();
				badPlayer.sendTranslatedOutgoingMessage("bungee.commands.mod.work.offline", null, playerName);
				return;
			}

			badPlayer.closeInventory();
			ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "m cp " + playerName);
			return;
		}

		if (slot == 2)
		{
			if (!online)
			{
				badPlayer.closeInventory();
				badPlayer.sendTranslatedOutgoingMessage("bungee.commands.mod.work.offline", null, playerName);
				return;
			}

			badPlayer.closeInventory();
			ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "m ghost " + playerName);
		}

		if (slot == 5)
		{
			badPlayer.closeInventory();
			ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "m warn " + playerName);
			return;
		}

		if (slot == 6)
		{
			badPlayer.closeInventory();
			ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "m kick " + playerName);
			return;
		}

		if (slot == 7)
		{
			if (otherPlayer.getPunished() != null && otherPlayer.getPunished().isMute())
			{
				badPlayer.closeInventory();
				ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "m unmute " + playerName);
			}
			else
			{
				badPlayer.closeInventory();
				ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "m mute " + playerName);
			}
			return;
		}

		if (slot == 8)
		{
			if (otherPlayer.getPunished() != null && otherPlayer.getPunished().isBan())
			{
				badPlayer.closeInventory();
				ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "m unban " + playerName);
			}
			else
			{
				badPlayer.closeInventory();
				ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "m ban " + playerName);
			}
		}
	}

}
