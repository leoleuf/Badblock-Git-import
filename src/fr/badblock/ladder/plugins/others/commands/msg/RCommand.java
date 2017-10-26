package fr.badblock.ladder.plugins.others.commands.msg;

import java.util.Date;

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

public class RCommand extends Command {

	public RCommand() {
		super("r", null, "reply");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(I18N.getTranslatedMessage("msg.onlyplayers"));
			return;
		}
		if (args.length < 1) {
			sender.sendMessages(I18N.getTranslatedMessages("msg.rusage"));
			return;
		}
		Player player = (Player) sender;
		FriendPlayer fromPlayer = FriendPlayer.get(player);
		if (fromPlayer == null)
			return;
		if (fromPlayer.flagMP > System.currentTimeMillis()) {
			sender.sendMessage(I18N.getTranslatedMessage("msg.wait"));
			return;
		}
		if (fromPlayer.getLastMP() == null) {
			player.sendMessage(I18N.getTranslatedMessage("msg.noonetorespond"));
			return;
		}
		String pseudo = fromPlayer.getLastMP();
		Player toPlayer = BadBlockOthers.getInstance().getLadder().getPlayer(pseudo);
		if (toPlayer == null || toPlayer.getBukkitServer() == null
				|| toPlayer.getBukkitServer().getName().startsWith("login")) {
			sender.sendMessage(I18N.getTranslatedMessage("msg.disconnected", pseudo));
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
		FriendPlayer friendPlayer = FriendPlayer.get(toPlayer);
		if (friendPlayer == null)
			return;
		FriendPlayer to = FriendPlayer.get(toPlayer);
		if (to == null)
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
			if (!friendPlayer.hasAcceptedFriend(toPlayer)
					&& friendPlayer.hasAcceptMPs().equals(AcceptType.ONLY_FRIENDS)) {
				sender.sendMessage(I18N.getTranslatedMessage("msg.errors.onlyfriends"));
				return;
			}
		}
		String message = "";
		int i = 0;
		for (String arg : args) {
			i++;
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
		if (message.contains("§") || message.contains("&0") || message.contains("&1") || message.contains("&2")
				|| message.contains("&3") || message.contains("&4") || message.contains("&5") || message.contains("&6")
				|| message.contains("&7") || message.contains("&8") || message.contains("&9") || message.contains("&a")
				|| message.contains("&b") || message.contains("&c") || message.contains("&d") || message.contains("&e")
				|| message.contains("&f")) {
			player.sendMessage(I18N.getTranslatedMessage("commands.antihackcolor"));
			return;
		}
		final String finalMessage = message;
		int uniqueId = MsgCommand.random.nextInt(Integer.MAX_VALUE);
		MsgCommand.messages.put(uniqueId, new PrivateMessage(sender.getName(), finalMessage));
		RawMessage rawMessage = Ladder.getInstance().createRawMessage("");
		rawMessage.add(Ladder.getInstance()
				.createRawMessage(I18N.getTranslatedMessageWithoutColor("msg.from", toPlayer.getName(), message, toPlayer.getAsPermissible().getDisplayName())));
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
					.addRequest(new Request(
							"INSERT INTO mp(pseudo, receiver, message, date, server, command) VALUES('"
									+ BadblockDatabase.getInstance().mysql_real_escape_string(sender.getName()) + "', '"
									+ BadblockDatabase.getInstance().mysql_real_escape_string(toPlayer.getName())
									+ "', '" + BadblockDatabase.getInstance().mysql_real_escape_string(finalMessage)
									+ "', '" + BadblockDatabase.getInstance().mysql_real_escape_string(date) + "', '"
									+ BadblockDatabase.getInstance().mysql_real_escape_string(serverName) + "', '/r')",
							RequestType.SETTER));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
