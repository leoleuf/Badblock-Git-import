package fr.badblock.ladder.plugins.others.commands.mod.punish;

import java.util.Arrays;
import java.util.Date;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.Punished;
import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.ladder.api.utils.Time;
import fr.badblock.ladder.entities.LadderPlayer;
import fr.badblock.ladder.plugins.others.BadBlockOthers;
import fr.badblock.ladder.plugins.others.database.BadblockDatabase;
import fr.badblock.ladder.plugins.others.database.Request;
import fr.badblock.ladder.plugins.others.database.Request.RequestType;
import fr.badblock.ladder.plugins.others.friends.FriendPlayer;

public class CommandMute extends SanctionCommand {

	public static CommandMute instance;

	public CommandMute() {
		super("mute", "ladder.command.mute", "mt");
		instance = this;
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		boolean console = !(sender instanceof Player);
		if (!console)
			console = FriendPlayer.get((Player) sender).tail || sender.hasPermission("*");
		if (args.length < 3 && !console) {
			sender.sendMessage("§cUsage: /mute <pseudo> <temps> <raison>");
			return;
		} else if (args.length < 2 && console) {
			sender.sendMessage("§cUsage: /mute <pseudo> <temps> [raison]");
			return;
		}
		if (args.length == 2) {
			args = Arrays.copyOf(args, args.length + 1);
			args[2] = ".";
		}
		String reason = ChatColor.replaceColor(StringUtils.join(args, " ", 2));
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
						"§c➤ Ce joueur est plus haut gradé/au même grade que vous, impossible de le bâillonner !");
				return;
			}
		}

		// Already muted
		if (offlinePlayer.getAsPunished().isMute() && offlinePlayer.getAsPunished().getMuteEnd() > System.currentTimeMillis()) {
			sender.sendMessage("§c➤ Ce joueur est déjà baîlonné.");
			return;
		}

		long time = Time.MILLIS_SECOND.matchTime(args[1]);
		if (time > Time.DAY.convert(14L, Time.MILLIS_SECOND) && !sender.hasPermission("ladder.punish.bypass")) {
			sender.sendMessage("§c➤ Vous ne pouvez pas bâillonner plus de deux semaines.");
			return;
		} else if (time == 0L) {
			sender.sendMessage("§c➤ Veuillez entrer un temps valide.");
			return;
		}
		String bannerIp = sender instanceof Player ? ((Player) sender).getLastAddress().getHostAddress() : "127.0.0.1";
		Request request = new Request(
				"INSERT INTO sanctions(pseudo, ip, type, expire, timestamp, date, reason, banner, fromIp) " + "VALUES('"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(offlinePlayer.getName()) + "', '"
						+ BadblockDatabase.getInstance()
								.mysql_real_escape_string(offlinePlayer.getLastAddress().getHostAddress())
						+ "', 'mute', '" + (System.currentTimeMillis() + time) + "', '" + System.currentTimeMillis()
						+ "', '" + BadBlockOthers.getInstance().simpleDateFormat.format(new Date()) + "', '"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(reason) + "', '"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(sender.getName()) + "', '"
						+ BadblockDatabase.getInstance().mysql_real_escape_string(bannerIp) + "')",
				RequestType.SETTER);
		BadblockDatabase.getInstance().addSyncRequest(request);
		offlinePlayer.getAsPunished().setMuteId(request.id);
		offlinePlayer.getIpAsPunished().setMuteId(request.id);
		// Punish player name
		this.punish(offlinePlayer.getAsPunished(), time, sender, reason);
		offlinePlayer.savePunishions();

		// Punish player's IP
		this.punish(offlinePlayer.getIpAsPunished(), time, sender, reason);
		offlinePlayer.getIpData().savePunishions();

		// Saving datas
		offlinePlayer.saveData();
		offlinePlayer.getIpData().saveData();

		String humanTime = Time.MILLIS_SECOND.toFrench(time, Time.MINUTE, Time.DAY);
		if (offlinePlayer instanceof LadderPlayer) {
			LadderPlayer connected = (LadderPlayer) offlinePlayer;
			if (connected.getBukkitServer() != null) {
				connected.getBukkitServer().broadcast("§b➤ " + connected.getName() + " §7a été bâillonné par §b"
						+ sender.getName() + "§7 pendant §b" + humanTime + "§7 pour §b" + reason + "§7.");
			}
			connected.sendToBungee("punish");
			offlinePlayer.getIpData().sendToServers();
		}
		
		sender.sendMessage("§a➤ Bâillon appliqué envers " + offlinePlayer.getName() + " pendant " + humanTime + " ("
				+ reason + ").");
		if (sender instanceof Player) {
			FriendPlayer fp = FriendPlayer.get((Player) sender);
			if (fp.lastSanction < System.currentTimeMillis()) {
				fp.sanctions++;
				fp.lastSanction = System.currentTimeMillis() + 300_000;
			}
		}
	}

	private void punish(Punished punished, long time, CommandSender sender, String reason) {
		punished.setMute(true);
		punished.setMuteEnd(System.currentTimeMillis() + time);
		punished.setMuter(sender.getName());
		punished.setMuteReason(reason);
	}

}
