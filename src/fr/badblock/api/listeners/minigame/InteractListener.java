package fr.badblock.api.listeners.minigame;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.utils.bukkit.ChatUtils;

public class InteractListener implements Listener{
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(e.getItem() == null || e.getItem().getItemMeta() == null || e.getItem().getItemMeta().getDisplayName() == null) return;
		final BPlayer player = BPlayersManager.getInstance().getPlayer(e.getPlayer());
		if(player == null || player.isSpectator()) return;

		if(ChooseTeamListener.isItem(e.getItem())){
			e.getPlayer().openInventory(ChooseTeamListener.generateInventory());
			e.setCancelled(true);
		} else if(KitsInventoryListener.isItem(e.getItem())){
			Inventory inv = KitsInventoryListener.generateInventory(player);
			if(inv != null)
				e.getPlayer().openInventory(inv);
			e.setCancelled(true);
		} else if(isPaper(e.getItem())){
			VoteListener.openInventory(e.getPlayer());
			e.setCancelled(true);
		}
	}
	public static ItemStack getPaper(){
		ItemStack is = new ItemStack(Material.PAPER, 1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatUtils.colorReplace("%gold%Cliquer pour voter pour une map !"));
		is.setItemMeta(im);
		return is;
	}
	public static boolean isPaper(ItemStack is){
		if(is == null) return false;
		if(is.getType() == Material.PAPER) return true;

		return false;
	}
}
