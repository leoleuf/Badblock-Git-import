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

public class CommandBan extends SanctionCommand {

	public static CommandBan instance;

	public CommandBan() {
		super("ban", "ladder.command.ban", "bn");
		instance = this;
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		boolean console = !(sender instanceof Player);
		if (!console)
			console = FriendPlayer.get((Player) sender).tail || sender.hasPermission("*");
		if (args.length < 2 && !console) {
			sender.sendMessage("§cUsage: /ban <pseudo> <raison>");
			return;
		} else if (args.length < 1 && console) {
			sender.sendMessage("§cUsage: /ban <pseudo> [raison]");
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
								"§c➤ Ce joueur est plus haut gradé/au même grade que vous, impossible de le bannir !");
						return;
					}
				}

				// Already banned
				if (offlinePlayer.getAsPunished().isBan()) {
					sender.sendMessage("§c➤ Ce joueur est déjà banni.");
					return;
				}

				long time = Time.YEAR.convert(1L, Time.MILLIS_SECOND);

				// Punish player name
				this.punish(offlinePlayer.getAsPunished(), time, sender, reason);
				String bannerIp = sender instanceof Player ? ((Player) sender).getLastAddress().getHostAddress() : "127.0.0.1";
				Request request = new Request(
						"INSERT INTO sanctions(pseudo, ip, type, expire, timestamp, date, reason, banner, fromIp) " + "VALUES('"
								+ BadblockDatabase.getInstance().mysql_real_escape_string(offlinePlayer.getName()) + "', '"
								+ BadblockDatabase.getInstance()
										.mysql_real_escape_string(offlinePlayer.getLastAddress().getHostAddress())
								+ "', 'tempban', '" + (time + System.currentTimeMillis()) + "', '" + System.currentTimeMillis()
								+ "', '" + BadBlockOthers.getInstance().simpleDateFormat.format(new Date()) + "', '"
								+ BadblockDatabase.getInstance().mysql_real_escape_string(reason) + "', '"
								+ BadblockDatabase.getInstance().mysql_real_escape_string(sender.getName()) + "', '"
								+ BadblockDatabase.getInstance().mysql_real_escape_string(bannerIp) + "')",
						RequestType.SETTER);
				BadblockDatabase.getInstance().addSyncRequest(request);
				offlinePlayer.getAsPunished().setBanId(request.id);
				offlinePlayer.savePunishions();

				String humanTime = Time.MILLIS_SECOND.toFrench(time, Time.MINUTE, Time.YEAR);
				if (offlinePlayer instanceof LadderPlayer) {
					LadderPlayer connected = (LadderPlayer) offlinePlayer;
					if (connected.getBukkitServer() != null) {
						connected.getBukkitServer().broadcast("§b➤ " + connected.getName() + " §7a été banni par §b"
								+ sender.getName() + "§7 pendant §b" + humanTime + "§7 pour §b" + reason + "§7.");
					}
					connected.disconnect(buildBanReason(System.currentTimeMillis() + time, reason));
				}

				// Saving datas
				offlinePlayer.saveData();
				sender.sendMessage("§a➤ Bannissement appliqué envers " + offlinePlayer.getName() + " pendant " + humanTime
						+ " (" + reason + ").");
			}

			public String buildBanReason(long expire, String banReason) {
				String time = "Permanent";
				if (expire != -1L)
					time = Time.MILLIS_SECOND.toFrench(expire - System.currentTimeMillis(), Time.MINUTE, Time.YEAR);
				return "Vous êtes banni de ce serveur ! (Temps: " + time + "§r | Motif: " + banReason.replace("§", "&") + "§r)";
			}

			private void punish(Punished punished, long time, CommandSender sender, String reason) {
				punished.setBan(true);
				punished.setBanEnd(System.currentTimeMillis() + time);
				punished.setBanner(sender.getName());
				punished.setBanReason(reason);
			}

}
