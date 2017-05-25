package fr.badblock.ladder.plugins.others.commands.mod.punish;

import java.util.Arrays;
import java.util.Date;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.ladder.plugins.others.BadBlockOthers;
import fr.badblock.ladder.plugins.others.database.BadblockDatabase;
import fr.badblock.ladder.plugins.others.database.Request;
import fr.badblock.ladder.plugins.others.database.Request.RequestType;
import fr.badblock.ladder.plugins.others.friends.FriendPlayer;

public class CommandKick extends SanctionCommand {

	public static CommandKick instance;

	public CommandKick() {
		super("kick", "ladder.command.kick", "kck");
		instance = this;
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		boolean console = !(sender instanceof Player);
		if (!console)
			console = (FriendPlayer.get((Player) sender) != null && FriendPlayer.get((Player) sender).tail) || sender.hasPermission("*");
		if (args.length < 2 && !console) {
			sender.sendMessage("§cUsage: /kick <pseudo> <raison>");
			return;
		} else if (args.length < 1 && console) {
			sender.sendMessage("§cUsage: /kick <pseudo> [raison]");
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
		Player player = ladder.getPlayer(args[0]);
		// Unknown player?
		if (player == null) {
			sender.sendMessage("§c➤ " + args[0] + " n'est pas connecté.");
			return;
		}

		// If it's a player (not a console, etc.)
		if (sender instanceof Player) {
			Player banner = (Player) sender;
			int bannerLevel = ModUtils.getLevel(banner);
			int bannedLevel = ModUtils.getLevel(player);
			// Rank level
			if (bannerLevel != 100 && bannerLevel <= bannedLevel) {
				sender.sendMessage(
						"§c➤ Ce joueur est plus haut gradé/au même grade que vous, impossible de l'éjecter !");
				return;
			}
		}

		player.disconnect("Vous avez été éjecté pour " + reason + " par " + sender.getName() + ".");
		String bannerIp = sender instanceof Player ? ((Player) sender).getLastAddress().getHostAddress() : "127.0.0.1";
		BadblockDatabase.getInstance().addRequest(new Request(
				"INSERT INTO sanctions(pseudo, ip, type, expire, timestamp, date, reason, banner, fromIp) " + "VALUES('"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(player.getName()) + "', '"
						+ BadblockDatabase.getInstance()
								.mysql_real_escape_string(player.getLastAddress().getHostAddress())
						+ "', 'kick', '-1', '" + System.currentTimeMillis() + "', '"
						+ BadBlockOthers.getInstance().simpleDateFormat.format(new Date()) + "', '"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(reason) + "', '"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(sender.getName()) + "', '"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(bannerIp) + "')",
				RequestType.SETTER));
		sender.sendMessage("§a➤ Kick appliqué envers " + player.getName() + " (" + reason + ").");
		if (sender instanceof Player) {
			FriendPlayer fp = FriendPlayer.get((Player) sender);
			if (fp.lastSanction < System.currentTimeMillis()) {
				fp.sanctions++;
				fp.lastSanction = System.currentTimeMillis() + 300_000;
			}
		}
	}

}
