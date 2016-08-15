package fr.badblock.api.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import fr.badblock.api.utils.bukkit.ChatUtils;

public abstract class AbstractCommand {
	protected String permission, help;
	protected boolean allowConsole;
	
	public AbstractCommand(String permission, String help, boolean allowConsole){
		this.permission = permission;
		this.allowConsole = allowConsole;
		this.help = help;
	}
	public boolean can(CommandSender sender){
		return sender.hasPermission(new Permission(permission)) && (allowConsole || (sender instanceof Player));
	}
	public void sendHelp(CommandSender sender){
		sendMessage(sender, help);
	}
	public void sendMessage(CommandSender sender, String... messages){
		ChatUtils.sendMessagePlayer(sender, messages);
	}
	public abstract void run(CommandSender sender, String[] args);
}
