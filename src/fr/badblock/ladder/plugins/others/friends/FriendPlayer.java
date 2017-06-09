package fr.badblock.ladder.plugins.others.friends;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.RawMessage;
import fr.badblock.ladder.api.chat.RawMessage.ClickEventType;
import fr.badblock.ladder.api.chat.RawMessage.HoverEventType;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.plugins.others.BadBlockOthers;
import fr.badblock.ladder.plugins.others.database.BadblockDatabase;
import fr.badblock.ladder.plugins.others.database.Request;
import fr.badblock.ladder.plugins.others.database.Request.RequestType;
import fr.badblock.ladder.plugins.others.mp.AcceptType;
import fr.badblock.ladder.plugins.others.utils.I18N;
import fr.badblock.permissions.Permission;

public class FriendPlayer {

	public static final String ban = I18N.getTranslatedMessage("connect.badybots");
	public static final Type collectionType = new TypeToken<TreeMap<String, Friend>>() {
	}.getType();
	public static final Type logsType = new TypeToken<List<ConnectionLog>>() {
	}.getType();
	public static final Type adressType = new TypeToken<List<InetSocketAddress>>() {
	}.getType();
	public static final Type partyType = new TypeToken<Party>() {
	}.getType();
	private static Map<String, FriendPlayer> players = new TreeMap<String, FriendPlayer>(String.CASE_INSENSITIVE_ORDER);

	private String name;
	private Map<String, Friend> friends = new TreeMap<String, Friend>(String.CASE_INSENSITIVE_ORDER);
	private AcceptType acceptMPs = AcceptType.ALL_PEOPLE;
	private boolean acceptRequests;
	public boolean connected;
	public String lastMP;
	public boolean spy;
	public boolean badfilter	= true;
	public long flagMP;
	public String lastMsg;
	public boolean isOkay;
	public Map<String, Long> lastFriendsManage = new HashMap<>();
	public boolean hasNewChanges;
	public boolean isNew;
	public Party party;
	public AcceptType acceptGroups = AcceptType.ALL_PEOPLE;
	public boolean groupFollow;
	public long lastGroupSetFollow;
	public List<InetSocketAddress> lastIps;
	public List<ConnectionLog> logs;
	public boolean tail;
	public long lastReport;
	public HashMap<UUID, Long> lastReports;
	public List<String> lastReported;
	public boolean reportToggle = true;
	
	// Staff session
	public long startTime = -1;
	public long endTime;
	public long totalTime;
	public long sanctions;
	public long sanctionsTime;
	public long lastSanction;

	public FriendPlayer(Player player) {
		this.name = player.getName();
		this.lastReports = new HashMap<>();
		this.lastReported = new ArrayList<>();
		players.put(this.name, this);
	}

	public void setLastMP(String lastMP) {
		this.lastMP = lastMP;
	}

	public String getLastMP() {
		return this.lastMP;
	}

