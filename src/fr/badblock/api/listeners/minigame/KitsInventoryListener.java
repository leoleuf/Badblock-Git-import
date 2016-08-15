package fr.badblock.api.listeners.minigame;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.kit.Kit;
import fr.badblock.api.kit.KitsManager;
import fr.badblock.api.utils.bukkit.ChatUtils;

public class KitsInventoryListener implements Listener{
	private static final String DIAMONDSWORD_ITEM_NAME = ChatUtils.colorReplace("%gold%Cliquez pour choisir un %red%kit %gold%!");

	public static ItemStack getItem(){
		ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(DIAMONDSWORD_ITEM_NAME);
		item.setItemMeta(meta);

		return item;
	}
	public static boolean isItem(ItemStack is){
		return is.getType() == Material.DIAMOND_SWORD && DIAMONDSWORD_ITEM_NAME.equals(is.getItemMeta().getDisplayName());
	}
	public static Inventory generateInventory(BPlayer player){
		return KitsManager.getInstance().generateInventory(player);
	}
	@EventHandler
	public void onClickInventory(InventoryClickEvent e){
		if(e.getCurrentItem() == null) return;
		if(!KitsManager.INVENTORY_NAME.equals(e.getInventory().getName())) return;
		BPlayer player = BPlayersManager.getInstance().getPlayer(e.getWhoClicked());
		if(player == null || player.isSpectator()) return;
		if(e.getCurrentItem().getItemMeta() == null || e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

		Kit kit = KitsManager.getInstance().getByItem(e.getCurrentItem());
		if(kit != null){
			if(kit.isVIP() && !player.isVip()){
				player.sendMessage("%red%Vous devez être VIP pour obtenir ce kit !");
			} else if(kit.isVIP()){
				player.setUsedKit(kit);
				player.sendMessage("%green%Kit séléctionné !");
			} else if(!player.getKits().contains(kit.getBDDName())){
				if(player.getCoins() >= kit.getPrice()){
					player.removeCoins(kit.getPrice());
					if(!kit.isVIP() && kit.getPrice() > 0){
						player.sendMessage("%green%Vous avez acheté ce kit pour " + kit.getPrice() + " BadCoins !");
						player.getKits().add(kit.getBDDName());
					}
					player.setUsedKit(kit);
					player.sendMessage("%green%Kit séléctionné !");
				} else {
					player.sendMessage("%red%Vous n'avez pas assez de BadCoins pour obtenir ce kit !");
				}
			} else {
				player.setUsedKit(kit);
				player.sendMessage("%green%Kit séléctionné !");
			}
		}
		e.setCancelled(true);
		e.getWhoClicked().closeInventory();
	}

}
