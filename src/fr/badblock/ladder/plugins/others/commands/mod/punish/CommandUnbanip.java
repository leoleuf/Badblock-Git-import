package fr.badblock.ladder.plugins.others.commands.mod.punish;

import java.util.Arrays;
import java.util.Date;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.ladder.plugins.others.BadBlockOthers;
import fr.badblock.ladder.plugins.others.database.BadblockDatabase;
import fr.badblock.ladder.plugins.others.database.Request;
import fr.badblock.ladder.plugins.others.database.Request.RequestType;
import fr.badblock.ladder.plugins.others.friends.FriendPlayer;

public class CommandUnbanip extends SanctionCommand {

	public static CommandUnbanip instance;

	public CommandUnbanip() {
		super("unbanip", "ladder.command.unbanip", "pardonip", "ubi", "ubip");
		instance = this;
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		boolean console = !(sender instanceof Player);
		if (!console)
			console = (FriendPlayer.get((Player) sender) != null && FriendPlayer.get((Player) sender).tail) || sender.hasPermission("ladder.command.punish.bypassreasonpardon");
		if (args.length < 2 && !console) {
			sender.sendMessage("§cUsage: /unbanip <pseudo> <raison>");
			return;
		} else if (args.length < 1 && console) {
			sender.sendMessage("§cUsage: /unbanip <pseudo> [raison]");
			return;
		}
		if (args.length == 1) {
			args = Arrays.copyOf(args, args.length + 1);
			args[1] = "";
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

		// If it isn't banned
		if (!offlinePlayer.getIpAsPunished().isBan()) {
			sender.sendMessage("§c➤ " + args[0] + " n'est pas banni par IP.");
			return;
		}

		// If it's a player (not a console, etc.)
		if (sender instanceof Player) {
			Player unbanner = (Player) sender;
			OfflinePlayer banner = ladder.getOfflinePlayer(offlinePlayer.getIpAsPunished().getBanner());
			int bannerLevel = banner.getName().toLowerCase().contains("guardian") || offlinePlayer.getIpAsPunished().getBanReason().toLowerCase().contains("guardian") ? 35 : banner.getName().equals("CONSOLE") ? 100 : 0;
			if (banner.hasPlayed())
				bannerLevel = ModUtils.getLevel(banner);
			int unbannerLevel = ModUtils.getLevel(unbanner);
			// Rank level
			if (unbannerLevel != 100 && bannerLevel >= unbannerLevel
					&& !unbanner.getName().equalsIgnoreCase(banner.getName())) {
				sender.sendMessage("§c➤ Vous n'avez pas le pouvoir de débannir l'IP de ce joueur !");
				return;
			}
		}

		offlinePlayer.getIpAsPunished().setBan(false);
		offlinePlayer.getIpAsPunished().setBanEnd(-1);
		System.out.println(offlinePlayer.getName() + " unbanip savePunishions");
		offlinePlayer.getIpData().savePunishions();
		System.out.println(offlinePlayer.getName() + " unbanip saveData");
		offlinePlayer.getIpData().saveData();
		String bannerIp = sender instanceof Player ? ((Player) sender).getLastAddress().getHostAddress() : "127.0.0.1";
		BadblockDatabase.getInstance().addRequest(new Request(
				"INSERT INTO sanctions(pseudo, ip, type, expire, timestamp, date, reason, banner, fromIp) " + "VALUES('"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(offlinePlayer.getName()) + "', '"
						+ BadblockDatabase.getInstance()
								.mysql_real_escape_string(offlinePlayer.getLastAddress().getHostAddress())
						+ "', 'unbanip', '-1', '" + System.currentTimeMillis() + "', '"
						+ BadBlockOthers.getInstance().simpleDateFormat.format(new Date()) + "', '"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(args[1]) + "', '"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(sender.getName()) + "', '"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(bannerIp) + "')",
				RequestType.SETTER));
		sender.sendMessage("§a➤ Débannissement appliqué envers l'IP de " + offlinePlayer.getName() + ".");
		if (sender instanceof Player) {
			FriendPlayer fp = FriendPlayer.get((Player) sender);
			if (fp.lastSanction < System.currentTimeMillis()) {
				fp.sanctions++;
				fp.lastSanction = System.currentTimeMillis() + 300_000;
			}
		}
	}

}