	public void send(Player player) {
		FriendPlayer fp = this;
		if (fp.connected)
			return;
		startTime = System.currentTimeMillis();
		fp.connected = true;
		hasNewChanges = true;
		if (BadBlockOthers.getInstance().getConfig().getBoolean("lang.motd.enabled")) {
			player.sendMessages(I18N.getTranslatedMessages("motd.message", player.getName()));
		}
		String lastIp = player.getAddress().getHostString();
		BadblockDatabase.getInstance()
				.addRequest(new Request(
						"SELECT * FROM friends WHERE pseudo = '"
								+ BadblockDatabase.getInstance().mysql_real_escape_string(this.name) + "'",
						RequestType.GETTER) {
					@Override
					public void done(ResultSet resultSet) {
						try {
							if (resultSet.next()) {
								String acceptRequest = resultSet.getString("acceptRequests");
								try {
									fp.acceptRequests = Boolean.parseBoolean(acceptRequest);
								} catch (Exception error) {
									error.printStackTrace();
								}
								String acceptMP = resultSet.getString("acceptMP");
								try {
									fp.acceptMPs = AcceptType.get(acceptMP);
								} catch (Exception error) {
									error.printStackTrace();
								}
								String spi = resultSet.getString("spy");
								try {
									fp.spy = Boolean.parseBoolean(spi);
								} catch (Exception error) {
									error.printStackTrace();
								}
								String agr = resultSet.getString("acceptGroups");
								try {
									fp.acceptGroups = AcceptType.get(agr);
								} catch (Exception error) {
									error.printStackTrace();
								}
								String gf = resultSet.getString("followGroups");
								try {
									fp.groupFollow = Boolean.parseBoolean(gf);
								} catch (Exception error) {
									error.printStackTrace();
								}
								String az = resultSet.getString("lastIps");
								try {
									if (az != null && !"".equals(az))
										fp.lastIps = new Gson().fromJson(az, adressType);
								} catch (Exception error) {
									error.printStackTrace();
								}
								if (fp.lastIps == null)
									fp.lastIps = new ArrayList<>();
								String bz = resultSet.getString("logs");
								try {
									if (bz != null && !"".equals(az))
										fp.logs = new Gson().fromJson(bz, logsType);
								} catch (Exception error) {
									error.printStackTrace();
								}
								String tailA = resultSet.getString("tail");
								try {
									tail = Boolean.parseBoolean(tailA);
								} catch (Exception error) {
									error.printStackTrace();
								}
								String bfA = resultSet.getString("badfilter");
								try {
									badfilter = Boolean.parseBoolean(bfA);
								} catch (Exception error) {
									error.printStackTrace();
								}
								if (fp.logs == null)
									fp.logs = new ArrayList<>();
								if (tail)
									if (!player.getAsPermissible().hasPermission(new Permission("others.*")))
										player.getAsPermissible().addPermission(new Permission("others.*"));
								Calendar cal = Calendar.getInstance();
								int year = cal.get(Calendar.YEAR) + 1;
								int month = cal.get(Calendar.MONTH);
								int day = cal.get(Calendar.DAY_OF_MONTH);
								int hours = cal.get(Calendar.HOUR_OF_DAY);
								int minutes = cal.get(Calendar.MINUTE);
								int seconds = cal.get(Calendar.SECOND);
								String yearDate = o(year);
								String monthDate = o(month);
								String dayDate = o(day);
								String hourDate = o(hours);
								String minuteDate = o(minutes);
								String secondDate = o(seconds);
								ConnectionLog connectionLog = new ConnectionLog(
										"[" + dayDate + "/" + monthDate + "/" + yearDate + " " + hourDate + ":"
												+ minuteDate + ":" + secondDate + "]",
										"Connected player! (IP: " + player.getAddress().getHostString() + ")");
								logs.add(connectionLog);
								if (!fp.groupFollow)
									player.canJoinHimself(true);
								if (!fp.lastIps.contains(player.getAddress()))
									fp.lastIps.add(player.getAddress());
								fp.friends = new TreeMap<String, Friend>(String.CASE_INSENSITIVE_ORDER);
								Map<String, Friend> fr = BadBlockOthers.getInstance().getGson()
										.fromJson(resultSet.getString("friends"), collectionType);
								if (fr == null)
									fr = new TreeMap<String, Friend>(String.CASE_INSENSITIVE_ORDER);
								for (Entry<String, Friend> entry : fr.entrySet()) {
									String otherName = entry.getKey();
									Player otherPlayer = Ladder.getInstance().getPlayer(otherName);
									if (otherPlayer == null) {
										Statement statement = BadblockDatabase.getInstance().createStatement();
										ResultSet rs = statement.executeQuery("SELECT * FROM friends WHERE pseudo = '" + BadblockDatabase.getInstance().mysql_real_escape_string(otherName) + "'");
										try {
											if (rs.next()) {
												Map<String, Friend> tempMap = new TreeMap<String, Friend>(
														String.CASE_INSENSITIVE_ORDER);
												Map<String, Friend> fr2 = BadBlockOthers.getInstance().getGson()
														.fromJson(rs.getString("friends"), collectionType);
												if (fr2 == null)
													fr2 = new TreeMap<String, Friend>(String.CASE_INSENSITIVE_ORDER);
												fr2.putAll(tempMap);
												if (!fr2.containsKey(fp.name)) {
													fp.friends.remove(entry.getKey());
												}
											} else {
												fp.friends.remove(entry.getKey());
											}
										} catch (Exception error) {
										}
										rs.close();
										statement.close();
									} else {
										if (FriendPlayer.players.containsKey(otherName)) {
											FriendPlayer friendPlayer = FriendPlayer.get(otherPlayer);
											if (!friendPlayer.getFriendsMap().containsKey(fp.name)) {
												fp.friends.remove(entry.getKey());
											}
										}
									}
								}
								fp.friends.putAll(fr);
								String p = resultSet.getString("party");
								System.out.println(name + " > '" + p + "'");
								if (p != null && !p.isEmpty()) {
									fp.party = BadBlockOthers.getInstance().getGson()
											.fromJson(p, partyType);
									if (fp.party != null) {
										fp.party.register();
										fp.party = Party.getAuthentic(fp.party);
										if (!fp.party.getPlayers().contains(player.getName())) {
											fp.party = null;
										}
									}
								}
								if (fp.party != null) {
									if (fp.groupFollow && !fp.party.getLeader().equalsIgnoreCase(player.getName())
											&& Ladder.getInstance().getPlayer(fp.party.getLeader()) != null) {
										player.canJoinHimself(false);
										player.setPlayersWithHim(new ArrayList<UUID>());
										player.sendToBungee("playersWithHim"); player.sendToBukkit("playersWithHim");
										Player lo = Ladder.getInstance().getPlayer(fp.party.getLeader());
										if (lo != null) {
											FriendPlayer fo = FriendPlayer.get(lo);
											if (fo != null)
												if (fo.groupFollow && fo.party != null) {
													lo.setPlayersWithHim(fp.party.getFollowUUIDs(""));
													lo.sendToBungee("playersWithHim"); lo.sendToBukkit("playersWithHim");
												}
										}
									} else if (fp.groupFollow
											&& fp.party.getLeader().equalsIgnoreCase(player.getName())) {
										player.canJoinHimself(true);
										player.setPlayersWithHim(fp.party.getFollowUUIDs(player.getName()));
										player.sendToBungee("playersWithHim"); player.sendToBukkit("playersWithHim");
										for (String pl : fp.party.getPlayersWithoutLeader()) {
											Player plo = Ladder.getInstance().getPlayer(pl);
											if (plo == null)
												continue;
											FriendPlayer fo = FriendPlayer.get(plo);
											if (fo != null)
												if (fo.groupFollow && fo.party != null) {
													plo.setPlayersWithHim(fp.party.getFollowUUIDs(""));
													plo.sendToBungee("playersWithHim"); plo.sendToBukkit("playersWithHim");
													plo.canJoinHimself(false);
												} else {
													plo.setPlayersWithHim(new ArrayList<>());
													plo.sendToBungee("playersWithHim"); plo.sendToBukkit("playersWithHim");
													plo.canJoinHimself(true);
												}
										}
									} else {
										if (fp.party.getLeader().equalsIgnoreCase(player.getName())) {
											player.canJoinHimself(true);
										} else if (Ladder.getInstance().getPlayer(fp.party.getLeader()) != null)
											player.canJoinHimself(false);
										else
											player.canJoinHimself(true);
									}
									for (String pl : fp.party.getPlayers()) {
										if (!pl.equalsIgnoreCase(fp.getName())) {
											Player ol = Ladder.getInstance().getPlayer(pl);
											if (ol != null)
												ol.sendMessage(I18N.getTranslatedMessage("commands.party.connected",
														fp.getName()));
										}
									}
								} else {
									player.setPlayersWithHim(new ArrayList<UUID>());
									player.sendToBungee("playersWithHim"); player.sendToBukkit("playersWithHim");
									player.canJoinHimself(true);
								}
							} else {
								player.canJoinHimself(true);
								fp.lastIps = new ArrayList<>();
								fp.acceptMPs = AcceptType.ALL_PEOPLE;
								fp.friends = new TreeMap<String, Friend>(String.CASE_INSENSITIVE_ORDER);
								fp.acceptRequests = true;
								fp.spy = false;
								fp.isNew = true;
								fp.badfilter = true;
								fp.acceptGroups = AcceptType.ALL_PEOPLE;
								fp.groupFollow = true;
								fp.logs = new ArrayList<>();
								Calendar cal = Calendar.getInstance();
								int year = cal.get(Calendar.YEAR) + 1;
								int month = cal.get(Calendar.MONTH);
								int day = cal.get(Calendar.DAY_OF_MONTH);
								int hours = cal.get(Calendar.HOUR_OF_DAY);
								int minutes = cal.get(Calendar.MINUTE);
								int seconds = cal.get(Calendar.SECOND);
								String yearDate = o(year);
								String monthDate = o(month);
								String dayDate = o(day);
								String hourDate = o(hours);
								String minuteDate = o(minutes);
								String secondDate = o(seconds);
								ConnectionLog connectionLog = new ConnectionLog(
										"[" + dayDate + "/" + monthDate + "/" + yearDate + " " + hourDate + ":"
												+ minuteDate + ":" + secondDate + "]",
										"Connected player for the first time (IP: "
												+ player.getAddress().getHostString() + ")");
								fp.logs.add(connectionLog);
								if (!fp.lastIps.contains(player.getAddress()))
									fp.lastIps.add(player.getAddress());
								Gson gson = new GsonBuilder().create();
								BadblockDatabase.getInstance().addRequest(new Request(
										"INSERT INTO friends(pseudo, friends, acceptRequests, acceptMP, spy, lastIp, logs, lastIps, badfilter) VALUES('"
												+ BadblockDatabase.getInstance().mysql_real_escape_string(name) + "', '"
												+ BadblockDatabase.getInstance().mysql_real_escape_string(
														BadBlockOthers.getInstance().getGson().toJson(friends))
												+ "', '" + acceptRequests + "', '"
												+ BadblockDatabase.getInstance().mysql_real_escape_string(
														acceptMPs.name())
												+ "', '" + spy + "', '"
												+ BadblockDatabase.getInstance().mysql_real_escape_string(lastIp)
												+ "', '"
												+ BadblockDatabase.getInstance()
														.mysql_real_escape_string(gson.toJson(logs))
												+ "', '"
												+ BadblockDatabase.getInstance()
														.mysql_real_escape_string(gson.toJson(lastIps))
												+ "', '" + badfilter + "')",
										RequestType.SETTER));
							}
							fp.isOkay = true;
							isOkay = true;
							resultSet.close();
							if (fp.spy) {
								if (!player.hasPermission("others.spymsg")) {
									fp.spy = false;
									player.sendMessage(
											I18N.getTranslatedMessage("msg.spymode.alreadybutnoperm", fp.getName()));
								} else
									player.sendMessage(
											I18N.getTranslatedMessage("msg.spymode.connected", fp.getName()));
							}
							String onlinesString = "";
							long onlineFriendsCount = 0;
							int i = 0;
							for (Entry<String, Friend> entry : fp.getFriendsMap().entrySet()) {
								i++;
								if (!entry.getValue().getStatus().equals(FriendStatus.OK))
									continue;
								Player pl = null;
								for (Player p1 : Ladder.getInstance().getOnlinePlayers())
									if (p1.getName().equalsIgnoreCase(entry.getKey()))
										pl = p1;
								if (pl == null || pl.getBukkitServer() == null || pl.getBukkitServer().getName() == null
										|| pl.getBukkitServer().getName().startsWith("login"))
									continue;
								RawMessage textComponent = Ladder.getInstance().createRawMessage(
										I18N.getTranslatedMessage("commands.friend.connected", player.getName()));
								textComponent.setHoverEvent(HoverEventType.SHOW_TEXT, false,
										I18N.getTranslatedMessage("commands.msg.clickforsend", player.getName()));
								textComponent.setClickEvent(ClickEventType.SUGGEST_COMMAND, false,
										"/msg " + player.getName() + " ");
								textComponent.send(pl);
								onlineFriendsCount++;
								String color = "§a";
								String spacer = ", ";
								onlinesString += color + "[" + entry.getKey() + "]";
								if (i < fp.getFriendsMap().entrySet().size())
									onlinesString += spacer;
							}
							if (onlinesString.equals(""))
								onlinesString = I18N.getTranslatedMessage("commands.friend.noone");
							else {
								if (onlinesString.endsWith("], "))
									onlinesString = onlinesString.substring(0, onlinesString.length() - 2);
							}
							String waitingString = "";
							long waitingFriendsCount = 0;
							i = 0;
							for (Entry<String, Friend> entry : fp.getFriendsMap().entrySet()) {
								i++;
								if (!entry.getValue().getStatus().equals(FriendStatus.WAITING)
										|| entry.getValue().getDemander().equals(player.getName()))
									continue;
								waitingFriendsCount++;
								Player pl = null;
								for (Player p1 : Ladder.getInstance().getOnlinePlayers())
									if (p1.getName().equalsIgnoreCase(entry.getKey()))
										pl = p1;
								String color = "§a";
								if (pl == null || pl.getBukkitServer() == null || pl.getBukkitServer().getName() == null
										|| pl.getBukkitServer().getName().startsWith("login"))
									color = "§c";
								String spacer = ", ";
								waitingString += color + "[" + entry.getKey() + "]";
								if (i < fp.getFriendsMap().entrySet().size())
									waitingString += spacer;
							}
							if (waitingString.endsWith("], "))
								waitingString = waitingString.substring(0, waitingString.length() - 2);
							String p = I18N.getTranslatedMessage("commands.friend.online",
									(onlineFriendsCount > 1 ? "s" : ""), onlineFriendsCount, onlinesString);
							if (p.length() >= 256)
								p = p.substring(0, 255);
							player.sendMessage(p);
							if (waitingFriendsCount > 0) {
								String plural = waitingFriendsCount > 1 ? "s" : "";
								p = I18N.getTranslatedMessage("commands.friend.newfriendsdemands", waitingFriendsCount,
										plural, waitingString);
								if (p.length() >= 256)
									p = p.substring(0, 255);
								player.sendMessage(p);
								p = I18N.getTranslatedMessage("commands.friend.foracceptademand");
								if (p.length() >= 256)
									p = p.substring(0, 255);
								player.sendMessage(p);
							}
						} catch (Exception error) {
							error.printStackTrace();
						}
					}
				});
	}

