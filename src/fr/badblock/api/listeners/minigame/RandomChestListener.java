package fr.badblock.api.listeners.minigame;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.utils.maths.NumberUtils;

public class RandomChestListener implements Listener{
	private List<Integer> probabilities = new ArrayList<Integer>();
	private List<ItemStack> items = new ArrayList<ItemStack>();
	private static List<String> openedChests = new ArrayList<String>();

	public void clear(){
		openedChests.clear();
	}
	public ItemStack[] getRandomInventory(){
		List<Integer> usedSlots = new ArrayList<Integer>();
		ItemStack[] inventory = new ItemStack[3 * 9];
		int nbrItems = 0, max = NumberUtils.random(3, 8);
		for(int i=0;i<items.size();i++){
			if(NumberUtils.random(1, probabilities.get(i)) == 1){
				if(nbrItems < max){
					int slot = getRandomCase(usedSlots);
					usedSlots.add(slot);
					inventory[slot] = items.get(i);
					nbrItems++;
				}
			}
		}
		return inventory;
	}
	public int getRandomCase(List<Integer> usedSlots){
		int i = -1;
		while(i == - 1){
			int slot = (int)(Math.random() * (3*9));
			if(!usedSlots.contains(slot)){
				i = slot;
			}
		}
		return i;
	}
	public RandomChestListener(FileConfiguration c){
		if(c.get("items") != null)	{	
			for(String key : c.getConfigurationSection("items").getKeys(false)){
				String id = c.getString("items." + key + ".id");
				int data = c.getInt("items." + key + ".data");
				byte durability = (byte)c.getInt("items." + key + ".durability");
				int amount = c.getInt("items." + key + ".amount");

				ItemStack is = getItem(id, data, amount);
				if(durability != 0){
					is.setDurability(durability);
				}
				if(is == null) continue;

				int probability = c.getInt("items." + key + ".probability");
				if(probability == 0) continue;

				items.add(is);
				probabilities.add(probability);
			}
		} else {
			c.set("exp.id", "1");
			c.set("exp.data", 0);
			c.set("exp.amount", 64);
			c.set("exp.probability", 1);
			items.add(getItem("STONE", 0, 64));
			probabilities.add(1);
		}
	}
	@SuppressWarnings("deprecation")
	public ItemStack getItem(String id, int data, int amount){
		if(id == null || amount == 0) return null;
		Material m = Material.matchMaterial(id);
		if(m != null)
			return new ItemStack(m, amount, (short)0, (byte) data); 
		else return null;
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		BPlayer player = BPlayersManager.getInstance().getPlayer(e.getPlayer());
		if(player == null || player.isSpectator()) return;
		
		if(e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CHEST){
			String str = locationToString(e.getClickedBlock().getLocation());
			if(!openedChests.contains(str)){
				openedChests.add(str);
				Chest c = (Chest) e.getClickedBlock().getState();
				c.getInventory().setContents(getRandomInventory());
				c.update();
			}
			e.setUseInteractedBlock(Result.ALLOW);
		}
	}
	private String locationToString(Location l){
		return (int) l.getX() + " " + (int) l.getY() + " " + (int) l.getZ();
	}
}
