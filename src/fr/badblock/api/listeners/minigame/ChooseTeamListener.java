package fr.badblock.api.listeners.minigame;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.game.Team;
import fr.badblock.api.game.Team.TeamType;
import fr.badblock.api.game.TeamsManager;
import fr.badblock.api.utils.bukkit.ChatUtils;

public class ChooseTeamListener implements Listener{
	private static final String INVENTORY_NAME = ChatUtils.colorReplace("%red%Choisit une équipe");
	private static final String WOOL_ITEM_NAME = ChatUtils.colorReplace("%gold%Cliquez pour choisir une %red%équipe %gold%!");

	public static ItemStack getItem(){
		ItemStack item = new ItemStack(Material.WOOL, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(WOOL_ITEM_NAME);
		item.setItemMeta(meta);

		return item;
	}
	public static boolean isItem(ItemStack is){
		return is.getType() == Material.WOOL && WOOL_ITEM_NAME.equals(is.getItemMeta().getDisplayName());
	}
	@SuppressWarnings("deprecation")
	public static Inventory generateInventory(){
		Collection<Team> teams = TeamsManager.getInstance().getTeams();
		int inventorySize = (int)(teams.size() / 9) * 9;
		if(teams.size() > inventorySize) inventorySize += 9;

		if(inventorySize > 54) inventorySize = 54;
		
		ItemStack[] items = new ItemStack[inventorySize];
		int i = 0;
		for(Team team : teams){
			ItemStack item = new ItemStack(Material.WOOL, team.getPlayers().size(), (short) 0, (byte) team.getType().getDyeColor().getData());
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatUtils.colorReplace(team.getType().getColor() + team.getType().getFrench()));
			meta.setLore(Arrays.asList(new String[]{"", ChatUtils.colorReplace("%red%> Clique pour rejoindre cette équipe")
					, ChatUtils.colorReplace("%red%> " + team.getPlayers().size() + " joueurs" + (team.isFull() ? " (pleine)" : ""))}));
			item.setItemMeta(meta);
			
			items[i] = item;
			i++;
		}
		Inventory inv = Bukkit.createInventory(null, inventorySize, INVENTORY_NAME);
		inv.setContents(items);
		
		return inv;
	}
	@EventHandler
	public void onClickInventory(InventoryClickEvent e){
		if(e.getCurrentItem() == null) return;
		if(!INVENTORY_NAME.equals(e.getInventory().getName())) return;
		BPlayer player = BPlayersManager.getInstance().getPlayer(e.getWhoClicked());
		if(player == null || player.isSpectator()) return;
		if(e.getCurrentItem().getItemMeta() == null || e.getCurrentItem().getItemMeta().getDisplayName() == null) return;
		
		TeamType type = TeamType.getByDyeColor(e.getCurrentItem());
		if(type != null && type.getTeam() != null){
			type.getTeam().enter(player);
		}
		
		e.setCancelled(true);
		e.getWhoClicked().closeInventory();
	}
}