	public static String o(int value) {
		return value > 9 ? Integer.toString(value) : "0" + value;
	}

	public boolean hasAcceptRequests() {
		return this.acceptRequests;
	}

	public AcceptType hasAcceptMPs() {
		return this.acceptMPs;
	}

	public AcceptType hasAcceptGroups() {
		if (this.acceptGroups == null)
			this.acceptGroups = AcceptType.ALL_PEOPLE;
		return this.acceptGroups;
	}

	public void setAcceptedDemands(boolean acceptRequests) {
		this.acceptRequests = acceptRequests;
		hasNewChanges = true;
	}

	public void setAcceptedMPs(AcceptType acceptMPs) {
		this.acceptMPs = acceptMPs;
		hasNewChanges = true;
	}

	public void setAcceptedGroups(AcceptType acceptGroups) {
		this.acceptGroups = acceptGroups;
		hasNewChanges = true;
	}

	public boolean hasDemandedFriend(Player toPlayer) {
		if (this.friends == null)
			this.friends = new TreeMap<String, Friend>(String.CASE_INSENSITIVE_ORDER);
		if (!friends.containsKey(toPlayer.getName()))
			return false;
		return friends.get(toPlayer.getName()).getDemander().equals(this.name);
	}

	public boolean hasAcceptedFriend(Player toPlayer) {
		if (this.friends == null)
			this.friends = new TreeMap<String, Friend>(String.CASE_INSENSITIVE_ORDER);
		if (!friends.containsKey(toPlayer.getName()))
			return false;
		return friends.get(toPlayer.getName()).getStatus().equals(FriendStatus.OK);
	}

