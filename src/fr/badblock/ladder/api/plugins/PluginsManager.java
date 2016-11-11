package fr.badblock.ladder.api.plugins;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.config.Configuration;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.events.Event;
import fr.badblock.ladder.api.events.Listener;
import fr.badblock.ladder.api.events.all.CommandEvent;
import fr.badblock.ladder.api.plugins.Plugin.PluginInfo;

public class PluginsManager {
	private Map<String, Plugin> plugins;
	private Map<String, Command> commands;
	private Map<String, PluginInfo> toLoad;
	
	private Map<Plugin, List<Listener>> listenersByPlugin;
	private Map<Plugin, List<Command>> commandsByPlugin;

	private EventDispatcher dispatcher;
	
	public Command getCommandByName(String name){
		return commands.get(name.toLowerCase());
	}
	
	public List<String> getCommandsNames(){
		List<String> commands = new ArrayList<String>();
		for(String command : this.commands.keySet())
			commands.add(command);
		return commands;
	}
	
	public JsonArray getJsonCommandsNames(){
		JsonArray commands   = new JsonArray();
		
		for(String command : this.commands.keySet()) {
			Command cmd = this.commands.get(command);
			if(cmd == null) continue;
			
			JsonObject object = new JsonObject();
			
			object.addProperty("name", command);
			object.addProperty("bypass", cmd.isBypassable());
			
			commands.add(object);
		}
		
		return commands;
	}
	
	public Collection<Command> getCommands(){
		return Collections.unmodifiableCollection(commands.values());
	}
	
	public PluginsManager(){
		plugins = new LinkedHashMap<String, Plugin>();
		commands = new HashMap<String, Command>();
		toLoad = new HashMap<String, PluginInfo>();
		
		listenersByPlugin = new HashMap<Plugin, List<Listener>>();
		commandsByPlugin = new HashMap<Plugin, List<Command>>();

		dispatcher = new EventDispatcher();
	}

	public PluginInfo detectPlugin(File jarFile) {
		String jarName = jarFile.getName().toLowerCase();
		if(jarFile.isDirectory() ||  !jarName.endsWith(".jar"))
			return null;

		try {
			JarFile jar = new JarFile(jarFile);
			JarEntry pluginYml = jar.getJarEntry("ladder.yml");
			if(pluginYml == null) pluginYml = jar.getJarEntry("plugin.yml");
			if(pluginYml == null){
				jar.close();
				throw new RuntimeException("JAR File " + jarFile.getName() + " doesn't contains a configuration file (ladder.yml or plugin.yml)! Could not load the plugin.");
			}

			Configuration config = Ladder.getInstance().getConfigurationProvider().load(jar.getInputStream(pluginYml));
			jar.close();

			String name = config.getString("name");
			String version = config.getString("version");
			String mainClass = config.getString("main");
			List<String> authors = config.getStringList("authors");
			List<String> depends = config.getStringList("depends");
			List<String> softdepends = config.getStringList("softdepends");

			if(authors.isEmpty()) authors.add("An Unnamed Author");
			if(mainClass == null) throw new RuntimeException("Invalid " + pluginYml.getName() + " for " + jarFile.getName() + " : plugin.yml don't specify main class!");
			if(name == null) throw new RuntimeException("Invalid " + pluginYml.getName() + " for " + jarFile.getName() + " : plugin.yml don't specify plugin name !");
			if(version == null) version = "Unknow Version";

			if(toLoad.containsKey(name.toLowerCase()) || plugins.containsKey(name.toLowerCase())){
				throw new RuntimeException("Plugin '" + name + "'already loaded (" + jarFile + ")");
			}
			
			return new PluginInfo(name, version, mainClass, authors, depends, softdepends, jarFile);
		} catch(Throwable t){
			t.printStackTrace();
		}
		return null;
	}

	public void detectPlugins(File folder) {
		if(!folder.exists()) folder.mkdirs();
		if(!folder.isDirectory()) return;

		for(File file : folder.listFiles()){
			PluginInfo pi = detectPlugin(file);
			if(pi != null)
				toLoad.put(pi.getName().toLowerCase(), pi);
		}
	}

	public void loadPlugins() {
		for(PluginInfo plugin : toLoad.values()) {
			try {
				loadPlugin(plugin);
			} catch(Throwable t){
				System.out.println("Unable to load plugin " + plugin);
				if(t instanceof StackOverflowError){
					System.out.println("Error is an StackOverflowError. Check if the plugin contains circular dependencies.");
				} else {
					t.printStackTrace();
				}
			}
		}
		toLoad.clear();
	}

