package fr.badblock.ladder.plugins.others.commands.big;

import java.sql.ResultSet;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.chat.RawMessage;
import fr.badblock.ladder.api.chat.RawMessage.ClickEventType;
import fr.badblock.ladder.api.chat.RawMessage.HoverEventType;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.config.Configuration;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.plugins.others.BadBlockOthers;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandBan;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandBanip;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandKick;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandMute;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandTempban;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandTempbanip;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandUnban;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandUnbanip;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandUnmute;
import fr.badblock.ladder.plugins.others.commands.mod.punish.CommandWarn;
import fr.badblock.ladder.plugins.others.commands.mod.punish.ModUtils;
import fr.badblock.ladder.plugins.others.database.BadblockDatabase;
import fr.badblock.ladder.plugins.others.database.Request;
import fr.badblock.ladder.plugins.others.database.Request.RequestType;
import fr.badblock.ladder.plugins.others.utils.I18N;

public class BigAutoBan extends Command {

	public BigAutoBan() {
		super("bigautoban", "ladder.command.bab", "bab");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		BadBlockOthers instance = BadBlockOthers.getInstance();
		if (!instance.getConfig().contains("lang.punishments.table")) {
			sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.errors.unknowntable"));
			return;
		}
		Configuration config = instance.getConfig().getSection("lang.punishments.table");
		if (args.length != 1 && args.length != 2) {
			sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.usage"));
			return;
		}
		int fromLevel = 100;
		if (sender instanceof Player)
			fromLevel = ModUtils.getLevel((Player) sender);
		if (args.length >= 1) {
			OfflinePlayer offlinePlayer = Ladder.getInstance().getOfflinePlayer(args[0]);
			// Unknown player?
			if (!offlinePlayer.hasPlayed()) {
				sender.sendMessage("§c➤ " + args[0] + " ne s'est jamais connecté sur le serveur.");
				return;
			}
			int toLevel = ModUtils.getLevel(offlinePlayer);
			if (fromLevel != 100 && fromLevel <= toLevel) {
				sender.sendMessage(
						"§c➤ Ce joueur est plus haut gradé/au même grade que vous, impossible de le sanctionner !");
				return;
			}
			if (args.length == 1) {
				new Thread() {
					@Override
					public void run() {
						List<String> helpText = I18N.getTranslatedListMessages("punishments.msg.help.text");
						List<String> helpClick = I18N.getTranslatedListMessages("punishments.msg.help.click");
						List<String> helpHover = I18N.getTranslatedListMessages("punishments.msg.help.hover");
						if (helpText.size() != helpClick.size() || helpText.size() != helpHover.size()) {
							sender.sendMessage("§cSynchronization error in the I18N language file: ");
							sender.sendMessage("Length: [Text: " + helpText.size() + "];[Click: " + helpClick.size()
									+ "];[Hover: " + helpHover.size() + "]");
							sender.sendMessage(
									"§cPlease have the same length in the punishments.msg.help.text, punishments.msg.help.click and punishments.msg.help.hover I18N keys");
							return;
						}
						Iterator<String> textIterator = helpText.iterator();
						Iterator<String> clickIterator = helpClick.iterator();
						Iterator<String> hoverIterator = helpHover.iterator();
						Ladder ladder = Ladder.getInstance();
						while (textIterator.hasNext()) {
							String textMessage = textIterator.next();
							String clickMessage = clickIterator.next();
							String hoverMessage = hoverIterator.next();
							RawMessage component = ladder.createRawMessage(textMessage);
							boolean special = false;
							if (!clickMessage.equalsIgnoreCase("NOTHING")) {
								special = true;
								String[] splitter = clickMessage.split(";");
								if (splitter.length != 2) {
									sender.sendMessage("§cUnable to split a ClickEvent (because != 2 / "
											+ splitter.length + "): '" + clickMessage + "'");
									return;
								}
								String clickEventTypeString = splitter[0];
								ClickEventType clickEventType = clickEventTypeString.equalsIgnoreCase("openfile")
										? ClickEventType.OPEN_FILE
										: clickEventTypeString.equalsIgnoreCase("openurl") ? ClickEventType.OPEN_URL
												: clickEventTypeString.equalsIgnoreCase("run")
														? ClickEventType.RUN_COMMAND
														: clickEventTypeString.equalsIgnoreCase("suggest")
																? ClickEventType.SUGGEST_COMMAND : null;
								if (clickEventType == null) {
									sender.sendMessage("§cUnknown ClickEventType: " + clickEventType);
									return;
								}
								component.setClickEvent(clickEventType, false, splitter[1].replace("@0", args[0]));
							}
							if (!hoverMessage.equalsIgnoreCase("NOTHING")) {
								special = true;
								component.setHoverEvent(HoverEventType.SHOW_TEXT, false, hoverMessage);
							}
							if (!special)
								sender.sendMessage(textMessage);
							else {
								if (sender instanceof Player)
									component.send((Player) sender);
								else
									sender.sendMessage(textMessage);
							}
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}.start();
				return;
			}
			String reason = args[1];
			if (!config.contains(reason)) {
				sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.errors.unknownreason"));
				return;
			}
			List<Entry<String, Integer>> list = new ArrayList<>();
			for (String key : config.getSection(reason).getKeys()) {
				Configuration c = config.getSection(reason).getSection(key);
				list.add(new AbstractMap.SimpleEntry<>(c.getString("type"), c.getInt("time")));
			}
			BadblockDatabase.getInstance()
					.addRequest(new Request("SELECT COUNT(*) AS count FROM sanctions WHERE pseudo = '"
							+ BadblockDatabase.getInstance().mysql_real_escape_string(args[0]) + "' AND reason = '"
							+ reason + "'", RequestType.GETTER) {
						@Override
						public void done(ResultSet resultSet) {
							try {
								if (resultSet.next()) {
									int count = resultSet.getInt("count");
									int next = count + 1;
									Entry<String, Integer> entry = list.size() >= next ? list.get(count)
											: list.get(list.size() - 1);
									if (entry == null) {
										sender.sendMessage(
												I18N.getTranslatedMessage("punishments.msg.errors.unknownentry"));
										return;
									}
									List<String> helpText = I18N.getTranslatedListMessages("punishments.msg.help.text");
									List<String> helpClick = I18N
											.getTranslatedListMessages("punishments.msg.help.click");
									List<String> helpHover = I18N
											.getTranslatedListMessages("punishments.msg.help.hover");
									if (helpText.size() != helpClick.size() || helpText.size() != helpHover.size()) {
										sender.sendMessage("§cSynchronization error in the I18N language file: ");
										sender.sendMessage("Length: [Text: " + helpText.size() + "];[Click: "
												+ helpClick.size() + "];[Hover: " + helpHover.size() + "]");
										sender.sendMessage(
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
											shownReason = ChatColor.stripColor(
													ChatColor.translateAlternateColorCodes('&', textMessage));
										}
									}
									if (entry.getKey().equalsIgnoreCase("ban")) {
										if (!sender.hasPermission("ladder.command.ban")) {
											sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.youcantban"));
											return;
										}
										CommandBan.instance.ron(sender, (args[0] + " " + shownReason).split(" "), true);
									}else if (entry.getKey().equalsIgnoreCase("bban")) {
										if (!sender.hasPermission("ladder.command.ban") || !sender.hasPermission("ladder.command.banip")) {
											sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.youcantban"));
											return;
										}
										CommandBan.instance.ron(sender, (args[0] + " " + shownReason).split(" "), true);
										CommandBanip.instance.ron(sender, (args[0] + " " + shownReason).split(" "),
												true);
									} else if (entry.getKey().equalsIgnoreCase("banip")) {
										if (!sender.hasPermission("ladder.command.banip")) {
											sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.youcantban"));
											return;
										}
										CommandBanip.instance.ron(sender, (args[0] + " " + shownReason).split(" "),
												true);
									}else if (entry.getKey().equalsIgnoreCase("kick")) {
										if (!sender.hasPermission("ladder.command.kick")) {
											sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.youcantkick"));
											return;
										}
										CommandKick.instance.ron(sender, (args[0] + " " + shownReason).split(" "),
												true);
									}else if (entry.getKey().equalsIgnoreCase("warn")) {
										if (!sender.hasPermission("ladder.command.warn")) {
											sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.youcantwarn"));
											return;
										}
										CommandWarn.instance.ron(sender, (args[0] + " " + shownReason).split(" "),
										true);
									}else if (entry.getKey().equalsIgnoreCase("mute")) {
										if (!sender.hasPermission("ladder.command.mute")) {
											sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.youcantmute"));
											return;
										}
										CommandMute.instance.ron(sender,
												(args[0] + " " + entry.getValue() + "s " + shownReason).split(" "),
												true);
									}else if (entry.getKey().equalsIgnoreCase("tempban")) {
										if (!sender.hasPermission("ladder.command.tempban")) {
											sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.youcanttempban"));
											return;
										}
										CommandTempban.instance.ron(sender,
												(args[0] + " " + entry.getValue() + "s " + shownReason).split(" "),
												true);
									}else if (entry.getKey().equalsIgnoreCase("tempbanip")) {
										if (!sender.hasPermission("ladder.command.tempbanip")) {
											sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.youcanttempban"));
											return;
										}
										CommandTempbanip.instance.ron(sender,
												(args[0] + " " + entry.getValue() + "s " + shownReason).split(" "),
												true);
									}else if (entry.getKey().equalsIgnoreCase("btempban")) {
										if (!sender.hasPermission("ladder.command.tempban") || !sender.hasPermission("ladder.command.tempbanip")) {
											sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.youcanttempban"));
											return;
										}
										CommandTempban.instance.ron(sender,
												(args[0] + " " + entry.getValue() + "s " + shownReason).split(" "),
												true);
										CommandTempbanip.instance.ron(sender,
												(args[0] + " " + entry.getValue() + "s " + shownReason).split(" "),
												true);
									} else if (entry.getKey().equalsIgnoreCase("unban")) {
										if (!sender.hasPermission("ladder.command.unban")) {
											sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.youcantunban"));
											return;
										}
										CommandUnban.instance.ron(sender, (args[0] + " " + shownReason).split(" "),
												true);
									}else if (entry.getKey().equalsIgnoreCase("bpardon")) {
										if (!sender.hasPermission("ladder.command.unban") || !sender.hasPermission("ladder.command.unbanip")) {
											sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.youcantunban"));
											return;
										}
										CommandUnban.instance.ron(sender, (args[0] + " " + shownReason).split(" "),
												true);
										CommandUnbanip.instance.ron(sender, (args[0] + " " + shownReason).split(" "),
												true);
									} else if (entry.getKey().equalsIgnoreCase("unbanip")) {
										if (!sender.hasPermission("ladder.command.unbanip")) {
											sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.youcantunban"));
											return;
										}
										CommandUnbanip.instance.ron(sender, (args[0] + " " + shownReason).split(" "),
												true);
									}else if (entry.getKey().equalsIgnoreCase("unmute")) {
										if (!sender.hasPermission("ladder.command.unmute")) {
											sender.sendMessage(I18N.getTranslatedMessage("punishments.msg.youcantunmute"));
											return;
										}
										CommandUnmute.instance.ron(sender, (args[0] + " " + shownReason).split(" "),
												true);
									}else {
										sender.sendMessage(I18N
												.getTranslatedMessage("punishments.msg.errors.unknownpunishmenttype"));
									}
								}
							} catch (Exception error) {
								error.printStackTrace();
							}
						}
					});
		}

	}
}