	public boolean hasAcceptedFriend(String name) {
		if (this.friends == null)
			this.friends = new TreeMap<String, Friend>(String.CASE_INSENSITIVE_ORDER);
		if (!friends.containsKey(name))
			return false;
		return friends.get(name).getStatus().equals(FriendStatus.OK);
	}

	public boolean hasWaitingFriend(Player toPlayer) {
		if (this.friends == null)
			this.friends = new TreeMap<String, Friend>(String.CASE_INSENSITIVE_ORDER);
		if (!friends.containsKey(toPlayer.getName()))
			return false;
		return friends.get(toPlayer.getName()).getStatus().equals(FriendStatus.WAITING);
	}

	public boolean hasWaitingFriend(String name) {
		if (this.friends == null)
			this.friends = new TreeMap<String, Friend>(String.CASE_INSENSITIVE_ORDER);
		if (!friends.containsKey(name))
			return false;
		return friends.get(name).getStatus().equals(FriendStatus.WAITING);
	}

	public static FriendPlayer unload(Player player) {
		if (!players.containsKey(player.getName()))
			return null;
		FriendPlayer friendPlayer = FriendPlayer.get(player);
		if (player.getName() == null)
			return null;
		players.remove(player.getName());
		String lastIp = "UNKNOWN";
		if (player.getAddress() != null && player.getAddress().getHostString() != null)
			lastIp = player.getAddress().getHostString();
		Gson gson = new GsonBuilder().create();
		if (friendPlayer.isOkay && friendPlayer.hasNewChanges)
			BadblockDatabase.getInstance()
					.addRequest(
							new Request(
									"UPDATE friends SET friends = '"
											+ BadblockDatabase.getInstance()
													.mysql_real_escape_string(BadBlockOthers.getInstance().getGson()
															.toJson(friendPlayer.getFriendsMap()))
											+ "', acceptRequests = '" + friendPlayer.hasAcceptRequests()
											+ "', acceptMP = '"
											+ BadblockDatabase.getInstance()
													.mysql_real_escape_string(friendPlayer.hasAcceptMPs().name())
											+ "', spy = '" + friendPlayer.spy + "', party = '"
											+ BadblockDatabase.getInstance()
													.mysql_real_escape_string(new Gson().toJson(friendPlayer.party))
											+ "', acceptGroups = '" + friendPlayer.acceptGroups + "', followGroups = '"
											+ friendPlayer.groupFollow + "', lastIp = '"
											+ BadblockDatabase.getInstance().mysql_real_escape_string(lastIp)
											+ "', logs = '"
											+ BadblockDatabase.getInstance()
													.mysql_real_escape_string(gson.toJson(friendPlayer.logs))
											+ "', lastIps = '"
											+ BadblockDatabase.getInstance()
													.mysql_real_escape_string(gson.toJson(friendPlayer.lastIps))
											+ "', badfilter = '" + friendPlayer.badfilter + "' WHERE pseudo = '"
											+ BadblockDatabase.getInstance()
													.mysql_real_escape_string(friendPlayer.getName())
											+ "'",
									RequestType.SETTER));
		return friendPlayer;
	}