	@SuppressWarnings("resource")
	public boolean loadPlugin(PluginInfo plugin) throws Throwable {
		if(getPlugin(plugin) != null) return true;
		for(String depend : plugin.getDepends()){
			if(!loadPlugin(toLoad.get(depend.toLowerCase()))){
				throw new RuntimeException("Unknow dependency '" + depend + "' for " + plugin);
			}
		}

		for(String softdepend : plugin.getSoftDepends()){
			loadPlugin(toLoad.get(softdepend.toLowerCase()));
		}

		URLClassLoader loader = new PluginClassLoader(new URL[]{plugin.getJarFile().toURI().toURL()});
		Class<?> main = loader.loadClass(plugin.getMainClass());
		Plugin clazz = (Plugin) main.getDeclaredConstructor().newInstance();

		clazz.initPlugin(plugin);
		plugins.put(plugin.getName().toLowerCase(), clazz);
		clazz.onLoad();
		
		return true;
	}

	public void unloadPlugin(Plugin plugin){
		unregisterAllCommands(plugin);
		unregisterAllEvents(plugin);
		
		while(plugins.values().remove(plugin));
	}
	
	public void enablePlugins() {
		for(Plugin plugin : plugins.values()){
			enablePlugin(plugin);
		}
	}

	public void enablePlugin(Plugin plugin){
		try {
			plugin.onEnable();
			System.out.println(plugin.getPluginInfo() + " enabled !");
		} catch(Throwable t){
			System.out.println("Unable to enable plugin " + plugin);
			t.printStackTrace();
		}
	}
	
	public void disablePlugin(Plugin plugin){
		try {
			plugin.onDisable();
			System.out.println(plugin.getPluginInfo() + " disabled !");
		} catch(Throwable t){
			System.out.println("Unable to disable plugin " + plugin);
			t.printStackTrace();
		}
	}

	public void disablePlugins() {
		for(Plugin plugin : plugins.values()){
			disablePlugin(plugin);
		}
	}

	public Plugin getPlugin(String name) {
		return plugins.get(name.toLowerCase());
	}

	public Plugin getPlugin(PluginInfo info) {
		return plugins.get(info.getName().toLowerCase());
	}

	public Collection<Plugin> getPlugins() {
		return Collections.unmodifiableCollection(plugins.values());
	}

	public void registerEvents(Plugin plugin, Listener listener) {
		dispatcher.registerListener(listener);
		
		if(!listenersByPlugin.containsKey(plugin)){
			listenersByPlugin.put(plugin, new ArrayList<Listener>());
		}
		listenersByPlugin.get(plugin).add(listener);
	}

	public void unregisterEvents(Plugin plugin, Listener listener){
		List<Listener> listeners = listenersByPlugin.get(plugin);
		if(listeners != null) {
			listeners.remove(listener);
			dispatcher.unregisterListener(listener);
		}
	}
	
	public void unregisterAllEvents(Plugin plugin) {
		List<Listener> listeners = listenersByPlugin.get(plugin);
		for(Listener listener : listeners){
			dispatcher.unregisterListener(listener);
		}
		
		listenersByPlugin.remove(plugin);
	}

	public <T extends Event> T dispatchEvent(T e){
		dispatcher.dispatch(e);
		
		return e;
	}

	public void registerCommand(Plugin plugin, Command command) {
		if(commands.containsKey(command.getCommand().toLowerCase())) return;
		
		commands.put(command.getCommand().toLowerCase(), command);
		for(String aliases : command.getAliases()){
			if(commands.containsKey(aliases.toLowerCase())) continue;
			
			commands.put(aliases.toLowerCase(), command);
		}
		if(plugin != null){
			if(!commandsByPlugin.containsKey(plugin)){
				commandsByPlugin.put(plugin, new ArrayList<Command>());
			}
			commandsByPlugin.get(plugin).add(command);
		}
	}

	public void unregisterCommand(Plugin plugin, Command command) {
		while(commands.values().remove(command));
		if(plugin != null){
			List<Command> commands = commandsByPlugin.get(plugin);
			if(commands != null) {
				commands.remove(command);
			}
		}
	}

	public void unregisterAllCommands(Plugin plugin) {
		List<Command> commands = commandsByPlugin.get(plugin);
		for(Command command : commands){
			while(this.commands.values().remove(command));
		}
		
		commandsByPlugin.remove(plugin);
	}
	
	public boolean executeCommand(CommandSender sender, String command) {
		if(sender instanceof Player){
			CommandEvent event = new CommandEvent((Player) sender, command);
			dispatchEvent(event);

			if(event.isCancelled()) return false;
			command = event.getMessage();
		}

		String[] args = command.split(" ");
		
		if(args.length == 0)
			return false;
		
		String[] trueArgs = new String[args.length - 1];
		Command cmd = commands.get(args[0].toLowerCase());

		if(cmd == null) {
			sender.sendMessage(ChatColor.RED + "Commande inconnue.");
			return false;
		} else if(cmd.getPermission() != null && !sender.hasPermission(cmd.getPermission())){
			sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande !");
			return false;
		}

		for(int i=1;i<args.length;i++){
			trueArgs[i - 1] = args[i];
		}

		try {
			cmd.executeCommand(sender, trueArgs);
		} catch(Exception e){
			sender.sendMessage(ChatColor.RED + "Erreur lors de l'execution de la commande.");
			System.out.println("Error while executing command '" + command + "' :");
			e.printStackTrace(); return false;
		}

		return true;
	}
}
