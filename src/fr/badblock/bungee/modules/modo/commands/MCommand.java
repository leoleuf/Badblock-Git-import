package fr.badblock.bungee.modules.modo.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.modules.modo.commands.subcommands.BanCommand;
import fr.badblock.bungee.modules.modo.commands.subcommands.BanIpCommand;
import fr.badblock.bungee.modules.modo.commands.subcommands.ConnectPlayerCommand;
import fr.badblock.bungee.modules.modo.commands.subcommands.ConnectServerCommand;
import fr.badblock.bungee.modules.modo.commands.subcommands.KickCommand;
import fr.badblock.bungee.modules.modo.commands.subcommands.MuteCommand;
import fr.badblock.bungee.modules.modo.commands.subcommands.SanctionCommand;
import fr.badblock.bungee.modules.modo.commands.subcommands.TempBanCommand;
import fr.badblock.bungee.modules.modo.commands.subcommands.TempBanIpCommand;
import fr.badblock.bungee.modules.modo.commands.subcommands.TempMuteCommand;
import fr.badblock.bungee.modules.modo.commands.subcommands.TrackCommand;
import fr.badblock.bungee.modules.modo.commands.subcommands.UnbanCommand;
import fr.badblock.bungee.modules.modo.commands.subcommands.UnbanIPCommand;
import fr.badblock.bungee.modules.modo.commands.subcommands.UnmuteCommand;
import fr.badblock.bungee.modules.modo.commands.subcommands.WarnCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * 
 * Moderation command
 *
 * @author xMalware
 *
 */
public class MCommand extends BadCommand {

	private List<AbstractModCommand> moderationCommands;

	// I18n key prefix
	private String prefix = "bungee.commands.mod.";

	/**
	 * Command constructor
	 */
	public MCommand(Plugin plugin) {
		// Super!
		super(plugin, "m", null, "md");

		moderationCommands = new ArrayList<>();
		moderationCommands.add(new BanIpCommand());
		moderationCommands.add(new BanCommand());
		moderationCommands.add(new TempBanIpCommand());
		moderationCommands.add(new TempBanCommand());
		moderationCommands.add(new KickCommand());
		moderationCommands.add(new TempMuteCommand());
		moderationCommands.add(new MuteCommand());
		moderationCommands.add(new UnmuteCommand());
		moderationCommands.add(new UnbanCommand());
		moderationCommands.add(new UnbanIPCommand());
		moderationCommands.add(new WarnCommand());
		moderationCommands.add(new TrackCommand());
		moderationCommands.add(new SanctionCommand());
		moderationCommands.add(new ConnectPlayerCommand());
		moderationCommands.add(new ConnectServerCommand());
	}

	/**
	 * Send help to the player
	 * 
	 * @param sender
	 */
	private void help(CommandSender sender) {
		// Send help
		I19n.sendMessage(sender, prefix + "help.intro", null);
		List<AbstractModCommand> greenCommands = new ArrayList<>();
		List<AbstractModCommand> redCommands = new ArrayList<>();
		
		for (AbstractModCommand aCommand : moderationCommands) {
			if (sender.hasPermission(aCommand.getPermission()))
			{
				greenCommands.add(aCommand);
			}
			else
			{
				redCommands.add(aCommand);
			}
		}
		
		for (AbstractModCommand c : greenCommands)
		{
			I19n.sendMessage(sender, prefix + "help." + c.getName() + "_g", null);
		}
		
		for (AbstractModCommand c : redCommands)
		{
			I19n.sendMessage(sender, prefix + "help." + c.getName() + "_r", null);
		}
		
		I19n.sendMessage(sender, prefix + "help.outro", null);
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		boolean perm = false;
		for (AbstractModCommand aCommand : moderationCommands)
		{
			if (sender.hasPermission(aCommand.getPermission()))
			{
				perm = true;
				break;
			}
		}
		
		if (!perm)
		{
			I19n.sendMessage(sender, prefix + "notenoughpermissions", null);
			return;
		}
		
		// If no argument has been entered
		if (args.length <= 0) {
			// We give him help.
			help(sender);
			// We stop there.
			return;
		}

		String subcommand = args[0];

		switch (subcommand) {
		case "help":
		case "h":
		case "?":
			help(sender);
			break;

		default:
			AbstractModCommand command = null;
			for (AbstractModCommand aCommand : moderationCommands) {
				if (aCommand.getName().equalsIgnoreCase(subcommand)) {
					command = aCommand;
					break;
				}
				if (aCommand.getArgs() != null
						&& Arrays.asList(aCommand.getArgs()).contains(subcommand.toLowerCase())) {
					command = aCommand;
					break;
				}
			}
			if (command != null) {
				if (sender.hasPermission(command.getPermission())) {
					command.run(sender, args);
				} else {
					I19n.sendMessage(sender, prefix + "notenoughpermissions", null);
				}
			} else {
				unknownCommand(sender);
			}
			break;
		}
	}

	/**
	 * Send unknown command to the player
	 * 
	 * @param sender
	 */
	private void unknownCommand(CommandSender sender) {
		// Send help
		I19n.sendMessage(sender, prefix + "unknowncommand", null);
	}

}