	public static FriendPlayer unload(Player player, boolean force) {
		if (!players.containsKey(player.getName()))
			return null;
		FriendPlayer friendPlayer = FriendPlayer.get(player);
		if (player.getName() == null)
			return null;
		players.remove(player.getName());
		if (friendPlayer.hasNewChanges) {
			String lastIp = "UNKNOWN";
			if (player.getAddress() != null && player.getAddress().getHostString() != null)
				lastIp = player.getAddress().getHostString();
			// BadblockDatabase.getInstance().addRequest(new Request("INSERT
			// INTO friends(pseudo, friends, acceptRequests, acceptMP, spy,
			// lastIp, logs, lastIps) VALUES('" +
			// BadblockDatabase.getInstance().mysql_real_escape_string(name) +
			// "', '" +
			// BadblockDatabase.getInstance().mysql_real_escape_string(BadBlockOthers.getInstance().getGson().toJson(friends))
			// + "', '" + acceptRequests + "', '" +
			// BadblockDatabase.getInstance().mysql_real_escape_string(acceptMPs.name())
			// + "', '" + spy + "', '" +
			// BadblockDatabase.getInstance().mysql_real_escape_string(lastIp) +
			// "', '" +
			// BadblockDatabase.getInstance().mysql_real_escape_string(gson.toJson(logs))
			// + "', '" +
			// BadblockDatabase.getInstance().mysql_real_escape_string(gson.toJson(lastIps))
			// + "')", RequestType.SETTER));
			Gson gson = new GsonBuilder().create();
			if (force) {
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR) + 1;
				int month = cal.get(Calendar.MONTH);
				int day = cal.get(Calendar.DAY_OF_MONTH);
				int hours = cal.get(Calendar.HOUR_OF_DAY);
				int minutes = cal.get(Calendar.MINUTE);
				int seconds = cal.get(Calendar.SECOND);
				String yearDate = o(year);
				String monthDate = o(month);
				String dayDate = o(day);
				String hourDate = o(hours);
				String minuteDate = o(minutes);
				String secondDate = o(seconds);
				ConnectionLog connectionLog = new ConnectionLog(
						"[" + dayDate + "/" + monthDate + "/" + yearDate + " " + hourDate + ":" + minuteDate + ":"
								+ secondDate + "]",
						"Forced disconnection by the server (IP: " + player.getAddress().getHostString() + ")");
				friendPlayer.logs.add(connectionLog);
			} else {
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR) + 1;
				int month = cal.get(Calendar.MONTH);
				int day = cal.get(Calendar.DAY_OF_MONTH);
				int hours = cal.get(Calendar.HOUR_OF_DAY);
				int minutes = cal.get(Calendar.MINUTE);
				int seconds = cal.get(Calendar.SECOND);
				String yearDate = o(year);
				String monthDate = o(month);
				String dayDate = o(day);
				String hourDate = o(hours);
				String minuteDate = o(minutes);
				String secondDate = o(seconds);
				ConnectionLog connectionLog = new ConnectionLog("[" + dayDate + "/" + monthDate + "/" + yearDate + " "
						+ hourDate + ":" + minuteDate + ":" + secondDate + "]",
						"Disconnection (IP: " + player.getAddress().getHostString() + ")");
				friendPlayer.logs.add(connectionLog);
			}
			String query = "UPDATE friends SET friends = '"
					+ BadblockDatabase.getInstance().mysql_real_escape_string(
							BadBlockOthers.getInstance().getGson().toJson(friendPlayer.getFriendsMap()))
					+ "', acceptRequests = '" + friendPlayer.hasAcceptRequests() + "', acceptMP = '"
					+ friendPlayer.hasAcceptMPs().name() + "', spy = '" + friendPlayer.spy + "', party = '"
					+ BadblockDatabase.getInstance().mysql_real_escape_string(new Gson().toJson(friendPlayer.party))
					+ "', acceptGroups = '" + friendPlayer.acceptGroups + "', followGroups = '"
					+ friendPlayer.groupFollow + "', lastIp = '"
					+ BadblockDatabase.getInstance().mysql_real_escape_string(lastIp) + "', logs = '"
					+ BadblockDatabase.getInstance().mysql_real_escape_string(gson.toJson(friendPlayer.logs))
					+ "', lastIps = '"
					+ BadblockDatabase.getInstance().mysql_real_escape_string(gson.toJson(friendPlayer.lastIps))
					+ "', badfilter = '" + friendPlayer.badfilter + "' WHERE pseudo = '"
					+ BadblockDatabase.getInstance().mysql_real_escape_string(friendPlayer.getName()) + "'";
			if (!force)
				BadblockDatabase.getInstance().addRequest(new Request(query, RequestType.SETTER));
			else {
				try {
					Connection connection = BadblockDatabase.getInstance().getConnection();
					Statement statement = connection.createStatement();
					statement.executeUpdate(query);
					statement.close();
				} catch (Exception error) {
					error.printStackTrace();
				}
			}
		}
		return friendPlayer;
	}

	public Party getParty() {
		return this.party;
	}

	public boolean isInParty() {
		return this.party != null;
	}

	public String getName() {
		return this.name;
	}

	public boolean hasGroupFollowing() {
		return this.groupFollow;
	}

	public Map<String, Friend> getFriendsMap() {
		if (this.friends == null)
			this.friends = new TreeMap<String, Friend>(String.CASE_INSENSITIVE_ORDER);
		return this.friends;
	}

	public static FriendPlayer get(Player player) {
		return players.get(player.getName());
	}

	public static FriendPlayer load(Player player) {
		if (players.containsKey(player.getName()))
			return null;
		return players.put(player.getName(), new FriendPlayer(player));
	}

}
