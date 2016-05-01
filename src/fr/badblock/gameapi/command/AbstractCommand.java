package fr.badblock.gameapi.command;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.i18n.messages.CommandMessages;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

/**
 * Classe abstraite permettantt de simplifier l'utilisation des commandes. Par exemple, elles ne devront pas �tre situ�es
 * dans le plugin.yml
 * @author LeLanN
 */
@Getter public abstract class AbstractCommand implements CommandExecutor {
	private String   		   command;
	private TranslatableString usage;
	private String   		   permission;
	private String[] 		   aliases;

	private boolean  		   allowConsole = true;

	/**
	 * Cr�e une nouvelle commande
	 * @param command Le nom de la commande
	 * @param usage Le message d'erreur si la commande est mal utilis�e
	 * @param permission La permission n�cessaire (pas de permission est cha�ne vide)
	 * @param aliases Les aliases �ventuels de la commande
	 */
	public AbstractCommand(String command, TranslatableString usage, String permission, String... aliases){
		this.command 	= command;
		this.usage      = usage;
		this.permission = permission;
		this.aliases    = aliases;

		ReflectCommand result = new ReflectCommand(command);

		result.setAliases(Arrays.asList(aliases));
		result.setExecutor(this);
		
		getCommandMap().register("", result);
	}
	
	/**
	 * Cr�e une nouvelle commande
	 * @param command Le nom de la commande
	 * @param usage Le message d'erreur si la commande est mal utilis�e
	 * @param permission La permission n�cessaire (pas de permission est {@link GamePermission#PLAYER})
	 * @param aliases Les aliases �ventuels de la commande
	 */
	public AbstractCommand(String command, TranslatableString usage, GamePermission permission, String... aliases){
		this(command, usage, permission.getPermission(), aliases);
	}

	/**
	 * Permet de dire si la console peut utiliser la commande. Par d�faut � false.
	 * @param console Un boolean
	 */
	public void allowConsole(boolean console){
		this.allowConsole = console;
	}

	@Override
	public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(permission != null && !permission.isEmpty() && !sender.hasPermission(permission)){
			CommandMessages.noPermission().send(sender);
		} else if(!allowConsole && !(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This command is only for players.");
		} else if(!executeCommand(sender, args) && usage != null){
			usage.send(sender);
		}
		
		return true;
	}
	
	/**
	 * Permet d'ex�cuter la commande
	 * @param sender Le sender
	 * @param args Les arguments
	 * @return Si la commande est bien utilis�e
	 */
	public abstract boolean executeCommand(CommandSender sender, String[] args);

	private final CommandMap getCommandMap() {
		try {
			return (CommandMap) new Reflector(Bukkit.getServer()).getFieldValue("commandMap");
		} catch (Exception e) { 
			e.printStackTrace(); 
			return null;
		}
	}

	private final class ReflectCommand extends Command {
		private AbstractCommand exe = null;

		protected ReflectCommand(String command) { 
			super(command); 
		}

		public void setExecutor(AbstractCommand exe) { 
			this.exe = exe; 
		}

		@Override public boolean execute(CommandSender sender, String commandLabel, String[] args) {
			if (exe != null) { return exe.onCommand(sender, this, commandLabel, args); }
			return false;
		}
	}
}