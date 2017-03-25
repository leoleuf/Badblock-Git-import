package fr.badblock.ladder.plugins.others.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.chat.RawMessage;
import fr.badblock.ladder.api.chat.RawMessage.ClickEventType;
import fr.badblock.ladder.api.chat.RawMessage.HoverEventType;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.config.Configuration;
import fr.badblock.ladder.api.config.ConfigurationProvider;
import fr.badblock.ladder.api.config.YamlConfiguration;
import fr.badblock.ladder.api.entities.Bukkit;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.plugins.others.BadBlockOthers;
import fr.badblock.ladder.plugins.others.utils.I18N;

public class AnimCommand extends Command {

	public Timer timer;
	public boolean accessible;
	public boolean enabled;
	public long timeBetweenMessages;
	public String eventName;
	public TimerTask timerTask;
	public List<String> lobbies;

	public AnimCommand() {
		super("animation", null, "anim");
		timer = new Timer();

		BadBlockOthers instance = BadBlockOthers.getInstance();
		Configuration config = instance.configuration;
		if (!config.contains("animation.lobbies")) {
			config.set("animation.lobbies", new ArrayList<>());
			lobbies = new ArrayList<>();
			try {
				ConfigurationProvider.getProvider(YamlConfiguration.class)
						.save(BadBlockOthers.getInstance().configuration, BadBlockOthers.getInstance().configFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			lobbies = config.getStringList("animation.lobbies");
			if (lobbies == null)
				lobbies = new ArrayList<>();
		}
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		/**
		 * 
		 * Changer le status du serveur d'animation anim state - Voir le status
		 * du serveur d'animation (fermé/ouvert au public) anim state on -
		 * Activer le serveur d'animation anim state off - Désactiver le serveur
		 * d'animation (ne kick pas les joueurs) anim state kickall - Kicker les
		 * joueurs
		 * 
		 * Créer un message répétitif
		 * 
		 * anim alert - Voir l'alerte en cours et les détails sur celle-ci anim
		 * alert set <timeBetweenMessages> <eventName> anim alert stop
		 * 
		 * Ajouter un lobby
		 * 
		 * anim lobby add <server> anim lobby remove <server> anim lobby list
		 * 
		 */
		if (!(sender instanceof Player)) {
			sender.sendMessages(I18N.getTranslatedMessages("commands.anim.onlyforplayers"));
			return;
		}
		Ladder ladder = Ladder.getInstance();
		Player player = (Player) sender;
		if (args.length == 1 && args[0].equalsIgnoreCase("tp")) {
			if (!accessible && !sender.hasPermission("animation.bypass")) {
				player.sendMessages(I18N.getTranslatedMessages("commands.anim.closed"));
				return;
			}
			Bukkit server = null;
			for (String serverName : lobbies) {
				Bukkit bukkit = ladder.getBukkitServer(serverName);
				if (bukkit != null) {
					if (bukkit.getPlayers() != null) {
						if (server == null || server.getPlayers().size() > bukkit.getPlayers().size())
							server = bukkit;
					}
				}
			}
			if (server == null) {
				player.sendMessages(I18N.getTranslatedMessages("commands.anim.nolobbyopened_anim"));
				return;
			}
			if (player.getBukkitServer() == server) {
				player.sendMessages(I18N.getTranslatedMessages("commands.anim.alreadyconnected"));
				return;
			}
			player.connect(server);
			player.sendMessages(I18N.getTranslatedMessages("commands.anim.teleportedto", server.getName()));
			return;
		}
		if (!sender.hasPermission("animation.command")) {
			player.sendMessage(I18N.getTranslatedMessage("commands.permission"));
			return;
		}
		if (args.length == 0 || (args.length > 0 && args[0].equalsIgnoreCase("help"))) {
			if (!sender.hasPermission("animation.command.help")) {
				player.sendMessage(I18N.getTranslatedMessage("commands.permission"));
				return;
			}
			player.sendMessages(I18N.getTranslatedMessages("commands.anim.usage_general"));
		} else {
			if (args.length >= 1 && args[0].equalsIgnoreCase("state")) {
				if (!sender.hasPermission("animation.command.state")) {
					player.sendMessage(I18N.getTranslatedMessage("commands.permission"));
					return;
				}
				if (args.length > 1 && args[1].equalsIgnoreCase("on")) {
					if (!sender.hasPermission("animation.command.state.on")) {
						player.sendMessage(I18N.getTranslatedMessage("commands.permission"));
						return;
					}
					if (accessible) {
						player.sendMessages(I18N.getTranslatedMessages("commands.anim.serveralready_on"));
						return;
					}
					accessible = true;
					player.sendMessages(I18N.getTranslatedMessages("commands.anim.done_on"));
					for (Player p : ladder.getOnlinePlayers()) {
						if (p.hasPermission("ladder.command.chatstaff"))
							p.sendMessages(I18N.getTranslatedMessages("commands.anim.chatstaff_on", player.getName()));
					}
				} else if (args.length > 1 && args[1].equalsIgnoreCase("off")) {
					if (!sender.hasPermission("animation.command.state.off")) {
						player.sendMessage(I18N.getTranslatedMessage("commands.permission"));
						return;
					}
					if (!accessible) {
						player.sendMessages(I18N.getTranslatedMessages("commands.anim.serveralready_off"));
						return;
					}
					accessible = false;
					enabled = false;
					timeBetweenMessages = -1;
					eventName = null;
					if (timerTask != null) {
						timerTask.cancel();
						timerTask = null;
					}
					player.sendMessages(I18N.getTranslatedMessages("commands.anim.done_off"));
					for (Player p : ladder.getOnlinePlayers()) {
						if (p.hasPermission("ladder.command.chatstaff"))
							p.sendMessages(I18N.getTranslatedMessages("commands.anim.chatstaff_off", player.getName()));
					}
				} else if (args.length > 1 && args[1].equalsIgnoreCase("kickall")) {
					if (!sender.hasPermission("animation.command.state.kickall")) {
						player.sendMessage(I18N.getTranslatedMessage("commands.permission"));
						return;
					}
					int i = 0;
					for (Bukkit server : ladder.getBukkitServers()) {
						if (!server.getName().startsWith("anim"))
							continue;
						if (server.getPlayers() == null)
							continue;
						for (UUID uuid : server.getPlayers()) {
							Player pl = ladder.getPlayer(uuid);
							if (pl == null)
								continue;
							i++;
							Bukkit lobbyServer = ladder.getBukkitServer("lobby");
							if (lobbyServer != null) {
								pl.connect(lobbyServer);
							} else
								pl.disconnect(I18N.getTranslatedMessages("commands.anim.nolobbyopened_normal"));
						}
					}
					String plural = i > 1 ? "s" : "";
					player.sendMessages(I18N.getTranslatedMessages("commands.anim.kicked", i, plural));
				} else if (args.length > 1) {
					player.sendMessages(I18N.getTranslatedMessages("commands.anim.invalidargument"));
					return;
				} else {
					String[] status = accessible ? I18N.getTranslatedMessages("commands.anim.online")
							: I18N.getTranslatedMessages("commands.anim.offline");
					player.sendMessages(I18N.getTranslatedMessages("commands.anim.status", status[0]));
					int i = 0;
					for (Bukkit server : ladder.getBukkitServers()) {
						if (!server.getName().startsWith("anim"))
							continue;
						if (server.getPlayers() == null)
							continue;
						for (UUID uuid : server.getPlayers()) {
							Player pl = ladder.getPlayer(uuid);
							if (pl == null)
								continue;
							i++;
						}
					}
					String plural = i > 1 ? "s" : "";
					player.sendMessages(I18N.getTranslatedMessages("commands.anim.connectedplayers", plural, i));
					player.sendMessages(I18N.getTranslatedMessages("commands.anim.alternativescommand"));
				}
			} else if (args.length >= 1 && args[0].equalsIgnoreCase("alert")) {
				if (!sender.hasPermission("animation.command.alert")) {
					player.sendMessage(I18N.getTranslatedMessage("commands.permission"));
					return;
				}
				if (args.length > 1 && args[1].equalsIgnoreCase("set")) {
					if (!sender.hasPermission("animation.command.alert.set")) {
						player.sendMessage(I18N.getTranslatedMessage("commands.permission"));
						return;
					}
					if (!accessible) {
						player.sendMessages(I18N.getTranslatedMessages("commands.anim.alerterror"));
						return;
					}
					if (args.length < 4) {
						player.sendMessages(I18N.getTranslatedMessages("commands.anim.usage_alert"));
						return;
					}
					String[] okayMessage = I18N.getTranslatedMessages("commands.anim.anim_edited");
					if (!enabled)
						okayMessage = I18N.getTranslatedMessages("commands.anim.anim_activated");
					enabled = true;
					try {
						long timeBetweenMessages = Long.parseLong(args[2]);
						if (timeBetweenMessages <= 0) {
							player.sendMessages(I18N.getTranslatedMessages("commands.anim.minvalue"));
							return;
						}
						String eventName = "";
						int i = -1;
						for (String character : args) {
							i++;
							if (i <= 2)
								continue;
							eventName += character;
							if (i < args.length - 1)
								eventName += "";
						}
						this.eventName = ChatColor.translateAlternateColorCodes('&', eventName);
						this.timeBetweenMessages = timeBetweenMessages;
						AnimCommand anim = this;
						if (timerTask != null) {
							timerTask.cancel();
							timerTask = null;
						}
						timerTask = new TimerTask() {
							@Override
							public void run() {
								String[] text = I18N.getTranslatedMessages("commands.anim.broadcast_text",
										anim.eventName);
								for (String string : text) {
									RawMessage rawMessage = ladder
											.createRawMessage(ChatColor.translateAlternateColorCodes('&', string));
									rawMessage.setClickEvent(ClickEventType.RUN_COMMAND, false, "/animation tp");
									rawMessage.setHoverEvent(HoverEventType.SHOW_TEXT, false, I18N
											.getTranslatedMessages("commands.anim.broadcast_hover", anim.eventName));
									rawMessage.broadcastAll();
									try {
										Thread.sleep(50);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
						};
						timer.schedule(timerTask, 0, this.timeBetweenMessages * 1000L);
						player.sendMessages(okayMessage);
					} catch (Exception error) {
						player.sendMessages(I18N.getTranslatedMessages("commands.anim.mustbeanumber"));
						return;
					}
				} else if (args.length > 1 && args[1].equalsIgnoreCase("stop")) {
					if (!sender.hasPermission("animation.command.alert.stop")) {
						player.sendMessage(I18N.getTranslatedMessage("commands.permission"));
						return;
					}
					if (!enabled) {
						player.sendMessages(I18N.getTranslatedMessages("commands.anim.alert_alreadystopped"));
						return;
					}
					enabled = false;
					timeBetweenMessages = -1;
					eventName = null;
					if (timerTask != null) {
						timerTask.cancel();
						timerTask = null;
					}
					player.sendMessages(I18N.getTranslatedMessages("commands.anim.alert_done"));
				} else if (args.length > 1) {
					player.sendMessages(I18N.getTranslatedMessages("commands.anim.invalidargument"));
					return;
				} else {
					String[] status = enabled ? I18N.getTranslatedMessages("commands.anim.online")
							: I18N.getTranslatedMessages("commands.anim.offline");
					player.sendMessages(I18N.getTranslatedMessages("commands.anim.alert_status", status[0]));
					player.sendMessages(
							I18N.getTranslatedMessages("commands.anim.secondsbetweenmessage", timeBetweenMessages));
					String eventName = this.eventName == null ? I18N.getTranslatedMessage("commands.friend.noone")
							: this.eventName;
					player.sendMessages(I18N.getTranslatedMessages("commands.anim.eventname", eventName));
				}
			} else if (args.length >= 1 && args[0].equalsIgnoreCase("lobby")) {
				if (!sender.hasPermission("animation.command.lobby")) {
					player.sendMessage(I18N.getTranslatedMessage("commands.permission"));
					return;
				}
				if (args.length > 1 && args[1].equalsIgnoreCase("add")) {
					if (!sender.hasPermission("animation.command.lobby.add")) {
						player.sendMessage(I18N.getTranslatedMessage("commands.permission"));
						return;
					}
					if (args.length != 3) {
						player.sendMessages(I18N.getTranslatedMessages("commands.anim.usage_lobby_add"));
						return;
					}
					if (lobbies.contains(args[2])) {
						player.sendMessages(I18N.getTranslatedMessages("commands.anim.lobby_add_already"));
						return;
					}
					Bukkit bukkit = ladder.getBukkitServer(args[2]);
					if (bukkit == null) {
						player.sendMessages(I18N.getTranslatedMessages("commands.anim.lobby_add_unknown"));
						return;
					}
					lobbies.add(args[2]);
					BadBlockOthers instance = BadBlockOthers.getInstance();
					instance.configuration.set("animation.lobbies", lobbies);
					try {
						ConfigurationProvider.getProvider(YamlConfiguration.class).save(
								BadBlockOthers.getInstance().configuration, BadBlockOthers.getInstance().configFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					player.sendMessages(I18N.getTranslatedMessages("commands.anim.lobby_add_done"));
					return;
				} else if (args.length > 1 && (args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("delete")
						|| args[1].equalsIgnoreCase("rm") || args[1].equalsIgnoreCase("del"))) {
					if (!sender.hasPermission("animation.command.lobby.remove")) {
						player.sendMessage(I18N.getTranslatedMessage("commands.permission"));
						return;
					}
					if (args.length != 3) {
						player.sendMessages(I18N.getTranslatedMessages("commands.anim.usage_lobby_remove"));
						return;
					}
					if (!lobbies.contains(args[2])) {
						player.sendMessages(I18N.getTranslatedMessages("commands.anim.lobby_remove_unknown_list"));
						return;
					}
					lobbies.remove(args[2]);
					player.sendMessages(I18N.getTranslatedMessages("commands.anim.lobby_remove_done"));
					BadBlockOthers instance = BadBlockOthers.getInstance();
					instance.configuration.set("animation.lobbies", lobbies);
					try {
						ConfigurationProvider.getProvider(YamlConfiguration.class).save(
								BadBlockOthers.getInstance().configuration, BadBlockOthers.getInstance().configFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				} else if (args.length > 1 && args[1].equalsIgnoreCase("list")) {
					if (!sender.hasPermission("animation.command.lobby.list")) {
						player.sendMessage(I18N.getTranslatedMessage("commands.permission"));
						return;
					}
					player.sendMessages(I18N.getTranslatedMessages("commands.anim.usage_lobby_list"));
					if (lobbies.isEmpty()) {
						player.sendMessages(I18N.getTranslatedMessages("commands.anim.usage_lobby_list_unknown"));
						return;
					}
					int i = 0;
					for (String lobby : lobbies) {
						i++;
						player.sendMessages(I18N.getTranslatedMessages("commands.anim.lobby_list_part", i, lobby));
					}
					return;
				} else {
					player.sendMessages(I18N.getTranslatedMessages("commands.anim.usage_lobby"));
					return;
				}
			} else {
				player.sendMessages(I18N.getTranslatedMessages("commands.anim.usage_general"));
			}
		}
	}

}
