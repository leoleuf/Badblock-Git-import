package fr.badblock.api;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import fr.badblock.api.commands.BadblockCommand;
import fr.badblock.api.commands.CommandsManager;
import fr.badblock.api.commands.FeedCommand;
import fr.badblock.api.commands.FlyCommand;
import fr.badblock.api.commands.GameModeCommand;
import fr.badblock.api.commands.GodModeCommand;
import fr.badblock.api.commands.HealCommand;
import fr.badblock.api.commands.TeleportCommand;
import fr.badblock.api.commands.TeleportPosCommand;
import fr.badblock.api.commands.VanishCommand;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.listeners.ArrowBugFixListener;
import fr.badblock.api.listeners.ChatListener;
import fr.badblock.api.permissions.AbstractPermissions;

public abstract class BPlugin extends JavaPlugin{
	protected static BPlugin instance;
	public static BPlugin getInstance(){
		return instance;
	}

	protected abstract void enable();
	protected abstract void disable();

	protected abstract boolean enableEssentialsCommands();
	protected abstract boolean enableBadblockCommand();

	protected abstract boolean mustFixBugs();
	protected abstract boolean mustManageChat();
	
	public abstract String getWelcomeTitle();
	public abstract String getWelcomeSubTitle();

	public abstract String getGameName();
	
	@Override
	public final void onEnable(){
		instance = this;
		
		for(Entity e : getServer().getWorlds().get(0).getEntities()){
			if(e.getType().isAlive())
				e.remove();
		}
		CommandsManager.init();
		AbstractPermissions.init();
		BPlayersManager.init();

		if(mustFixBugs()){
			registerEvent(new ArrowBugFixListener());
			// OTHER BUGS
		}
		if(mustManageChat())
			registerEvent(new ChatListener());
		if(enableEssentialsCommands()){
			CommandsManager.getInstance().registerCommand("fly", FlyCommand.class);
			CommandsManager.getInstance().registerCommand("heal", HealCommand.class);
			CommandsManager.getInstance().registerCommand("feed", FeedCommand.class);
			CommandsManager.getInstance().registerCommand("tp", TeleportCommand.class, "teleport");
			CommandsManager.getInstance().registerCommand("tppos", TeleportPosCommand.class, "teleportpos");
			CommandsManager.getInstance().registerCommand("gamemode", GameModeCommand.class, "gm");
			CommandsManager.getInstance().registerCommand("godmode", GodModeCommand.class, "god");
			registerEvent(new GodModeCommand());
			
			CommandsManager.getInstance().registerCommand("vanish", VanishCommand.class, "v");
			registerEvent(new VanishCommand());
		}
		if(enableBadblockCommand()){
			CommandsManager.getInstance().registerCommand("badblock", BadblockCommand.class, "bb");
		}

		enable();
		for(Player p : Bukkit.getOnlinePlayers()){
			BPlayersManager.getInstance().connect(p, false);
		}
	}
	@Override
	public final void onDisable(){
		disable();
	}
	public <T extends Listener> void registerEvent(T listener){
		getServer().getPluginManager().registerEvents(listener, this);
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		CommandsManager.getInstance().executeCommand(sender, label, args);
		return true;
	}
}
