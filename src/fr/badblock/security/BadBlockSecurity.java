package fr.badblock.security;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import fr.badblock.security.listeners.PlayerLoginListener;

public class BadBlockSecurity extends JavaPlugin {

	public static BadBlockSecurity instance;
	
	public List<String> ips = new ArrayList<>();
	
	@Override
	public void onEnable() {
		instance = this;
		this.reloadConfig();
		ips = this.getConfig().getStringList("ips");
		this.getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);
	}
	
	public static BadBlockSecurity getInstance() {
		return instance;
	}
	
}
