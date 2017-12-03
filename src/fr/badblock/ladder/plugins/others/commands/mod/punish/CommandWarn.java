package fr.badblock.ladder.plugins.others.commands.mod.punish;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.ladder.entities.LadderPlayer;
import fr.badblock.ladder.plugins.others.BadBlockOthers;
import fr.badblock.ladder.plugins.others.database.BadblockDatabase;
import fr.badblock.ladder.plugins.others.database.Request;
import fr.badblock.ladder.plugins.others.database.Request.RequestType;
import fr.badblock.ladder.plugins.others.friends.FriendPlayer;
import fr.badblock.ladder.plugins.others.utils.I18N;

public class CommandWarn extends SanctionCommand {

	public static CommandWarn instance;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");

	public CommandWarn() {
		super("warn", "ladder.command.warn", "wn");
		instance = this;
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		boolean console = !(sender instanceof Player);
		if (!console)
			console = (FriendPlayer.get((Player) sender) != null && FriendPlayer.get((Player) sender).tail) || sender.hasPermission("*");
		if (args.length < 2 && !console) {
			sender.sendMessage("§cUsage: /warn <pseudo> <raison>");
			return;
		} else if (args.length < 1 && console) {
			sender.sendMessage("§cUsage: /warn <pseudo> [raison]");
			return;
		}
		if (args.length == 1) {
			args = Arrays.copyOf(args, args.length + 1);
			args[1] = ".";
		}
		String reason = ChatColor.replaceColor(StringUtils.join(args, " ", 1));
		if (reason.length() < 3 && !console) {
			sender.sendMessage("§c➤ Le motif doit faire au minimum 3 caractères.");
			return;
		}
		if (args[0].equals("")) {
			sender.sendMessage("§cLe pseudo ne doit pas être vide.");
			return;
		}
		Ladder ladder = Ladder.getInstance();
		OfflinePlayer offlinePlayer = ladder.getOfflinePlayer(args[0]);
		// Unknown player?
		if (!offlinePlayer.hasPlayed()) {
			sender.sendMessage("§c➤ " + args[0] + " ne s'est jamais connecté sur le serveur.");
			return;
		}

		// If it's a player (not a console, etc.)
		if (sender instanceof Player) {
			Player banner = (Player) sender;
			int bannerLevel = ModUtils.getLevel(banner);
			int bannedLevel = ModUtils.getLevel(offlinePlayer);
			// Rank level
			if (bannerLevel != 100 && bannerLevel <= bannedLevel) {
				sender.sendMessage(
						"§c➤ Ce joueur est plus haut gradé/au même grade que vous, impossible de l'avertir !");
				return;
			}
		}

		// Punish player name
		String bannerIp = sender instanceof Player ? ((Player) sender).getLastAddress().getHostAddress() : "127.0.0.1";
		
		int done = -1;
		if (offlinePlayer instanceof LadderPlayer) {
			LadderPlayer connected = (LadderPlayer) offlinePlayer;
			if (connected.getBukkitServer() != null) {
				connected.getBukkitServer().broadcast("§b➤ " + connected.getName() + " §7a été averti par §b"
						+ sender.getName() + "§7 pour §b" + reason + "§7.");
				connected.sendMessages(I18N.getTranslatedMessages("msg.warn", sender.getName(), reason, sdf.format(new Date())));
				done = 0;
			}
		}
		
		Request request = new Request(
				"INSERT INTO sanctions(pseudo, ip, type, expire, timestamp, date, reason, banner, fromIp) " + "VALUES('"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(offlinePlayer.getName()) + "', '"
						+ BadblockDatabase.getInstance()
						.mysql_real_escape_string(offlinePlayer.getLastAddress().getHostAddress())
						+ "', 'warn', '" + done + "', '" + System.currentTimeMillis()
						+ "', '" + BadBlockOthers.getInstance().simpleDateFormat.format(new Date()) + "', '"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(reason) + "', '"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(sender.getName()) + "', '"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(bannerIp) + "')",
						RequestType.SETTER);
		BadblockDatabase.getInstance().addSyncRequest(request);

		// Saving datas
		offlinePlayer.saveData();
		sender.sendMessage("§a➤ Avertissement appliqué envers " + offlinePlayer.getName() + " (" + reason + ").");
	}

}
