package fr.badblock.ladder.plugins.others.rabbit.receivers;

import java.sql.ResultSet;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.config.Configuration;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.plugins.others.BadBlockOthers;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandWarn;
import fr.badblock.ladder.plugins.others.database.BadblockDatabase;
import fr.badblock.ladder.plugins.others.database.Request;
import fr.badblock.ladder.plugins.others.database.Request.RequestType;
import fr.badblock.ladder.plugins.others.objects.SanctionFactory;
import fr.badblock.ladder.plugins.others.utils.I18N;
import fr.badblock.rabbitconnector.RabbitConnector;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;

public class ReceiveSanctionListener extends RabbitListener {

	public ReceiveSanctionListener() {
		super(RabbitConnector.getInstance().getService("default"), "sanction", false,
				RabbitListenerType.MESSAGE_BROKER);
	}

	@Override
	public void onPacketReceiving(String string) {
		if (string == null)
			return;
		SanctionFactory sanctionFactory = BadBlockOthers.getInstance().getGson().fromJson(string,
				SanctionFactory.class);
		if (sanctionFactory == null)
			return;
		OfflinePlayer offlinePlayer = Ladder.getInstance().getOfflinePlayer(sanctionFactory.getPseudo());
		if (offlinePlayer == null) {
			System.out.println("[SANCTION] " + sanctionFactory.getPseudo() + " has never played before.");
			return;
		}
		if (offlinePlayer == null || !offlinePlayer.hasPlayed()) {
			System.out.println("[SANCTION] " + sanctionFactory.getPseudo() + " has never played before.");
			return;
		}
		if (sanctionFactory.isAuto()) {
			Configuration config = BadBlockOthers.getInstance().getConfig().getSection("lang.punishments.table");
			String reason = sanctionFactory.getReason();
			if (!config.contains(reason)) {
				System.out.println("[SANCTION] Unknown '" + reason + "' reason.");
				return;
			}
			List<Entry<String, Integer>> list = new ArrayList<>();
			for (String key : config.getSection(reason).getKeys()) {
				Configuration c = config.getSection(reason).getSection(key);
				list.add(new AbstractMap.SimpleEntry<>(c.getString("type"), c.getInt("time")));
			}
			List<String> helpText = I18N.getTranslatedListMessages("punishments.msg.help.text");
			List<String> helpClick = I18N.getTranslatedListMessages("punishments.msg.help.click");
			List<String> helpHover = I18N.getTranslatedListMessages("punishments.msg.help.hover");
			if (helpText.size() != helpClick.size() || helpText.size() != helpHover.size()) {
				CommandSender commandSender = Ladder.getInstance().getConsoleCommandSender();
				commandSender.sendMessage("§cSynchronization error in the I18N language file: ");
				commandSender.sendMessage("Length: [Text: " + helpText.size() + "];[Click: " + helpClick.size()
				+ "];[Hover: " + helpHover.size() + "]");
				commandSender.sendMessage(
						"§cPlease have the same length in the punishments.msg.help.text, punishments.msg.help.click and punishments.msg.help.hover I18N keys");
				return;
			}
			Iterator<String> textIterator = helpText.iterator();
			Iterator<String> clickIterator = helpClick.iterator();
			String shownReason = reason;
			while (textIterator.hasNext()) {
				String textMessage = textIterator.next();
				String clickMessage = clickIterator.next();
				if (clickMessage.equalsIgnoreCase("run;/bab @0 " + reason)) {
					shownReason = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', textMessage));
				}
			}
			final String finalShownReason = shownReason;
			BadblockDatabase.getInstance()
			.addRequest(new Request(
					"SELECT COUNT(*) AS count FROM sanctions WHERE pseudo = '"
							+ BadblockDatabase.getInstance().mysql_real_escape_string(offlinePlayer.getName())
							+ "' AND reason = '"
							+ BadblockDatabase.getInstance().mysql_real_escape_string(finalShownReason) + "'",
							RequestType.GETTER) {
				@Override
				public void done(ResultSet resultSet) {
					try {
						if (resultSet.next()) {
							int count = resultSet.getInt("count");
							int next = count + 1;
							Entry<String, Integer> entry = list.size() >= next ? list.get(count)
									: list.get(list.size() - 1);
							if (entry == null) {
								System.out.println("[SANCTION] Unknown '" + reason + "' entries.");
								return;
							}
							sanctionFactory.setType(entry.getKey());
							sanctionFactory.setExpire(System.currentTimeMillis() + (entry.getValue() * 1000));
							apply(sanctionFactory, offlinePlayer, finalShownReason);
						}
					} catch (Exception error) {
						error.printStackTrace();
					}
				}
			});
		} else
			apply(sanctionFactory, offlinePlayer, sanctionFactory.getReason());
	}

