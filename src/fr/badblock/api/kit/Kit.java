package fr.badblock.api.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import fr.badblock.api.game.BPlayer;
import fr.badblock.api.utils.bukkit.ChatUtils;
import lombok.Data;

@Data public class Kit {
	private String bddName;
	private ItemStack[] armorItems;
	private ItemStack[] otherItems;
	private List<String> commands;
	private ItemStack kitItem;
	private int price;
	private boolean isVIP;
	private double health = 20.0d;

	public Kit(ConfigurationSection section){
		armorItems = new ItemStack[4];
		otherItems = new ItemStack[4 * 9];

		isVIP = section.getBoolean("isVIP");
		price = section.getInt("price");
		kitItem = loadItem(section.getConfigurationSection("kit-item"));

		armorItems = section.getList("armor").toArray(new ItemStack[0]);
		otherItems = section.getList("others").toArray(new ItemStack[0]);
		
		commands = section.getStringList("commands");
		
		health = section.getDouble("health");
		
		if(health == 0) health = 20.0d;

		bddName = section.getString("bddField");
	}
	
	public Kit(PlayerInventory inv, String name){
		bddName = "toModif_" + name;
		armorItems = inv.getArmorContents();
		otherItems = inv.getContents();
		price = 100000; // bouhaha :3
		isVIP = false;
		health = 20.0d;
		
		kitItem = new ItemStack(Material.DIAMOND_SWORD, 1);
		ItemMeta meta = kitItem.getItemMeta(); meta.setDisplayName(name); kitItem.setItemMeta(meta);
	
		commands = Arrays.asList("example %player%");
	}
	
	public void save(FileConfiguration config){
		config.set("bddField", bddName);
		config.set("price", price);
		config.set("isVIP", isVIP);
		
		config.set("health", health);

		saveItem(config, "kit-item", kitItem);

		config.set("armor", armorItems);
		config.set("others", otherItems);
		config.set("commands", commands);
	}
	
	@SuppressWarnings("deprecation")
	private ItemStack loadItem(ConfigurationSection section){
		Material material = Material.matchMaterial(section.getString("id"));
		if(material == Material.AIR){
			return null;
		}
		List<String> loreTemp = section.getStringList("lore");
		List<String> lore = new ArrayList<String>();
		String displayName = section.getString("displayName");
		short durability = (short) section.getInt("durability");
		byte data = (byte) section.getInt("data");
		int amount = section.getInt("amount");

		for(String l : loreTemp){
			lore.add(ChatUtils.colorReplace(l));
		}

		if(material == null) return null; else if(amount == 0) amount = 1;

		ItemStack result = new ItemStack(material, amount, durability, data);

		if(displayName != null){
			ItemMeta meta = result.getItemMeta(); meta.setDisplayName(ChatUtils.colorReplace(displayName));
			meta.setLore(lore); result.setItemMeta(meta);
		}

		return result;
	}
	
	@SuppressWarnings("deprecation")
	public void saveItem(ConfigurationSection c, String deb, ItemStack is){
		if(is == null){
			saveItem(c, deb, new ItemStack(Material.AIR, 1));
			return;
		}
		c.set(deb + ".id", is.getType().toString());
		c.set(deb + ".data", is.getData() != null ? is.getData().getData() : 0);
		c.set(deb + ".durability", is.getDurability());
		c.set(deb + ".amount", is.getAmount());

		if(is.getItemMeta() != null){
			c.set(deb + ".displayname", is.getItemMeta().getDisplayName());
			if(is.getItemMeta() != null)
				c.set(deb + ".lore", is.getItemMeta().getLore());
			else c.set(deb + ".lore", new ArrayList<String>());
		} else {
			c.set(deb + ".lore", new ArrayList<String>());
		}

	}

	public void give(BPlayer player){
		player.clear();
		player.getPlayer().setMaxHealth(health);
		player.getPlayer().setHealth(health);
		
		player.getPlayer().getInventory().setArmorContents(armorItems);
		int i=0;
		for(ItemStack item : otherItems){
			if(item != null)
				player.getPlayer().getInventory().setItem(i, item);
			i++;
		}
		
		if(commands == null) return;
		for(String command : commands){
			System.out.println(command);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getPlayerName()));
		}
	}
	public String getBDDName(){
		return bddName;
	}
	public ItemStack getKitItem(){
		return kitItem;
	}
	public boolean isVIP(){
		return isVIP;
	}
	public int getPrice(){
		return price;
	}
}
