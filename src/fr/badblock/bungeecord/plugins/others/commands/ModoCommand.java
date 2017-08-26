package fr.badblock.bungeecord.plugins.others.commands;

import java.util.HashMap;
import java.util.Map;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitPacketType;
import fr.badblock.common.commons.utils.Encodage;
import fr.badblock.common.protocol.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ModoCommand extends Command {

	private static Map<String, Long> times = new HashMap<>();
	private static long				 time  = 120_000L;

	public ModoCommand() {
		super("modo");
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage("§e--------------------------------------------------");
			sender.sendMessage("§fPosez votre question à la modération !");
			sender.sendMessage(" ");
			sender.sendMessage("     §bUtilisation: §f/modo §b<message>");
			sender.sendMessage(" ");
			sender.sendMessage("§cTout abus de cette commande vaudra un bannissement.");
			sender.sendMessage("§fN'oubliez pas d'activer vos MP pour avoir une réponse.");
			sender.sendMessage("§cLes signalements ne se font pas par cette commande.");
			sender.sendMessage("§e--------------------------------------------------");
			return;
		}
		String playerName = sender.getName();
		long timestamp = System.currentTimeMillis();
		if (times.containsKey(playerName) && times.get(playerName) > timestamp) {
			sender.sendMessage("§e--------------------------------------------------");
			sender.sendMessage("§cVeuillez patienter entre chaque message.");
			sender.sendMessage(" ");
			sender.sendMessage("§fLa modération doit répondre à tout le serveur.");
			sender.sendMessage("§fCe n'est pas tâche facile.");
			sender.sendMessage(" ");
			sender.sendMessage("§bUn message est autorisé toutes les §cdeux minutes§b.");
			sender.sendMessage("§cEn cas de réponse d'un modérateur, cliquez sur son DM");
			sender.sendMessage("§cpour y répondre au lieu d'utiliser le /modo.");
			sender.sendMessage("§e--------------------------------------------------");
			return;
		}
		long expire = timestamp + time;
		times.put(playerName, expire);
		String message = StringUtils.join(args, " ");
		BadBlockBungeeOthers.getInstance().getRabbitService().sendSyncPacket("badreport", "§6[/modo] &f" + playerName + " >> §7" + message, Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
		BadBlockBungeeOthers.getInstance().getRabbitService().sendSyncPacket("badreport", "§fRépondez à la question en faisant /msg " + playerName, Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
		sender.sendMessage("§e--------------------------------------------------");
		sender.sendMessage("§aVotre question a été posée à la modération :");
		sender.sendMessage(message);
		sender.sendMessage(" ");
		sender.sendMessage("§eRappel: ");
		sender.sendMessage("§c > Tout abus de cette commande vaudra un bannissement.");
		sender.sendMessage("§c > Les signalements ne se font pas par cette commande.");
		sender.sendMessage("§f > N'oubliez pas d'activer vos MP pour avoir une réponse.");
		sender.sendMessage("§e--------------------------------------------------");
	}

}