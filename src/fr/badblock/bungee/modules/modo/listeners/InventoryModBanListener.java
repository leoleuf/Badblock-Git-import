package fr.badblock.bungee.modules.modo.listeners;

import java.util.List;
import java.util.Optional;

import fr.badblock.api.common.utils.i18n.ChatColor;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.packets.event.InteractWindowEvent;
import fr.badblock.bungee.packets.item.ItemStack;
import fr.badblock.bungee.packets.window.Window;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class InventoryModBanListener  extends BadListener {

	public InventoryModBanListener(Plugin plugin)
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

		String workTitle = badPlayer.getTranslatedMessage("bungee.commands.mod.ban.inventory_name", null, "");

		if (!window.getTitle().getText().startsWith(workTitle))
		{
			return;
		}

		Optional<ItemStack> optional = event.getClickedItem();

		if (!optional.isPresent())
		{
			return;
		}

		ItemStack stack = optional.get();
		
		if (stack == null || stack.getName() == null)
		{
			return;
		}

		String playerName = window.getTitle().getText().replace(workTitle, "");
		playerName = ChatColor.stripColor(playerName);

		List<String> lore = stack.getLore();

		if (lore == null || lore.isEmpty())
		{
			return;
		}
		
		String reason = lore.get(lore.size() - 1);

		badPlayer.closeInventory();
		ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "m ban " + playerName + " " + reason);
	}

}
