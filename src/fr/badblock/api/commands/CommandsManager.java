package fr.badblock.api.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;

import fr.badblock.api.utils.bukkit.ChatUtils;

public class CommandsManager {
	private static CommandsManager instance;
	public static CommandsManager getInstance(){
		return instance;
	}
	private Map<String, Class<? extends AbstractCommand>> commands;
	
	public static void init(){
		instance = new CommandsManager();
	}
	public CommandsManager(){
		commands = new HashMap<String, Class<? extends AbstractCommand>>();
	}
	public void registerCommand(String command, Class<? extends AbstractCommand> clazz, String... aliases){
		commands.put(command.toLowerCase(), clazz);
		if(aliases.length > 0)
			for(String alias : aliases){
				if(!commands.containsKey(alias.toLowerCase())){
					commands.put(alias.toLowerCase(), clazz);	
				}
			}
	}
	public void sendHelp(CommandSender sender){
		for(Class<? extends AbstractCommand> command : commands.values()){
			try {
				AbstractCommand cmd = command.newInstance();
				if(!cmd.can(sender));
				else cmd.sendHelp(sender);
			} catch(Throwable unused) {}
		}
	}
	public void executeCommand(CommandSender sender, String label, String[] args){
		Class<? extends AbstractCommand> command = commands.get(label.toLowerCase());
		if(command == null){
			sendHelp(sender); return;
		}
		try {
			AbstractCommand cmd = command.newInstance();
			if(!cmd.can(sender))
				ChatUtils.sendMessagePlayer(sender, "%red%Vous n'avez pas la permission d'exécuter cette commande !");
			else cmd.run(sender, args);
		} catch(Throwable e) {
			ChatUtils.sendMessagePlayer(sender, "%red%La commande n'est pas valide. Veuillez en informer un administrateur.");
			e.printStackTrace();
		}
	}
}
