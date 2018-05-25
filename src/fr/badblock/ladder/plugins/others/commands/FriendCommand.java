package fr.badblock.ladder.plugins.others.commands;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.RawMessage;
import fr.badblock.ladder.api.chat.RawMessage.ClickEventType;
import fr.badblock.ladder.api.chat.RawMessage.HoverEventType;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.plugins.others.BadBlockOthers;
import fr.badblock.ladder.plugins.others.database.BadblockDatabase;
import fr.badblock.ladder.plugins.others.database.Request;
import fr.badblock.ladder.plugins.others.database.Request.RequestType;
import fr.badblock.ladder.plugins.others.friends.Friend;
import fr.badblock.ladder.plugins.others.friends.FriendPlayer;
import fr.badblock.ladder.plugins.others.friends.FriendStatus;
import fr.badblock.ladder.plugins.others.utils.I18N;
import fr.badblock.permissions.PermissiblePlayer;

public class FriendCommand extends Command {

	public FriendCommand() {
		super("friends", null, "friend", "ami", "amis", "a");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(I18N.getTranslatedMessage("commands.friend.onlyplayers"));
			return;
		}
		Player player = (Player) sender;
		if (args.length == 0) {
			help(player);
			return;
		}
		if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("aide")) {
			help(player);
			return;
		}
		if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")
				|| args[0].equalsIgnoreCase("supprimer")) {
			remove(player, args);
			return;
		}
		if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("ajouter")
				|| args[0].equalsIgnoreCase("invite")) {
			add(player, args);
			return;
		}
		if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off")) {
			state(player, args);
			return;
		}
		if (args[0].equalsIgnoreCase("list")) {
			int page = 1;

			if(args.length > 1)
			{
				try
				{
					page = Integer.parseInt(args[1]);
				}
				catch(Exception e) {}
			}
			
			list(player, page);
			return;
		}
		player.sendMessage(I18N.getTranslatedMessage("commands.friend.unknowncommand"));
	}

	public void clear(Player player) {
		FriendPlayer from = FriendPlayer.get(player);
		if (from == null)
			return;
		for (String f : from.getFriendsMap().keySet())
			player.forceCommand("friends remove " + f);
		player.sendMessage(I18N.getTranslatedMessage("commands.friend.clear"));
		from.hasNewChanges = true;
	}

	public void addall(Player player) {
		final String name = player.getName();
		new Thread() {
			@Override
			public void run() {
				Player prpl = Ladder.getInstance().getPlayer(name);
				if (prpl != null) {
					for (Player player : BadBlockOthers.getInstance().getLadder().getOnlinePlayers()) {
						prpl.forceCommand("friends add " + player.getName());
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}

	public void addall2(Player player) {
		FriendPlayer from = FriendPlayer.get(player);
		if (from == null)
			return;
		from.hasNewChanges = true;
		BadblockDatabase.getInstance().addRequest(new Request("SELECT * FROM friends", RequestType.GETTER, "") {
			@Override
			public void done(ResultSet resultSet) {
				try {
					while (resultSet.next()) {
						try {
							String pseudo = resultSet.getString("pseudo");
							Player pl = Ladder.getInstance().getPlayer(pseudo);
							if (pl == null) {
								if (from.getFriendsMap().size() >= 500) {
									player.sendMessage(I18N.getTranslatedMessage("commands.friend.reachedlimit"));
									return;
								}
								Map<String, Friend> friends = BadBlockOthers.getInstance().getGson()
										.fromJson(resultSet.getString("friends"), FriendPlayer.collectionType);
								// Demande déjà envoyée, réponse donc
								if (friends.containsKey(player.getName())) {
									if (friends.get(player.getName()).getStatus().equals(FriendStatus.WAITING)
											&& friends.get(player.getName()).getDemander()
													.equalsIgnoreCase(player.getName())) {
										player.sendMessage(I18N.getTranslatedMessage("commands.friend.already.wait"));
										continue;
									}
									if (friends.get(player.getName()).getStatus().equals(FriendStatus.OK)) {
										player.sendMessage(I18N.getTranslatedMessage("commands.friend.already.inlist"));
										continue;
									}
									Friend friend = from.getFriendsMap().get(pseudo);
									if (friend == null)
										continue;
									friend.setStatus(FriendStatus.OK);
									friends.get(player.getName()).setStatus(FriendStatus.OK);
									from.lastFriendsManage.put(pseudo, System.currentTimeMillis() + 30_000L);
									from.hasNewChanges = true;
									BadblockDatabase.getInstance().addRequest(
											new Request(
													"UPDATE friends SET friends = '"
															+ BadblockDatabase.getInstance()
																	.mysql_real_escape_string(BadBlockOthers
																			.getInstance().getGson().toJson(friends))
															+ "' WHERE pseudo = '"
															+ BadblockDatabase.getInstance()
																	.mysql_real_escape_string(pseudo)
															+ "'",
													RequestType.SETTER));
									player.sendMessage(I18N.getTranslatedMessage("commands.friend.done", pseudo));
									continue;
								}
								FriendStatus status = FriendStatus.OK;
								if (!player.hasPermission("friends.bypass") && !from.tail) {
									status = FriendStatus.WAITING;
									String acceptRequest = resultSet.getString("acceptRequests");
									boolean acceptRequests = Boolean.parseBoolean(acceptRequest);
									if (!acceptRequests) {
										player.sendMessage(I18N.getTranslatedMessage("friend.set_on_ondemand"));
										continue;
									}
								}
								friends.put(player.getName(), new Friend(player.getName(), pseudo, status));
								from.getFriendsMap().put(pseudo, new Friend(player.getName(), pseudo, status));
								from.lastFriendsManage.put(pseudo, System.currentTimeMillis() + 30_000L);
								from.hasNewChanges = true;
								BadblockDatabase.getInstance().addRequest(new Request(
										"UPDATE friends SET friends = '"
												+ BadblockDatabase.getInstance().mysql_real_escape_string(
														BadBlockOthers.getInstance().getGson().toJson(friends))
												+ "' WHERE pseudo = '"
												+ BadblockDatabase.getInstance().mysql_real_escape_string(pseudo) + "'",
										RequestType.SETTER));
								if (status.equals(FriendStatus.OK)) {
									player.sendMessage(I18N.getTranslatedMessage("commands.friend.done", pseudo));
								} else
									player.sendMessage(I18N.getTranslatedMessage("commands.friend.sended", pseudo));
							}
						} catch (Exception error) {
							error.printStackTrace();
						}
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					player.sendMessage(I18N.getTranslatedMessage("commands.friend.bomberfriendsdone"));
				} catch (Exception error) {
					error.printStackTrace();
				}
			}
		});
	}

	public void help(Player player) {
		player.sendMessages(I18N.getTranslatedMessages("commands.friend.help"));
		/*
		 * player.
		 * sendMessages("§8§l«§b§l-§8§l»§m------§f§8§l«§b§l-§8§l»§b §b§lAmis §8§l«§b§l-§8§l»§m------§f§8§l«§b§l-§8§l»"
		 * , "§c> §6/friends §bhelp §6: liste des commandes",
		 * "§c> §6/friends §badd <pseudo> §6: ajouter/accepter un joueur à ses amis"
		 * ,
		 * "§c> §6/friends §bremove <pseudo> §6: retirer un joueur de ses amis",
		 * "§c> §6/friends §bon/off §6: activer/désactiver les nouvelles demandes d'amis"
		 * , "§c> §6/friends §blist §6: liste de vos amis",
		 * "§8§l«§b§l-§8§l»§m----------------------§f§8§l«§b§l-§8§l»§b");
		 */
	}

	public void add(Player player, String[] args) {
		// Usage: /friends add <pseudo>
		if (args.length != 2) {
			player.sendMessage(I18N.getTranslatedMessage("commands.friend.add.usage"));
			return;
		}
		String pseudo = args[1];
		if (pseudo.equalsIgnoreCase(player.getName())) {
			player.sendMessage(I18N.getTranslatedMessage("commands.friend.himself", pseudo));
			return;
		}
		FriendPlayer from = FriendPlayer.get(player);
		if (from == null)
			return;
		Integer b = player.getPermissionValue("friendsSlots", Integer.class);
		if (b == null) b = 10; // default friendsSlots
		if (!player.hasPermission("others.friendsbypass") && 
			from.getFriendsMap().values().stream().filter(vo -> vo.getStatus().equals(FriendStatus.OK)).count() >= b) {
			player.sendMessage(I18N.getTranslatedMessage("commands.friend.reachedlimit"));
			return;
		}
		if (from.lastFriendsManage.containsKey(pseudo)
				&& from.lastFriendsManage.get(pseudo) > System.currentTimeMillis()) {
			player.sendMessage(I18N.getTranslatedMessage("commands.friend.remove.waitbetweeninteractions"));
			return;
		}
		Player toPlayer = BadBlockOthers.getInstance().getLadder().getPlayer(pseudo);
		if (toPlayer == null) {
			BadblockDatabase.getInstance()
					.addRequest(new Request(
							"SELECT * FROM friends WHERE pseudo = '"
									+ BadblockDatabase.getInstance().mysql_real_escape_string(pseudo) + "'",
							RequestType.GETTER, "") {
						@Override
						public void done(ResultSet resultSet) {
							try {
								if (resultSet.next()) {
									try {
										Map<String, Friend> friends = BadBlockOthers.getInstance().getGson()
												.fromJson(resultSet.getString("friends"), FriendPlayer.collectionType);
										// Demande déjà envoyée, réponse donc
										if (friends.containsKey(player.getName())) {
											if (friends.get(player.getName()).getStatus().equals(FriendStatus.WAITING)
													&& friends.get(player.getName()).getDemander()
															.equalsIgnoreCase(player.getName())) {
												player.sendMessage(
														I18N.getTranslatedMessage("commands.friend.already.wait"));
												return;
											}
											if (friends.get(player.getName()).getStatus().equals(FriendStatus.OK)) {
												player.sendMessage(
														I18N.getTranslatedMessage("commands.friend.already.inlist"));
												return;
											}
											Friend friend = from.getFriendsMap().get(pseudo);
											if (friend == null)
												return;
											friend.setStatus(FriendStatus.OK);
											friends.get(player.getName()).setStatus(FriendStatus.OK);
											from.lastFriendsManage.put(pseudo, System.currentTimeMillis() + 30_000L);
											from.hasNewChanges = true;
											BadblockDatabase.getInstance().addRequest(new Request(
													"UPDATE friends SET friends = '"
															+ BadblockDatabase.getInstance()
																	.mysql_real_escape_string(BadBlockOthers
																			.getInstance().getGson().toJson(friends))
															+ "' WHERE pseudo = '"
															+ BadblockDatabase.getInstance()
																	.mysql_real_escape_string(pseudo)
															+ "'",
													RequestType.SETTER));
											player.sendMessage(
													I18N.getTranslatedMessage("commands.friend.done", pseudo));
											return;
										}
										FriendStatus status = FriendStatus.OK;
										if (!player.hasPermission("friends.bypass") && !from.tail) {
											status = FriendStatus.WAITING;
											String acceptRequest = resultSet.getString("acceptRequests");
											boolean acceptRequests = Boolean.parseBoolean(acceptRequest);
											if (!acceptRequests) {
												player.sendMessage(I18N.getTranslatedMessage("friend.set_on_ondemand"));
												return;
											}
										}
										friends.put(player.getName(), new Friend(player.getName(), pseudo, status));
										from.getFriendsMap().put(pseudo, new Friend(player.getName(), pseudo, status));
										from.lastFriendsManage.put(pseudo, System.currentTimeMillis() + 30_000L);
										from.hasNewChanges = true;
										BadblockDatabase.getInstance()
												.addRequest(
														new Request(
																"UPDATE friends SET friends = '"
																		+ BadblockDatabase.getInstance()
																				.mysql_real_escape_string(BadBlockOthers
																						.getInstance().getGson()
																						.toJson(friends))
																		+ "' WHERE pseudo = '"
																		+ BadblockDatabase.getInstance()
																				.mysql_real_escape_string(pseudo)
																		+ "'",
																RequestType.SETTER));
										if (status.equals(FriendStatus.WAITING))
											player.sendMessage(
													I18N.getTranslatedMessage("commands.friend.sended", pseudo));
										else
											player.sendMessage(
													I18N.getTranslatedMessage("commands.friend.done", pseudo));
									} catch (Exception error) {
										error.printStackTrace();
									}
								} else {
									player.sendMessage(
											I18N.getTranslatedMessage("commands.friend.unknownplayer", pseudo));
								}
							} catch (Exception error) {
								error.printStackTrace();
							}
						}
					});
			return;
		}
		FriendPlayer to = FriendPlayer.get(toPlayer);
		if (to == null)
			return;
		// Acceptation à une demande d'ami
		if (to.hasDemandedFriend(player)) {
			if (from.hasAcceptedFriend(toPlayer)) {
				player.sendMessage(I18N.getTranslatedMessage("commands.friend.already.inlist", pseudo));
				return;
			}
			Friend friend = to.getFriendsMap().get(player.getName());
			friend.setStatus(FriendStatus.OK);
			if (from.getFriendsMap().get(pseudo) == null) {
				from.getFriendsMap().put(to.getName(), friend);
			} else {
				from.getFriendsMap().get(to.getName()).setStatus(FriendStatus.OK);
			}
			from.hasNewChanges = true;
			to.hasNewChanges = true;
			player.sendMessage(I18N.getTranslatedMessage("commands.friend.done", toPlayer.getName()));
			toPlayer.sendMessage(I18N.getTranslatedMessage("commands.friend.done", player.getName()));
			from.lastFriendsManage.put(pseudo, System.currentTimeMillis() + 30_000L);
			return;
		}
		if (from.hasAcceptedFriend(toPlayer)) {
			player.sendMessage(I18N.getTranslatedMessage("commands.friend.already.inlist"));
			return;
		}
		if (from.hasWaitingFriend(toPlayer)) {
			player.sendMessage(I18N.getTranslatedMessage("commands.friend.already.wait"));
			return;
		}
		FriendStatus status = FriendStatus.OK;
		if (!player.hasPermission("friends.bypass") && !from.tail) {
			status = FriendStatus.WAITING;
			if (!to.hasAcceptRequests()) {
				player.sendMessage(I18N.getTranslatedMessage("friend.set_on_ondemand"));
				return;
			}
		}
		to.getFriendsMap().put(player.getName(), new Friend(player.getName(), to.getName(), status));
		from.getFriendsMap().put(toPlayer.getName(), new Friend(player.getName(), to.getName(), status));
		from.hasNewChanges = true;
		to.hasNewChanges = true;
		if (status.equals(FriendStatus.WAITING)) {
			new Thread() {
				@Override
				public void run() {
					String[] strings = I18N.getTranslatedMessages("commands.friend.received", player.getName());
					for (String string : strings) {
						String message = "";
						boolean o = false;
						boolean raw = false;
						RawMessage rawMessage = null;
						for (Character character : string.toCharArray()) {
							String stringChar = character.toString();
							if (stringChar.equals("@")) {
								o = true;
							} else {
								if (o) {
									if (stringChar.equals("1") || stringChar.equals("2")) {
										rawMessage = Ladder.getInstance().createRawMessage(message);
										if (stringChar.equals("1")) {
											raw = true;
											RawMessage addedMessage = Ladder.getInstance().createRawMessage(I18N
													.getTranslatedMessage("commands.friend.accept", player.getName()));
											addedMessage.setClickEvent(ClickEventType.RUN_COMMAND, false,
													"/friends add " + player.getName());
											addedMessage.setHoverEvent(HoverEventType.SHOW_TEXT, false,
													I18N.getTranslatedMessage("commands.friend.accepthover",
															player.getName()));
											rawMessage.add(addedMessage);
										} else if (stringChar.equals("2")) {
											raw = true;
											RawMessage addedMessage = Ladder.getInstance().createRawMessage(I18N
													.getTranslatedMessage("commands.friend.refuse", player.getName()));
											addedMessage.setClickEvent(ClickEventType.RUN_COMMAND, false,
													"/friends remove " + player.getName());
											addedMessage.setHoverEvent(HoverEventType.SHOW_TEXT, false,
													I18N.getTranslatedMessage("commands.friend.refusedhover",
															player.getName()));
											rawMessage.add(addedMessage);
										}
										break;
									} else
										message += stringChar;
									o = false;
								} else
									message += stringChar;
							}
						}
						if (rawMessage == null) {
							rawMessage = Ladder.getInstance().createRawMessage(message);
						}
						int i = 0;
						String msg = "";
						boolean ok = false;
						for (Character character : string.toCharArray()) {
							i++;
							if (i < message.length() + 4)
								continue;
							String stringChar = character.toString();
							if (stringChar.equals("@")) {
								o = true;
							} else {
								if (o) {
									if (stringChar.equals("1") || stringChar.equals("2")) {
										if (stringChar.equals("1")) {
											raw = true;
											ok = true;
											RawMessage addedMessage = Ladder.getInstance().createRawMessage(msg + I18N
													.getTranslatedMessage("commands.friend.accept", player.getName()));
											addedMessage.setClickEvent(ClickEventType.RUN_COMMAND, false,
													"/friends add " + player.getName());
											addedMessage.setHoverEvent(HoverEventType.SHOW_TEXT, false,
													I18N.getTranslatedMessage("commands.friend.accepthover",
															player.getName()));
											rawMessage.add(addedMessage);
										} else if (stringChar.equals("2")) {
											raw = true;
											RawMessage addedMessage = Ladder.getInstance().createRawMessage(msg + I18N
													.getTranslatedMessage("commands.friend.refuse", player.getName()));
											addedMessage.setClickEvent(ClickEventType.RUN_COMMAND, false,
													"/friends remove " + player.getName());
											addedMessage.setHoverEvent(HoverEventType.SHOW_TEXT, false,
													I18N.getTranslatedMessage("commands.friend.refusedhover",
															player.getName()));
											rawMessage.add(addedMessage);
										}
										break;
									} else
										msg += stringChar;
									o = false;
								} else
									msg += stringChar;
							}
						}
						if (!ok && !msg.equals("")) {
							RawMessage addedMessage = Ladder.getInstance().createRawMessage(msg);
							rawMessage.add(addedMessage);
						}
						if (!raw)
							toPlayer.sendMessage(string);
						else
							rawMessage.send(toPlayer);
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			player.sendMessage(I18N.getTranslatedMessage("commands.friend.sended", toPlayer.getName()));
			/*
			 * String message = ""; RawMessage generalRawMessage =
			 * Ladder.getInstance().createRawMessage(""); RawMessage rawMessage
			 * =
			 * Ladder.getInstance().createRawMessage(I18N.getTranslatedMessage(
			 * "commands.friend.accept", player.getName()) + " - ");
			 * rawMessage.setClickEvent(ClickEventType.RUN_COMMAND, false,
			 * "/friends add " + player.getName());
			 * rawMessage.setHoverEvent(HoverEventType.SHOW_TEXT, false,
			 * I18N.getTranslatedMessage("commands.friend.accepthover",
			 * player.getName())); generalRawMessage.add(rawMessage); RawMessage
			 * rawReject =
			 * Ladder.getInstance().createRawMessage(I18N.getTranslatedMessage(
			 * "commands.friend.refuse", player.getName()));
			 * rawReject.setClickEvent(ClickEventType.RUN_COMMAND, false,
			 * "/friends remove " + player.getName());
			 * rawReject.setHoverEvent(HoverEventType.SHOW_TEXT, false,
			 * I18N.getTranslatedMessage("commands.friend.refusedhover",
			 * player.getName())); generalRawMessage.add(spacerComponent);
			 * generalRawMessage.add(rawReject);
			 * from.lastFriendsManage.put(pseudo, System.currentTimeMillis() +
			 * 30_000L);
			 */
		} else {
			from.lastFriendsManage.put(pseudo, System.currentTimeMillis() + 30_000L);
			player.sendMessage(I18N.getTranslatedMessage("commands.friend.done", toPlayer.getName()));
			toPlayer.sendMessage(I18N.getTranslatedMessage("commands.friend.done", player.getName()));
		}
	}

	public void remove(Player player, String[] args) {
		if (args.length != 2) {
			player.sendMessage(I18N.getTranslatedMessage("commands.friend.remove.usage", player.getName()));
			return;
		}
		String pseudo = args[1];
		if (pseudo.equalsIgnoreCase(player.getName())) {
			player.sendMessage(I18N.getTranslatedMessage("commands.friend.himself", player.getName()));
			return;
		}
		FriendPlayer from = FriendPlayer.get(player);
		if (from == null)
			return;
		if (from.lastFriendsManage.containsKey(pseudo)
				&& from.lastFriendsManage.get(pseudo) > System.currentTimeMillis()) {
			player.sendMessage(
					I18N.getTranslatedMessage("commands.friend.remove.waitbetweeninteractions", player.getName()));
			return;
		}
		Player toPlayer = BadBlockOthers.getInstance().getLadder().getPlayer(pseudo);
		if (toPlayer == null) {
			if (from.hasWaitingFriend(pseudo)) {
				BadblockDatabase.getInstance()
						.addRequest(new Request(
								"SELECT * FROM friends WHERE pseudo = '"
										+ BadblockDatabase.getInstance().mysql_real_escape_string(pseudo) + "'",
								RequestType.GETTER, "") {
							@Override
							public void done(ResultSet resultSet) {
								try {
									if (resultSet.next()) {
										if (!resultSet.getString("bypass").equals("true")) {
											try {
												Map<String, Friend> friends = BadBlockOthers.getInstance().getGson()
														.fromJson(resultSet.getString("friends"),
																FriendPlayer.collectionType);
												if (!friends.containsKey(player.getName())) {
													player.sendMessage(I18N.getTranslatedMessage("commands.friend.nope",
															player.getName()));
													return;
												}
												Friend friend = from.getFriendsMap().get(pseudo);
												if (friend == null)
													return;
												friends.remove(player.getName());
												from.getFriendsMap().remove(pseudo);
												from.lastFriendsManage.put(pseudo,
														System.currentTimeMillis() + 30_000L);
												from.hasNewChanges = true;
												BadblockDatabase.getInstance()
														.addRequest(new Request(
																"UPDATE friends SET friends = '"
																		+ BadblockDatabase
																				.getInstance()
																				.mysql_real_escape_string(BadBlockOthers
																						.getInstance().getGson()
																						.toJson(friends))
																		+ "' WHERE pseudo = '"
																		+ BadblockDatabase.getInstance()
																				.mysql_real_escape_string(pseudo)
																		+ "'",
																RequestType.SETTER));
												if (friend.getDemander().equalsIgnoreCase(player.getName()))
													player.sendMessage(I18N
															.getTranslatedMessage("commands.friend.cancelled", pseudo));
												else
													player.sendMessage(I18N
															.getTranslatedMessage("commands.friend.refused", pseudo));
											} catch (Exception error) {
												error.printStackTrace();
											}
										} else {
											player.sendMessage(I18N.getTranslatedMessage("friend.remove.unknownerror"));
											return;
										}
									} else {
										player.sendMessage(
												I18N.getTranslatedMessage("commands.friend.unknownplayer", pseudo));
									}
								} catch (Exception error) {
									error.printStackTrace();
								}
							}
						});
				return;
			}
			if (!from.hasAcceptedFriend(pseudo)) {
				player.sendMessage(I18N.getTranslatedMessage("commands.friend.nowith", pseudo));
				return;
			}
			BadblockDatabase.getInstance()
					.addRequest(new Request(
							"SELECT * FROM friends WHERE pseudo = '"
									+ BadblockDatabase.getInstance().mysql_real_escape_string(pseudo) + "'",
							RequestType.GETTER, "") {
						@Override
						public void done(ResultSet resultSet) {
							try {
								if (resultSet.next()) {
									if (!resultSet.getString("bypass").equals("true")) {
										try {
											Map<String, Friend> friends = BadBlockOthers.getInstance().getGson()
													.fromJson(resultSet.getString("friends"),
															FriendPlayer.collectionType);
											if (!friends.containsKey(player.getName())) {
												player.sendMessage(
														I18N.getTranslatedMessage("commands.friend.nope", pseudo));
												return;
											}
											friends.remove(player.getName());
											from.getFriendsMap().remove(pseudo);
											from.lastFriendsManage.put(pseudo, System.currentTimeMillis() + 30_000L);
											from.hasNewChanges = true;
											BadblockDatabase.getInstance().addRequest(new Request(
													"UPDATE friends SET friends = '"
															+ BadblockDatabase.getInstance()
																	.mysql_real_escape_string(BadBlockOthers
																			.getInstance().getGson().toJson(friends))
															+ "' WHERE pseudo = '"
															+ BadblockDatabase.getInstance()
																	.mysql_real_escape_string(pseudo)
															+ "'",
													RequestType.SETTER));
											player.sendMessage(I18N.getTranslatedMessage(
													"commands.friend.removedfromyourlist", pseudo));
										} catch (Exception error) {
											error.printStackTrace();
										}
									} else {
										player.sendMessage(I18N.getTranslatedMessage("friend.remove.unknownerror"));
										return;
									}
								} else {
									player.sendMessage(
											I18N.getTranslatedMessage("commands.friend.unknownplayer", pseudo));
								}
							} catch (Exception error) {
								error.printStackTrace();
							}
						}
					});
			return;
		}
		FriendPlayer to = FriendPlayer.get(toPlayer);
		if (to == null)
			return;
		if (from.hasWaitingFriend(toPlayer)) {
			Friend friend = from.getFriendsMap().get(pseudo);
			if (friend == null)
				return;
			if (toPlayer.hasPermission("friends.bypass") || from.tail) {
				player.sendMessage(I18N.getTranslatedMessage("friend.remove.unknownerror"));
				return;
			}
			to.getFriendsMap().remove(player.getName());
			from.getFriendsMap().remove(pseudo);
			from.lastFriendsManage.put(pseudo, System.currentTimeMillis() + 30_000L);
			from.hasNewChanges = true;
			if (friend.getDemander().equalsIgnoreCase(player.getName()))
				player.sendMessage(I18N.getTranslatedMessage("commands.friend.cancelled", pseudo));
			else
				player.sendMessage(I18N.getTranslatedMessage("commands.friend.refused", pseudo));
			return;
		}
		if (!from.hasAcceptedFriend(toPlayer)) {
			player.sendMessage(I18N.getTranslatedMessage("commands.friend.nowith", pseudo));
			return;
		}
		if (toPlayer.hasPermission("friends.bypass") || from.tail) {
			player.sendMessage(I18N.getTranslatedMessage("friend.remove.unknownerror"));
			return;
		}
		to.getFriendsMap().remove(player.getName());
		from.getFriendsMap().remove(to.getName());
		to.hasNewChanges = true;
		from.hasNewChanges = true;
		from.lastFriendsManage.put(pseudo, System.currentTimeMillis() + 30_000L);
		player.sendMessage(I18N.getTranslatedMessage("commands.friend.removedfromyourlist", toPlayer.getName()));
		toPlayer.sendMessage(I18N.getTranslatedMessage("commands.friend.removedfromyourlist", player.getName()));
	}

	public void state(Player player, String[] args) {
		FriendPlayer from = FriendPlayer.get(player);
		if (from == null)
			return;
		if (args[0].equalsIgnoreCase("on")) {
			if (from.hasAcceptRequests()) {
				player.sendMessage(I18N.getTranslatedMessage("friend.set_on_already"));
				return;
			}
			from.setAcceptedDemands(true);
			player.sendMessage(I18N.getTranslatedMessage("friend.set_on_done"));
		} else if (args[0].equalsIgnoreCase("off")) {
			if (!from.hasAcceptRequests()) {
				player.sendMessage(I18N.getTranslatedMessage("friend.set_off_already"));
				return;
			}
			from.setAcceptedDemands(false);
			player.sendMessage(I18N.getTranslatedMessage("friend.set_off_done"));
		} else {

		}
	}

	public void list(Player player, int page) {
		FriendPlayer from = FriendPlayer.get(player);
		
		if (from == null)
			return;

		List<RawMessage> messages = new ArrayList<RawMessage>();

		Set<Entry<String, Friend>> onlineFriends = from.getFriendsMap().entrySet().parallelStream().filter(entry -> entry.getValue().getStatus().equals(FriendStatus.OK)).collect(Collectors.toSet());
		for(Entry<String, Friend> entry : onlineFriends)
		{
			RawMessage message = createRawMessage(entry);
			
			if(message != null)
				messages.add(message);
		}
		
		Set<Entry<String, Friend>> waitingFriends = from.getFriendsMap().entrySet().parallelStream().filter(entry -> entry.getValue().getStatus().equals(FriendStatus.WAITING)).collect(Collectors.toSet());
		for(Entry<String, Friend> entry : waitingFriends)
		{
			RawMessage message = createRawMessageWaiting(entry);
			
			if(message != null)
				messages.add(message);
		}
		
		
		if (messages.isEmpty())
		{
			player.sendMessage(I18N.getTranslatedMessage("commands.friend.noone2"));
		}
		else
		{
			int indexStart = Math.max(0, (page - 1) * 10);
			int indexStop = Math.min(messages.size(), page * 10);
			
			player.sendMessage(I18N.getTranslatedMessage("commands.friend.list.prefix", page, messages.size() / 10 + (messages.size() % 10 == 0 ? 0 : 1)));
			
			for(int i = indexStart; i < indexStop; i++)
				messages.get(i).send(player);
			
			player.sendMessage(I18N.getTranslatedMessage("commands.friend.list.suffix"));
		}
	}
	
	private RawMessage createRawMessage(Entry<String, Friend> entry)
	{
		Ladder ladder = Ladder.getInstance();
		String name = entry.getKey();
		
		OfflinePlayer other = ladder.getOfflinePlayer(name);
		
		if(!other.hasPlayed()) //WTF
			return null;
		
		boolean connected = other instanceof Player;
		
		RawMessage prefix = null, sendMessage = null, invite = null, remove = null;
		
		if(connected)
		{
			prefix = ladder.createRawMessage("&7[&aC&7] ").setHoverEvent(HoverEventType.SHOW_TEXT, true, "&7Ce joueur est &aconnecté &7!");
			sendMessage = ladder.createRawMessage("&7[&a✉&7] ")
				.setHoverEvent(HoverEventType.SHOW_TEXT, true, "&7Envoyer un message à &b" + name)
				.setClickEvent(ClickEventType.SUGGEST_COMMAND, false, "/msg " + name);
			
			invite = ladder.createRawMessage("&7[&aGroupe&7] ")
				.setHoverEvent(HoverEventType.SHOW_TEXT, true, "&7Invite &b" + entry.getKey() + " &7dans votre groupe.")
				.setClickEvent(ClickEventType.RUN_COMMAND, false, "/groupe invite " + name);
		}
		else
		{
			prefix = ladder.createRawMessage("&7[&cD&7] ").setHoverEvent(HoverEventType.SHOW_TEXT, true, "&7Ce joueur est &cdéconnecté &7!");
			sendMessage = ladder.createRawMessage("&7[&c✉&7] ").setHoverEvent(HoverEventType.SHOW_TEXT, true, "&cCe joueur n'est pas connecté");
			invite = ladder.createRawMessage("&7[&cGroupe&7] ").setHoverEvent(HoverEventType.SHOW_TEXT, true, "&cCe joueur n'est pas connecté");
		}
		
		remove = ladder.createRawMessage("&7[&c✘&7]")
				.setHoverEvent(HoverEventType.SHOW_TEXT, true, "&7Supprime &b" + entry.getKey() + " &7de vos amis.")
				.setClickEvent(ClickEventType.SUGGEST_COMMAND, false, "/friends remove " + name);

		PermissiblePlayer permissiblePlayer = (PermissiblePlayer) other.getAsPermissible();
		
		RawMessage username = ladder.createRawMessage( "&7" + entry.getKey() + " " )
				.setHoverEvent(HoverEventType.SHOW_TEXT, true,
						permissiblePlayer.getDisplayName(),
						"&7Badcoins: &b" + getBadcoins(other),
						"&7Level: &b" + getLevel(other));
		
		return prefix.addAll(username, sendMessage, invite, remove);
	}
	
	private RawMessage createRawMessageWaiting(Entry<String, Friend> entry)
	{
		Ladder ladder = Ladder.getInstance();
		String name = entry.getKey();
		
		OfflinePlayer other = ladder.getOfflinePlayer(name);
		
		if(!other.hasPlayed()) //WTF
			return null;
		
		RawMessage prefix = null, accept = null, remove = null;

		prefix = ladder.createRawMessage("&7[?] ").setHoverEvent(HoverEventType.SHOW_TEXT, true, "&7Pour voir le statut de ce joueur, acceptez sa demande d'ami.");

		accept = ladder.createRawMessage("&7[&a✔&7] ")
				.setHoverEvent(HoverEventType.SHOW_TEXT, true, "&7Accepter l'invitation de &b" + entry.getKey() + "&7.")
				.setClickEvent(ClickEventType.RUN_COMMAND, false, "/friends add " + name);
		
		remove = ladder.createRawMessage("&7[&c✘&7]")
				.setHoverEvent(HoverEventType.SHOW_TEXT, true, "&7Refuse l'invitation de &b" + entry.getKey() + "&7.")
				.setClickEvent(ClickEventType.RUN_COMMAND, false, "/friends remove " + name);

		RawMessage username = ladder.createRawMessage( "&7" + entry.getKey() + " " );
		
		return prefix.addAll(username, accept, remove);
	}

	private int getBadcoins(OfflinePlayer player)
	{
		if(!player.getData().has("game"))
			return 0;
		
		JsonObject game = player.getData().get("game").getAsJsonObject();
		return game.has("badcoins") ? game.get("badcoins").getAsInt() : 0;
	}
	
	private int getLevel(OfflinePlayer player)
	{
		if(!player.getData().has("game"))
			return 1;
		
		JsonObject game = player.getData().get("game").getAsJsonObject();
		return game.has("level") ? game.get("level").getAsInt() : 1;
	}
}