	public void apply(SanctionFactory sanctionFactory, OfflinePlayer offlinePlayer, String shownReason) {
		List<String> types = new ArrayList<>();
		Player playerd = Ladder.getInstance().getPlayer(offlinePlayer.getName());
		switch (sanctionFactory.getType()) {
		case "ban":
			offlinePlayer.getAsPunished().setBan(true);
			offlinePlayer.getAsPunished().setBanEnd(-1L);
			offlinePlayer.getAsPunished().setBanner(sanctionFactory.getBanner());
			offlinePlayer.getAsPunished().setBanReason(shownReason);
			offlinePlayer.savePunishions();
			offlinePlayer.saveData();
			types.add("ban");
			sanctionFactory.setExpire(-1);
			if (playerd != null) {
				playerd.sendToBungee("punish");
			}
			break;
		case "bban":
			offlinePlayer.getAsPunished().setBan(true);
			offlinePlayer.getAsPunished().setBanEnd(-1L);
			offlinePlayer.getAsPunished().setBanner(sanctionFactory.getBanner());
			offlinePlayer.getAsPunished().setBanReason(shownReason);
			offlinePlayer.savePunishions();
			offlinePlayer.getIpAsPunished().setBan(true);
			offlinePlayer.getIpAsPunished().setBanEnd(-1L);
			offlinePlayer.getIpAsPunished().setBanner(sanctionFactory.getBanner());
			offlinePlayer.getIpAsPunished().setBanReason(shownReason);
			offlinePlayer.getIpData().savePunishions();
			offlinePlayer.getIpData().saveData();
			offlinePlayer.saveData();
			types.add("ban");
			types.add("banip");
			sanctionFactory.setExpire(-1);
			if (playerd != null) {
				playerd.sendToBungee("punish");
			}
			offlinePlayer.getIpData().sendToServers();
			break;
		case "banip":
			offlinePlayer.getIpAsPunished().setBan(true);
			offlinePlayer.getIpAsPunished().setBanEnd(-1L);
			offlinePlayer.getIpAsPunished().setBanner(sanctionFactory.getBanner());
			offlinePlayer.getIpAsPunished().setBanReason(shownReason);
			offlinePlayer.getIpData().savePunishions();
			offlinePlayer.saveData();
			types.add("banip");
			sanctionFactory.setExpire(-1);
			if (playerd != null) {
				playerd.sendToBungee("punish");
			}
			offlinePlayer.getIpData().sendToServers();
			break;
		case "kick":
			Player player = Ladder.getInstance().getPlayer(sanctionFactory.getPseudo());
			if (player == null) {
				System.out.println("[SANCTION] " + sanctionFactory.getPseudo() + " is offline, unable to kick him.");
				return;
			}
			sanctionFactory.setExpire(-1);
			types.add("kick");
			break;
		case "warn":
			sanctionFactory.setExpire(-1);
			types.add("warn");
			Player playerz = Ladder.getInstance().getPlayer(sanctionFactory.getPseudo());
			if (playerz == null) {
				System.out.println("[SANCTION] " + sanctionFactory.getPseudo() + " is offline, unable to warn him.");
				return;
			}

			break;
		case "mute":
			offlinePlayer.getAsPunished().setMute(true);
			offlinePlayer.getAsPunished().setMuteEnd(sanctionFactory.getExpire());
			offlinePlayer.getAsPunished().setMuter(sanctionFactory.getBanner());
			offlinePlayer.getAsPunished().setMuteReason(shownReason);
			offlinePlayer.savePunishions();
			offlinePlayer.getIpAsPunished().setMute(true);
			offlinePlayer.getIpAsPunished().setMuteEnd(sanctionFactory.getExpire());
			offlinePlayer.getIpAsPunished().setMuter(sanctionFactory.getBanner());
			offlinePlayer.getIpAsPunished().setMuteReason(shownReason);
			offlinePlayer.getIpData().savePunishions();
			offlinePlayer.getIpData().saveData();
			offlinePlayer.saveData();
			types.add("mute");
			if (playerd != null) {
				playerd.sendToBungee("punish");
			}
			offlinePlayer.getIpData().sendToServers();
			break;
		case "tempban":
			offlinePlayer.getAsPunished().setBan(true);
			offlinePlayer.getAsPunished().setBanEnd(sanctionFactory.getExpire());
			offlinePlayer.getAsPunished().setBanner(sanctionFactory.getBanner());
			offlinePlayer.getAsPunished().setBanReason(shownReason);
			offlinePlayer.savePunishions();
			offlinePlayer.saveData();
			types.add("tempban");
			if (playerd != null) {
				playerd.sendToBungee("punish");
			}
			break;
		case "btempban":
			offlinePlayer.getAsPunished().setBan(true);
			offlinePlayer.getAsPunished().setBanEnd(sanctionFactory.getExpire());
			offlinePlayer.getAsPunished().setBanner(sanctionFactory.getBanner());
			offlinePlayer.getAsPunished().setBanReason(shownReason);
			offlinePlayer.savePunishions();
			offlinePlayer.getIpAsPunished().setBan(true);
			offlinePlayer.getIpAsPunished().setBanEnd(sanctionFactory.getExpire());
			offlinePlayer.getIpAsPunished().setBanner(sanctionFactory.getBanner());
			offlinePlayer.getIpAsPunished().setBanReason(shownReason);
			offlinePlayer.getIpData().savePunishions();
			offlinePlayer.getIpData().saveData();
			offlinePlayer.saveData();
			types.add("tempban");
			types.add("tempbanip");
			if (playerd != null) {
				playerd.sendToBungee("punish");
			}
			offlinePlayer.getIpData().sendToServers();
			break;
		case "tempbanip":
			offlinePlayer.getIpAsPunished().setBan(true);
			offlinePlayer.getIpAsPunished().setBanEnd(sanctionFactory.getExpire());
			offlinePlayer.getIpAsPunished().setBanner(sanctionFactory.getBanner());
			offlinePlayer.getIpAsPunished().setBanReason(shownReason);
			offlinePlayer.getIpData().savePunishions();
			offlinePlayer.getIpData().saveData();
			types.add("tempbanip");
			if (playerd != null) {
				playerd.sendToBungee("punish");
			}
			offlinePlayer.getIpData().sendToServers();
			break;
		case "unban":
			offlinePlayer.getAsPunished().setBan(false);
			offlinePlayer.getAsPunished().setBanEnd(-1);
			offlinePlayer.savePunishions();
			sanctionFactory.setExpire(-1);
			offlinePlayer.saveData();
			types.add("unban");
			if (playerd != null) {
				playerd.sendToBungee("punish");
			}
			offlinePlayer.getIpData().sendToServers();
			break;
		case "unbanip":
			offlinePlayer.getIpAsPunished().setBan(false);
			offlinePlayer.getIpAsPunished().setBanEnd(-1);
			offlinePlayer.getIpData().savePunishions();
			sanctionFactory.setExpire(-1);
			offlinePlayer.getIpData().saveData();
			types.add("unbanip");
			if (playerd != null) {
				playerd.sendToBungee("punish");
			}
			offlinePlayer.getIpData().sendToServers();
			break;
		case "bpardon":
			offlinePlayer.getAsPunished().setBan(false);
			offlinePlayer.getAsPunished().setBanEnd(-1);
			offlinePlayer.savePunishions();
			offlinePlayer.getIpAsPunished().setBan(false);
			offlinePlayer.getIpAsPunished().setBanEnd(-1);
			offlinePlayer.getIpData().savePunishions();
			sanctionFactory.setExpire(-1);
			offlinePlayer.getIpData().saveData();
			offlinePlayer.saveData();
			types.add("unban");
			types.add("unbanip");
			if (playerd != null) {
				playerd.sendToBungee("punish");
			}
			offlinePlayer.getIpData().sendToServers();
			break;
		case "unmute":
			offlinePlayer.getAsPunished().setMute(false);
			offlinePlayer.getAsPunished().setMuteEnd(-1);
			offlinePlayer.savePunishions();
			offlinePlayer.getIpAsPunished().setMute(false);
			offlinePlayer.getIpAsPunished().setMuteEnd(-1);
			offlinePlayer.getIpData().savePunishions();
			offlinePlayer.getIpData().saveData();
			offlinePlayer.saveData();
			sanctionFactory.setExpire(-1);
			types.add("unmute");
			if (playerd != null) {
				playerd.sendToBungee("punish");
			}
			offlinePlayer.getIpData().sendToServers();
			break;
		default:
			System.out.println("[SANCTION] Unknown '" + sanctionFactory.getReason() + "' reason!");
			return;
		}
		types.forEach(type -> {
			Request request = new Request(
					"INSERT INTO sanctions(pseudo, ip, type, expire, timestamp, date, reason, banner, fromIp, proof) "
							+ "VALUES('" + BadblockDatabase.getInstance().mysql_real_escape_string(offlinePlayer.getName())
							+ "', '"
							+ BadblockDatabase.getInstance()
							.mysql_real_escape_string(offlinePlayer.getLastAddress().getHostAddress())
							+ "', '" + BadblockDatabase.getInstance().mysql_real_escape_string(type) + "', '"
							+ sanctionFactory.getExpire() + "', '" + sanctionFactory.getTimestamp() + "', '"
							+ BadblockDatabase.getInstance()
							.mysql_real_escape_string(BadBlockOthers.getInstance().simpleDateFormat
									.format(new Date(sanctionFactory.getTimestamp())))
							+ "', '" + BadblockDatabase.getInstance().mysql_real_escape_string(shownReason) + "', '"
							+ BadblockDatabase.getInstance().mysql_real_escape_string(sanctionFactory.getBanner()) + "', '"
							+ BadblockDatabase.getInstance().mysql_real_escape_string(sanctionFactory.getFromIp()) + "', '"
							+ BadblockDatabase.getInstance().mysql_real_escape_string(sanctionFactory.getProof()) + "')",
							RequestType.SETTER);
			BadblockDatabase.getInstance().addSyncRequest(request);
			if (type.equals("ban") || type.equals("tempban")) {
				offlinePlayer.getAsPunished().setBanId(request.id);
				offlinePlayer.savePunishions();
				offlinePlayer.saveData();
			}else if (type.equals("banip") || type.equals("tempbanip")) {
				offlinePlayer.getIpAsPunished().setBanId(request.id);
				offlinePlayer.getIpData().savePunishions();
				offlinePlayer.getIpData().saveData();
			}else if (type.equals("mute")) {
				offlinePlayer.getAsPunished().setMuteId(request.id);
				offlinePlayer.savePunishions();
				offlinePlayer.saveData();
			}else if (type.equals("muteip")) {
				offlinePlayer.getIpAsPunished().setMuteId(request.id);
				offlinePlayer.getIpData().savePunishions();
				offlinePlayer.getIpData().saveData();
			}
			if (playerd != null)
			{
				Request requestz = new Request(
						"SELECT * FROM sanctions WHERE pseudo = '"
								+ BadblockDatabase.getInstance().mysql_real_escape_string(playerd.getName()) + "' AND expire = '-1' AND type = 'warn'",
								RequestType.GETTER)
				{
					@Override
					public void done(ResultSet resultSet)
					{
						try
						{
							while (resultSet.next())
							{
								String banner = resultSet.getString("banner");
								String date = CommandWarn.sdf.format(new Date(resultSet.getLong("timestamp")));
								String reason = resultSet.getString("reason");
								playerd.sendMessages(I18N.getTranslatedMessages("msg.warn", banner, reason, date));
							}
						}
						catch(Exception error)
						{
							error.printStackTrace();
						}
					}
				};
				BadblockDatabase.getInstance().addRequest(requestz);
			}
		});

	}

}
