package fr.badblock.api.kit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.badblock.api.BPlugin;
import fr.badblock.api.commands.AddKitCommand;
import fr.badblock.api.commands.CommandsManager;
import fr.badblock.api.game.BPlayer;
import fr.badblock.api.utils.bukkit.ChatUtils;

public class KitsManager {
	public static final String INVENTORY_NAME = ChatUtils.colorReplace("%red%Choisissez un kit :");
	
	private static KitsManager instance;
	public static KitsManager getInstance(){
		return instance;
	}
	private List<Kit> kits;
	public List<Kit> getKits(){
		return kits;
	}
	public Kit getByItem(ItemStack item){
		for(Kit kit : kits){
			if(kit == null) continue;
			if(kit.getKitItem().getType() == item.getType() &&
					kit.getKitItem().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName()))
				return kit;
		}
		return null;
	}
	public Inventory generateInventory(BPlayer player){
		int inventorySize = (int)(kits.size() / 9) * 9;
		if(kits.size() > inventorySize) inventorySize += 9;
		
		if(kits.size() == 0){
			player.sendMessage("%red%Les kits en " + BPlugin.getInstance().getGameName() + ", bientôt sur vos écrans ! :p");
			return null;
		}
		ItemStack[] items = new ItemStack[inventorySize];
		
		int i = 0;
		for(Kit kit : kits){
			if(kit == null){
				i++; continue;
			}
			ItemStack item = kit.getKitItem().clone();
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore(); 
			if(lore == null) {
				lore = new ArrayList<String>();
			}
			lore.add("");
			
			if(player.hasKit(kit)){
				lore.add(ChatUtils.colorReplace("%green%Cliquez pour sélectionner ce kit."));
			} else if(kit.isVIP()) {
				lore.add(ChatUtils.colorReplace("%red%Kit réservé aux VIPs " + kit.getPrice() + " (badblock.fr pour l'obtenir)"));
			} else if(player.getCoins() >= kit.getPrice()){
				lore.add(ChatUtils.colorReplace("%green%Achetez ce kit pour " + kit.getPrice() + " BadCoins."));
			} else {
				lore.add(ChatUtils.colorReplace("%red%Il vous faut " + kit.getPrice() + " BadCoins pour obtenir ce kit."));
			}

			meta.setLore(lore); item.setItemMeta(meta);
			items[i] = item;
			i++;
		}
		
		Inventory inv = Bukkit.createInventory(null, inventorySize, INVENTORY_NAME);
		inv.setContents(items);
		
		return inv;
	}
	public KitsManager(File folder, List<String> kitsList){
		instance = this;
		kits  = new ArrayList<Kit>();
		if(!folder.exists()) folder.mkdirs();
		
		for(String kitName : kitsList){
			if(kitName.isEmpty()){
				kits.add(null); continue;
			}
			Kit kit = null;
			File file = new File(folder, kitName + ".yml");
			if(!file.exists()) continue;
			
			try {
				kit = new Kit(YamlConfiguration.loadConfiguration(file));
			} catch(Exception e){
				e.printStackTrace();
			}
			
			if(kit != null){
				kits.add(kit);
			}
		}
		
		CommandsManager.getInstance().registerCommand("addkit", AddKitCommand.class);
	}
}
