package fr.badblock.ladder.plugins.others.commands;

import java.util.HashMap;
import java.util.Map;

import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.plugins.others.commands.msg.MsgCommand;
import fr.badblock.ladder.plugins.others.commands.msg.PrivateMessage;
import fr.badblock.ladder.plugins.others.database.BadblockDatabase;
import fr.badblock.ladder.plugins.others.database.Request;
import fr.badblock.ladder.plugins.others.database.Request.RequestType;
import fr.badblock.ladder.plugins.others.rabbit.receivers.BadFilterListener;

public class CBReportCommand extends Command {

	public static Map<String, Long> lastReport = new HashMap<>();
	public static Map<Integer, String> lastReportId = new HashMap<>();
	
	public CBReportCommand() {
		super("cbreport");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if (args.length != 1) return;
		String idString = args[0];
		int id = -1;
		try {
			id = Integer.parseInt(idString);
		}catch(Exception error) {
			return;
		}
		if (!MsgCommand.messages.containsKey(id)) {
			sender.sendMessage("§cCe message ne peut plus être signalé, il est expiré.");
			return;
		}
		if (lastReportId.containsKey(id)) {
			if (lastReportId.get(id).equalsIgnoreCase(sender.getName().toLowerCase())) {
				sender.sendMessage("§cVous avez déjà signalé ce message.");
				return;
			}else {
				sender.sendMessage("§cCe message a déjà été signalé.");
				return;
			}
		}
		if (lastReport.containsKey(sender.getName().toLowerCase())) {
			long l = lastReport.get(sender.getName().toLowerCase());
			if (l > System.currentTimeMillis()) {
				sender.sendMessage("§cVeuillez patienter entre chaque signalement.");
				return;
			}
		}
		PrivateMessage privateMessage = MsgCommand.messages.get(id);
		if (privateMessage.playerName.equalsIgnoreCase(sender.getName())) {
			sender.sendMessage("§cVous ne pouvez pas signaler vous-même vos propres messages.");
			return;
		}
		BadFilterListener.work("§7(By " + sender.getName() + ") | §7" + privateMessage.playerName + " §8» §7" + privateMessage.message);
		BadblockDatabase.getInstance().addRequest(new Request("INSERT INTO reportMsg(player, byPlayer, message, timestamp) VALUES('" + secure(privateMessage.playerName) + "', '" + secure(sender.getName()) + "', '" + secure(privateMessage.message) + "', '" + System.currentTimeMillis() + "')", RequestType.SETTER));
		sender.sendMessage("§aLe message privé suivant a bien été signalé :");
		sender.sendMessage("§b" + privateMessage.playerName + " §8: §7" + privateMessage.message);
		lastReport.put(sender.getName().toLowerCase(), System.currentTimeMillis() + 30_000L);
		lastReportId.put(id, sender.getName().toLowerCase());
	}
	
	private String secure(String string) {
		return BadblockDatabase.getInstance().mysql_real_escape_string(string);
	}

}
