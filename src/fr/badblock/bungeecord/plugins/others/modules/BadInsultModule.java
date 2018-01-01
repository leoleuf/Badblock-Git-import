package fr.badblock.bungeecord.plugins.others.modules;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.bungeecord.plugins.others.Player;
import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import fr.badblock.bungeecord.plugins.others.modules.abstracts.Module;
import fr.badblock.common.commons.technologies.rabbitmq.RabbitPacketType;
import fr.badblock.common.commons.utils.Encodage;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BadInsultModule extends Module {

	public static BadInsultModule	instance	= null;

	public  List<String> insultsList 			= new ArrayList<>();
	public  List<String> insultsMuteList 		= new ArrayList<>();
	private List<String> antiSpam				= new ArrayList<>();
	public  List<String> pseudoList				= new ArrayList<>();
	private List<String> antiFlood				= new ArrayList<>();
	private long		 timeBetweenEachMessage = 1000L;
	private long		 timeBetweenSameMessage = 60000L;
	private List<String> commands				= new ArrayList<>();

	public BadInsultModule() {
		instance = this;
		Configuration configuration = BadBlockBungeeOthers.getInstance().getConfiguration();
		boolean changed = false;
		if (configuration.get("modules.badInsult.insults") == null) {
			insultsList.add("tg");
			configuration.set("modules.badInsult.insults", insultsList);
			changed = true;
		}
		if (configuration.get("modules.badInsult.insultsmute") == null) {
			insultsMuteList.add("tg");
			configuration.set("modules.badInsult.insultsmute", insultsMuteList);
			changed = true;
		}
		if (configuration.get("modules.spam.antiSpam") == null) {
			configuration.set("modules.spam.antiSpam", antiSpam);
			changed = true;
		}
		if (configuration.get("modules.spam.antiFlood") == null) {
			configuration.set("modules.spam.antiFlood", antiFlood);
			changed = true;
		}
		if (configuration.get("modules.spam.timeBetweenEachMessage") == null) {
			configuration.set("modules.spam.timeBetweenEachMessage", timeBetweenEachMessage);
			changed = true;
		}
		if (configuration.get("modules.spam.timeBetweenSameMessage") == null) {
			configuration.set("modules.spam.timeBetweenSameMessage", timeBetweenSameMessage);
			changed = true;
		}
		/*if (configuration.get("modules.badInsult.insultError") == null) {
			configuration.set("modules.badInsult.insultError", insultError);
			changed = true;
		}*/
		if (configuration.get("modules.badInsult.onlyForCommands") == null) {
			/*
			 * 	/*if (!message.startsWith("msg") && !message.startsWith("whisper") && !message.startsWith("m") && !message.startsWith("w") && !message.startsWith("tellraw") 
				&& !message.startsWith("tell") && !message.startsWith("minecraft:tell") && !message.startsWith("message:tellraw") && 
				!message.startsWith("minecraft:whisper") && !message.startsWith("minecraft:w") && !message.startsWith("r")) return;
			 */
			commands.add("msg");
			commands.add("whisper");
			commands.add("m");
			commands.add("w");
			commands.add("tellraw");
			commands.add("tell");
			commands.add("minecraft:tell");
			commands.add("minecraft:tellraw");
			commands.add("minecraft:whisper");
			commands.add("minecraft:w");
			commands.add("r");
			configuration.set("modules.badInsult.onlyForCommands", commands);
			changed = true;
		}
		if (changed) {
			try {
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(BadBlockBungeeOthers.getInstance().getDataFolder(), "config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}	
			try {
				configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(BadBlockBungeeOthers.getInstance().getDataFolder(), "config.yml"));
				BadBlockBungeeOthers.getInstance().setConfiguration(configuration);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		insultsList = configuration.getStringList("modules.badInsult.insults");
		pseudoList = configuration.getStringList("modules.badInsult.pseudoList");
		List<String> temporary = new ArrayList<>();
		insultsList.forEach(insult -> temporary.add(applyFilterN(insult)));
		insultsList = temporary;
		List<String> temporarye = new ArrayList<>();
		insultsMuteList = configuration.getStringList("modules.badInsult.insultsmute");
		insultsMuteList.forEach(insult -> temporarye.add(applyFilterN(insult)));
		insultsMuteList = temporarye;
		antiSpam = configuration.getStringList("modules.spam.antiSpam");
		antiFlood = configuration.getStringList("modules.spam.antiFlood");
		//insultError = configuration.getStringList("modules.badInsult.insultError");
		commands = configuration.getStringList("modules.badInsult.onlyForCommands");
		timeBetweenEachMessage = configuration.getLong("modules.spam.timeBetweenEachMessage");
		timeBetweenSameMessage = configuration.getLong("modules.spam.timeBetweenSameMessage");
	}
	public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");

	@SuppressWarnings({ "deprecation", "static-access" })
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onAsyncChat(ChatEvent event) {
		Connection sender = event.getSender();
		if (!(sender instanceof ProxiedPlayer)) return;
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if (player.getServer() == null || player.getServer().getInfo() == null || player.getServer().getInfo().getName() == null || player.getServer().getInfo().getName().startsWith("login")) return;
		if (player.hasPermission("chat.bypass")) return;
		fr.badblock.bungeecord.plugins.ladder.Player pl = fr.badblock.bungeecord.plugins.ladder.LadderBungee.getInstance().getPlayer(player.getName());
		if (pl.getPunished().isMute() || pl.getPunished().getMuteEnd() > System.currentTimeMillis()) return;
		String message = event.getMessage();
		if (message.startsWith("/")) {
			boolean bypass = true;
			for (String command : commands) {
				if (message.startsWith("/" + command + " ")) {
					bypass = false;
					break;
				}
			}
			if (bypass) return;
		}

		//if (event.isCommand()) return;
		// Réécriture du message sans doublons
		// plus ou égal à trois choses
		int nb = 0;
		String filteredMessage = applyFilter(message);
		Character lastCharacter = null;
		int upperCase = 0;
		String okMessage = "";
		for (Character character : message.toCharArray()) {
			if (lastCharacter != null && character.toString().equals(lastCharacter.toString()) && (lastCharacter.toString().equals("!") || lastCharacter.toString().equals("?"))) {
				nb++;
				if (nb >= 2 && (lastCharacter.toString().equals("!") || lastCharacter.toString().equals("?"))) {
					if (nb == 2) {
						if (lastCharacter.toString().equals("w")) {
							continue;
						}
					}
					player.sendMessage(BadBlockBungeeOthers.getInstance().getMessage(antiFlood));
					event.setCancelled(true);
					return;
				}
			}else{
				nb = 0;
				if (character.isUpperCase(character)) {
					if (upperCase >= 5) {
						character = character.toLowerCase(character);
					}
					upperCase++;
				}
			}
			lastCharacter = character;
			okMessage += character.toString();
		}
		message = okMessage;
		// Test d'insultes
		boolean handle = false;
		/*if (!invincible && !handle) {
		/*boolean invincible = player.hasPermission("ladder.command.sanction");
		if (!invincible && !handle) {
			for (String insult : insultsMuteList) {
				if ((!insult.contains("_") && (filteredMessage.contains(insult) || filteredMessage.equalsIgnoreCase(insult))) ||
						(insult.contains("_") && ((event.getMessage().contains(" " + insult + " ") || event.getMessage().startsWith(insult + " ") || event.getMessage().endsWith(" " + insult))))) {
					BadBlockBungeeOthers.getInstance().getRabbitService().sendPacket("badfilter", "§eVérification neuronale §9>> §b" + player.getName() + " §e(" + insult + ")", Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
					handle = true;
					final String finalMessage = message;
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							if (pl.getPunished() != null && (pl.getPunished().isMute() && pl.getPunished().getMuteEnd() > System.currentTimeMillis())) return;
							Sanction sanction = new Sanction(player.getName(), "mute", System.currentTimeMillis() + 1800_000L, System.currentTimeMillis(), "Message irrespectueux envers les CGU : " + finalMessage, pseudoList.get(new Random().nextInt(pseudoList.size())), "127.0.0.1", finalMessage, false);
							BadBlockBungeeOthers.getInstance().getRabbitService().sendPacket("sanction", new Gson().toJson(sanction), Encodage.UTF8, RabbitPacketType.MESSAGE_BROKER, 5000, false);
							if (pl.getPunished() != null) {
								pl.getPunished().setMuteEnd(System.currentTimeMillis() + 1800_000L);
								pl.getPunished().setMute(true);
								pl.getPunished().setMuter(sanction.getBanner());
								pl.getPunished().setMuteReason("Message irrespectueux envers les CGU : " + finalMessage);
							}
							player.sendMessage("§4----------------------------------------");
							player.sendMessage("§cVous avez été baîllonné par l'IA qui veille");
							player.sendMessage("§csur le t'chat du jeu. Cette sanction est temporaire.");
							player.sendMessage("§c");
							player.sendMessage("§eLe message en question est le suivant :");
							player.sendMessage("§c'" + finalMessage + "'");
							player.sendMessage("§c");
							player.sendMessage("§9En cas de faux positif, veuillez nous le signaler");
							player.sendMessage("§9sur le forum dans la catégorie bugs.");
							player.sendMessage("§4----------------------------------------");
							String msg = "§c[§6Guardian§c] §b" + player.getName() + "§6 a été sanctionné pour §cMessage irrespectueux§6.";
							BadBlockBungeeOthers.getInstance().getRabbitService().sendPacket("guardian.broadcast", msg, Encodage.UTF8, RabbitPacketType.MESSAGE_BROKER, 5000, false);
							BadBlockBungeeOthers.getInstance().getRabbitService().sendPacket("badfilter", "§cPunish §9>> §e" + player.getName() + " §8- §9" + insult + " §8: §7" + finalMessage, Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
						}
					}, new Random().nextInt(5000) + 5000);
					return;
				}
			}
		}*/
		if (!handle) {
			for (String insult : insultsList) {
				if ((!insult.contains("_") && (filteredMessage.contains(insult) || filteredMessage.equalsIgnoreCase(insult))) ||
						(insult.contains("_") && ((event.getMessage().contains(" " + insult + " ") || event.getMessage().startsWith(insult + " ") || event.getMessage().endsWith(" " + insult))))) {
					//event.setCancelled(true);
					//event.setCancelled(true);
					BadblockDatabase.getInstance().addRequest(new Request("INSERT INTO reportMsg(byPlayer, player, message, timestamp) VALUES('', '" + BadblockDatabase.getInstance().mysql_real_escape_string(player.getName()) + "', '" + BadblockDatabase.getInstance().mysql_real_escape_string(message) + "', '" + System.currentTimeMillis() + "')", RequestType.SETTER));
					BadBlockBungeeOthers.getInstance().getRabbitService().sendPacket("badfilter", "§7" + player.getName() + " (" + insult + ") §8» §7" + message, Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
					handle = true;
					break;
					//player.sendMessage(BadBlockBungeeOthers.getInstance().getMessage(this.insultError));
				}
			}
		}
		if (BadCommunitySpookerModule.getInstance().testSpooking(player, event)) return;
		// Test de publicité
		String filteredWithoutDotMessage = message.toLowerCase().replaceAll("[^\\w.]+", "").replace(" ", "").replace("_", "");
		BadAdvertsModule.getInstance().testAdvert(player, event, filteredMessage, filteredWithoutDotMessage);
		Player playerO = Player.get(player);
		if (!event.isCancelled()) {
			if (playerO.getLastMessage() != null && playerO.getLastMessageTime() > System.currentTimeMillis()) {
				event.setCancelled(true);
				player.sendMessage(BadBlockBungeeOthers.getInstance().getMessage(this.antiFlood) + "!");
				return;
			}
			if (playerO.getSpamMessages().containsKey(filteredMessage)) {
				long time = playerO.getSpamMessages().get(filteredMessage);
				if (time > System.currentTimeMillis()) {
					event.setCancelled(true);
					player.sendMessage(BadBlockBungeeOthers.getInstance().getMessage(this.antiSpam));
					return;
				}
			}
		}
		playerO.getSpamMessages().put(filteredMessage, System.currentTimeMillis() + timeBetweenSameMessage);
		playerO.setLastMessageTime(System.currentTimeMillis() + timeBetweenEachMessage);
		playerO.setLastMessage(filteredMessage);
		event.setMessage(message);
	}

	public String applyFilter(String string) {
		string = string.toLowerCase().replaceAll("\\W", "").replace(" ", "").replace("_", "");
		String o = "";
		Character lastCharacter = null;
		for (Character character : string.toCharArray()) {
			if (lastCharacter == null || !lastCharacter.toString().equals(character.toString())) 
				o += character.toString();
			lastCharacter = character;
		}
		string = o;
		return o;
	}

	public String applyFilterN(String string) {
		string = string.toLowerCase().replaceAll("\\W", "").replace(" ", "");
		String o = "";
		Character lastCharacter = null;
		for (Character character : string.toCharArray()) {
			if (lastCharacter == null || !lastCharacter.toString().equals(character.toString())) 
				o += character.toString();
			lastCharacter = character;
		}
		string = o;
		return o;
	}

}
