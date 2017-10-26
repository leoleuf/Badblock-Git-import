package fr.badblock.ladder.plugins.others.commands.msg;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.RawMessage;
import fr.badblock.ladder.api.chat.RawMessage.ClickEventType;
import fr.badblock.ladder.api.chat.RawMessage.HoverEventType;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.plugins.others.BadBlockOthers;
import fr.badblock.ladder.plugins.others.database.BadblockDatabase;
import fr.badblock.ladder.plugins.others.database.Request;
import fr.badblock.ladder.plugins.others.database.Request.RequestType;
import fr.badblock.ladder.plugins.others.friends.FriendPlayer;
import fr.badblock.ladder.plugins.others.mp.AcceptType;
import fr.badblock.ladder.plugins.others.utils.AntiHackColor;
import fr.badblock.ladder.plugins.others.utils.I18N;
import fr.badblock.permissions.PermissiblePlayer;

public class MsgCommand extends Command {

	public final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public static Map<Integer, PrivateMessage> messages = new HashMap<>();
	public static SecureRandom				   random   = new SecureRandom();
	
	public MsgCommand() {
		super("msg", null, "whisper", "m", "mp", "w", "tellraw", "tell", "minecraft:tell", "minecraft:tellraw",
				"minecraft:whisper", "minecraft:w");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(I18N.getTranslatedMessage("msg.onlyplayers"));
			return;
		}
		if (args.length < 2) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("set")) {
					Player player = (Player) sender;
					String[] strings = I18N.getTranslatedMessages("msg.set.onlyfor");
					for (String string : strings) {
						if (string.equals("@0")) {
							RawMessage rawMessage = Ladder.getInstance()
									.createRawMessage(I18N.getTranslatedMessage("party.set.all.message"));
							rawMessage.setClickEvent(ClickEventType.RUN_COMMAND, false, "/msg set ALL");
							rawMessage.setHoverEvent(HoverEventType.SHOW_TEXT, false,
									I18N.getTranslatedMessage("msg.set.for.all.hover"));
							rawMessage.send(player);
							rawMessage = Ladder.getInstance()
									.createRawMessage(I18N.getTranslatedMessage("party.set.friends.message"));
							rawMessage.setClickEvent(ClickEventType.RUN_COMMAND, false, "/msg set FRIENDS");
							rawMessage.setHoverEvent(HoverEventType.SHOW_TEXT, false,
									I18N.getTranslatedMessage("msg.set.for.friends.hover"));
							rawMessage.send(player);
							rawMessage = Ladder.getInstance()
									.createRawMessage(I18N.getTranslatedMessage("party.set.noone.message"));
							rawMessage.setClickEvent(ClickEventType.RUN_COMMAND, false, "/msg set NOTHING");
							rawMessage.setHoverEvent(HoverEventType.SHOW_TEXT, false,
									I18N.getTranslatedMessage("msg.set.for.noone.hover"));
							rawMessage.send(player);
						} else
							player.sendMessage(string);
					}
					return;
				}
			}
			sender.sendMessages(I18N.getTranslatedMessages("msg.usage"));
			return;
		}
		String pseudo = args[0];
		if (pseudo.equalsIgnoreCase(sender.getName())) {
			sender.sendMessage(I18N.getTranslatedMessage("msg.himself"));
			return;
		}
		Player player = (Player) sender;
		if (pseudo.equalsIgnoreCase("set")) {
			String arg = args[1];
			FriendPlayer fromPlayer = FriendPlayer.get(player);
			if (fromPlayer == null)
				return;
			if (arg.equalsIgnoreCase("ALL")) {
				if (fromPlayer.hasAcceptMPs().equals(AcceptType.ALL_PEOPLE)) {
					player.sendMessage(I18N.getTranslatedMessage("msg.set.for.all.already"));
					return;
				}
				fromPlayer.setAcceptedMPs(AcceptType.ALL_PEOPLE);
				fromPlayer.hasNewChanges = true;
				player.sendMessage(I18N.getTranslatedMessage("msg.set.for.all.done"));
				return;
			}
			PermissiblePlayer p = (PermissiblePlayer) player.getAsPermissible();
			for (String group : p.getAlternateGroups().keySet()) {
				if (group.toLowerCase().contains("modo")) {
					player.sendMessage(I18N.getTranslatedMessage("msg.set.for.friends.youaremodo"));
					return;
				}
			}
			if (p.getSuperGroup().toLowerCase().contains("modo")) {
				player.sendMessage(I18N.getTranslatedMessage("msg.set.for.friends.youaremodo"));
				return;
			}
			if (arg.equalsIgnoreCase("FRIENDS")) {
				if (fromPlayer.hasAcceptMPs().equals(AcceptType.ONLY_FRIENDS)) {
					player.sendMessage(I18N.getTranslatedMessage("msg.set.for.friends.already"));
					return;
				}
				fromPlayer.setAcceptedMPs(AcceptType.ONLY_FRIENDS);
				fromPlayer.hasNewChanges = true;
				player.sendMessage(I18N.getTranslatedMessage("msg.set.for.friends.done"));
				return;
			}
			if (arg.equalsIgnoreCase("NOTHING")) {
				if (fromPlayer.hasAcceptMPs().equals(AcceptType.NO_ONE)) {
					player.sendMessage(I18N.getTranslatedMessage("msg.set.for.noone.already"));
					return;
				}
				fromPlayer.setAcceptedMPs(AcceptType.NO_ONE);
				fromPlayer.hasNewChanges = true;
				player.sendMessage(I18N.getTranslatedMessage("msg.set.for.noone.done"));
				return;
			}
			player.sendMessage(I18N.getTranslatedMessage("msg.set.for.invalid"));
			return;
		}
		Player toPlayer = BadBlockOthers.getInstance().getLadder().getPlayer(pseudo);
		if (toPlayer == null || toPlayer.getBukkitServer() == null || toPlayer.getBukkitServer() == null
				|| toPlayer.getBukkitServer().getName() == null
				|| toPlayer.getBukkitServer().getName().startsWith("login")) {
			sender.sendMessage(I18N.getTranslatedMessage("msg.disconnect", pseudo));
			return;
		}
		FriendPlayer fromPlayer = FriendPlayer.get(player);
		if (fromPlayer == null)
			return;
		if (fromPlayer.flagMP > System.currentTimeMillis()) {
			sender.sendMessage(I18N.getTranslatedMessage("msg.wait"));
			return;
		}
		FriendPlayer to = FriendPlayer.get(toPlayer);
		if (to == null)
			return;
		FriendPlayer friendPlayer = FriendPlayer.get(toPlayer);
		if (friendPlayer == null)
			return;
		boolean bypass = player.hasPermission("others.mp.bypass");
		PermissiblePlayer toPlayerP = (PermissiblePlayer) toPlayer.getAsPermissible();
		for (String group : toPlayerP.getAlternateGroups().keySet())
			if (group.toLowerCase().contains("modo"))
				bypass = true;
		if (toPlayerP.getSuperGroup().toLowerCase().contains("modo"))
			bypass = true;
		if (!bypass) {
			if (friendPlayer.hasAcceptMPs().equals(AcceptType.NO_ONE)) {
				sender.sendMessage(I18N.getTranslatedMessage("msg.errors.refused"));
				return;
			}
			if (!friendPlayer.hasAcceptedFriend(player)
					&& friendPlayer.hasAcceptMPs().equals(AcceptType.ONLY_FRIENDS)) {
				sender.sendMessage(I18N.getTranslatedMessage("msg.errors.onlyfriends"));
				return;
			}
		}
		String message = "";
		int i = 0;
		for (String arg : args) {
			i++;
			if (i == 1)
				continue;
			String spacer = " ";
			if (args.length == i)
				spacer = "";
			message += arg + spacer;
		}
		if (message.equalsIgnoreCase(fromPlayer.lastMsg)) {
			sender.sendMessage(I18N.getTranslatedMessage("msg.spammer"));
			return;
		}
		if (AntiHackColor.isHacking(player, message)) {
			return;
		}
		if (player.getIpAsPunished() != null && player.getIpAsPunished().isMute()) {
			player.sendMessage(I18N.getTranslatedMessage("msg.youaremute"));
			return;
		}
		if (player.getAsPunished() != null && player.getAsPunished().isMute()) {
			player.sendMessage(I18N.getTranslatedMessage("msg.youaremute"));
			return;
		}
		if (toPlayer.getIpAsPunished() != null && toPlayer.getIpAsPunished().isMute()) {
			player.sendMessage(I18N.getTranslatedMessage("msg.mute"));
			return;
		}
		if (toPlayer.getAsPunished() != null && toPlayer.getAsPunished().isMute()) {
			player.sendMessage(I18N.getTranslatedMessage("msg.mute"));
			return;
		}
		if (message.contains("§") || message.contains("&0") || message.contains("&1") || message.contains("&2")
				|| message.contains("&3") || message.contains("&4") || message.contains("&5") || message.contains("&6")
				|| message.contains("&7") || message.contains("&8") || message.contains("&9") || message.contains("&a")
				|| message.contains("&b") || message.contains("&c") || message.contains("&d") || message.contains("&e")
				|| message.contains("&f")) {
			player.sendMessage(I18N.getTranslatedMessage("commands.antihackcolor"));
			return;
		}
		final String finalMessage = message;
		int uniqueId = random.nextInt(Integer.MAX_VALUE);
		messages.put(uniqueId, new PrivateMessage(sender.getName(), finalMessage));
		RawMessage rawMessage = Ladder.getInstance()
				.createRawMessage(I18N.getTranslatedMessageWithoutColor("msg.from", toPlayer.getName(), message, toPlayer.getAsPermissible().getDisplayName()));
		rawMessage.setHoverEvent(HoverEventType.SHOW_TEXT, false,
				I18N.getTranslatedMessage("msg.clickforrespond", toPlayer.getName()));
		rawMessage.setClickEvent(ClickEventType.SUGGEST_COMMAND, false, "/msg " + toPlayer.getName() + " ");
		rawMessage.send(player);
		rawMessage = Ladder.getInstance().createRawMessage("");
		RawMessage alert = Ladder.getInstance().createRawMessage("§c§l⚠§r ");
		alert.setClickEvent(ClickEventType.RUN_COMMAND, false, "/cbreport " + uniqueId);
		alert.setHoverEvent(HoverEventType.SHOW_TEXT, false, "§cSignaler le message privé de " + sender.getName());
		rawMessage.add(alert);
		RawMessage rowMessage = Ladder.getInstance()
				.createRawMessage(I18N.getTranslatedMessageWithoutColor("msg.to", sender.getName(), message, player.getAsPermissible().getDisplayName()));
		rowMessage.setHoverEvent(HoverEventType.SHOW_TEXT, false,
				I18N.getTranslatedMessage("msg.clickforrespond", player.getName()));
		rowMessage.setClickEvent(ClickEventType.SUGGEST_COMMAND, false, "/msg " + player.getName() + " ");
		rawMessage.add(rowMessage);
		rawMessage.send(toPlayer);

		String date = MsgCommand.dateFormat.format(new Date());
		rawMessage = Ladder.getInstance().createRawMessage(I18N.getTranslatedMessageWithoutColor("msg.fromto",
				sender.getName(), toPlayer.getName(), finalMessage));
		rawMessage.setHoverEvent(HoverEventType.SHOW_TEXT, false,
				I18N.getTranslatedMessage("msg.sender", player.getAsPermissible().getDisplayName() + sender.getName()),
				I18N.getTranslatedMessage("msg.sendto", toPlayer.getAsPermissible().getDisplayName() + toPlayer.getName()),
				I18N.getTranslatedMessage("msg.server", player.getBukkitServer().getName()),
				I18N.getTranslatedMessage("msg.date", date));
		for (Player pl : BadBlockOthers.getInstance().getLadder().getOnlinePlayers()) {
			FriendPlayer fPl = FriendPlayer.get(pl);
			if (fPl == null)
				continue;
			if (pl.getBukkitServer() == null || pl.getBukkitServer().getName().startsWith("login"))
				continue;
			if (!fPl.spy)
				continue;
			if (!pl.hasPermission("others.spymsg")) {
				pl.sendMessage(I18N.getTranslatedMessage("msg.spymode.alreadybutnoperm"));
				fPl.spy = false;
				continue;
			}
			rawMessage.send(pl);
		}
		fromPlayer.setLastMP(toPlayer.getName());
		to.setLastMP(player.getName());
		fromPlayer.flagMP = System.currentTimeMillis() + 1000L;
		fromPlayer.lastMsg = message;
		try {
			String serverName = player.getBukkitServer().getName();
			BadblockDatabase.getInstance()
					.addRequest(new Request("INSERT INTO mp(pseudo, receiver, message, date, server, command) VALUES('"
							+ BadblockDatabase.getInstance().mysql_real_escape_string(sender.getName()) + "', '"
							+ BadblockDatabase.getInstance().mysql_real_escape_string(toPlayer.getName()) + "', '"
							+ BadblockDatabase.getInstance().mysql_real_escape_string(finalMessage) + "', '"
							+ BadblockDatabase.getInstance().mysql_real_escape_string(date) + "', '"
							+ BadblockDatabase.getInstance().mysql_real_escape_string(serverName) + "', '/msg')",
							RequestType.SETTER));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
