package fr.badblock.ladder.api.plugins;

import java.io.File;
import java.io.IOException;
import java.util.List;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.config.Configuration;
import fr.badblock.ladder.api.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Plugin {
	private static final String CONFIG_FILE_NAME = "config.yml";
	@Getter private PluginInfo pluginInfo;
	@Getter private Configuration config;
	
	public void onLoad(){}
	public void onEnable(){}
	public void onDisable(){}
	
	public Ladder getLadder(){
		return Ladder.getInstance();
	}
	
	public final File getDataFolder(){
		return new File("plugins" + File.separator + pluginInfo.getName());
	}
	
	public final void saveConfig(){
		try {
			getLadder().getConfigurationProvider().save(config, new File(getDataFolder(), CONFIG_FILE_NAME));
		} catch (IOException e) {
			System.out.println("Can not save configuration file for " + pluginInfo.toString() + " :");
			e.printStackTrace();
		}
	}
	
	public final void reloadConfig(){
		try {
			config = getLadder().getConfigurationProvider().load(new File(getDataFolder(), CONFIG_FILE_NAME));
		} catch (IOException e) {
			System.out.println("Can not load configuration file for " + pluginInfo.toString() + " :");
			e.printStackTrace();
		}
	}
	
	@Override
	public final String toString(){
		return getPluginInfo().toString();
	}
	
	@Override
	public final boolean equals(Object o){
		if(o instanceof Plugin){
			Plugin plugin = (Plugin) o;
			return plugin.getPluginInfo().equals(getPluginInfo());
		}
		return false;
	}
	
	protected final void initPlugin(PluginInfo info){
		this.pluginInfo = info;
		if(!getDataFolder().exists()) getDataFolder().mkdirs();
		
		reloadConfig();
	}
	
	@AllArgsConstructor
	public static class PluginInfo {
		@Getter private String name, version, mainClass;
		@Getter private List<String> authors, depends, softDepends;
		@Getter private File jarFile;
		
		@Override
		public String toString(){
			return name + " " + version + " by " + StringUtils.join(getAuthors(), ", ");
		}
		
		@Override
		public final boolean equals(Object o){
			if(o instanceof PluginInfo){
				PluginInfo pluginInfo = (PluginInfo) o;
				return pluginInfo.getName().equalsIgnoreCase(getName());
			}
			return false;
		}
	}
}
