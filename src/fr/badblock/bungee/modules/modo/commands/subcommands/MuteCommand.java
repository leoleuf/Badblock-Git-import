package fr.badblock.bungee.modules.modo.commands.subcommands;

import java.util.Map.Entry;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.utils.bungee.PunishType;
import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.api.common.utils.permissions.PermissionUser;
import fr.badblock.api.common.utils.time.Time;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.modo.commands.AbstractModCommand;
import fr.badblock.bungee.modules.modo.commands.objects.PunishmentIndex;
import fr.badblock.bungee.modules.modo.commands.objects.PunishmentReason;
import fr.badblock.bungee.modules.modo.commands.objects.PunishmentReasons;
import fr.badblock.bungee.modules.modo.commands.punishments.PunishmentType;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.badblock.bungee.utils.mcjson.McJson;
import fr.badblock.bungee.utils.mcjson.McJsonFactory;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * BadCommand
 * 
 * @author xMalware
 *
 */
public class MuteCommand extends AbstractModCommand {

	/**
	 * Constructor
	 */
	public MuteCommand() {
		// Super!
		super("mute", new String[] { "m" });
	}

	/**
	 * If a player can be muted
	 * 
	 * @param sender
	 * @param playerName
	 * @return
	 */
	public boolean canBeMuted(CommandSender sender, String playerName) {
		// If the muter is a player
		boolean muterPlayer = sender instanceof ProxiedPlayer;

		// Get the target player
		BadOfflinePlayer badOfflinePlayer = BungeeManager.getInstance().getBadOfflinePlayer(playerName);

		// If he doesn't exist
		if (badOfflinePlayer == null || !badOfflinePlayer.isLoaded() || !badOfflinePlayer.isFound()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			// So we stop there
			return false;
		}

		// If the player is already muted
		if (badOfflinePlayer.getPunished().isMute()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("alreadymuted"), null, badOfflinePlayer.getName());
			// So we stop there
			return false;
		}

		// If the muter is a player
		if (muterPlayer) {
			// Get the muter
			BadPlayer badPlayer = BadPlayer.get((ProxiedPlayer) sender);

			// If the muter is null
			if (badPlayer == null) {
				// Send an error message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 1);
				// So we stop there
				return false;
			}

			// Get the muter permissions
			PermissionUser perm = badPlayer.getPermissions();

			// If the muter doesn't have any permissions
			if (perm == null) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 2);
				// So we stop there
				return false;
			}

			// Get the highest muter rank
			Permissible permissible = perm.getHighestRank("bungee", false);

			// If the permissible is null
			if (permissible == null) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 3);
				// So we stop there
				return false;
			}

			// Get the target permissions
			PermissionUser targetPerm = badOfflinePlayer.getPermissions();

			// If the target player doesn't have any permissions
			if (targetPerm == null) {
				// So we stop there
				return true;
			}

			// Get the highest target rank
			Permissible targetPermissible = targetPerm.getHighestRank("bungee", false);

			// If the permissible is null
			if (targetPermissible == null) {
				// So we stop there
				return true;
			}

			// If the muter has a higher rank than the target player (or a bypass)
			if (permissible.getPower() > targetPermissible.getPower()
					|| badPlayer.hasPermission(getPermission() + ".bypasspower")) {
				// So we stop there
				return true;
			}

			// Not enough permission message
			I19n.sendMessage(sender, getPrefix("notenoughpermissions"), null, badOfflinePlayer.getName());
			// Returns false
			return false;
		} else {
			// Returns true
			return true;
		}
	}

	/**
	 * Run
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		// If arg length != 2 or 3
		if (args.length != 2 && args.length != 3) {
			// Send the message
			I19n.sendMessage(sender, getPrefix("usage"), null);
			// So we stop there
			return;
		}

		// Get the player name
		String playerName = args[1];

		// If he can't be muted
		if (!canBeMuted(sender, playerName)) {
			// So we stop there
			return;
		}

		// He's a player?
		boolean isPlayer = sender instanceof ProxiedPlayer;

		ProxiedPlayer proxiedPlayer = isPlayer ? (ProxiedPlayer) sender : null;
		// Get the bad player
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
		// He's a player?
		isPlayer = isPlayer && proxiedPlayer != null && badPlayer != null;

		// two args
		if (args.length == 2) {

			// If the sender is a player
			if (isPlayer) {
				// Send the message
				badPlayer.sendTranslatedOutgoingMessage(getPrefix("select_intro"), null, playerName);
			} else
				// If the sender isn't a player
			{
				// Send the message
				I19n.sendMessage(sender, getPrefix("select_intro"), null, playerName);
			}

			boolean hasReason = false;

			for (Entry<String, PunishmentReason> entry : PunishmentReasons.getInstance().getMuteReasons().entrySet()) {
				// If the sender doesn't have the permission to ban for this reason
				if (!sender.hasPermission(getPermission() + "." + entry.getKey())) {
					// So continue
					continue;
				}

				// One reason!
				hasReason = true;

				// If the sender is a player
				if (isPlayer) {
					// Get the intro message
					String intro = badPlayer.getTranslatedMessage(getPrefix("reason_intro"), null);
					// Get the reason message
					String reason = badPlayer.getTranslatedMessage(getPrefix("reason." + entry.getKey()), null);

					// Get the McJson
					McJson json = new McJsonFactory(intro).finaliseComponent().initNewComponent(reason)
							.setHoverText(reason).setClickCommand("/m mute " + playerName + " " + entry.getKey())
							.finaliseComponent().build();

					// Send the message
					badPlayer.sendTranslatedOutgoingMCJson(json);
				} else
					// If the sender isn't a player
				{
					// Send the reason message
					I19n.sendMessage(sender, getPrefix("reason." + entry.getKey()), null);
				}
			}

			// If we don't have reasons
			if (!hasReason) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("noreason"), null);
			}

			// So we stop there
			return;
		}

		// If the sender is a player
		if (isPlayer && badPlayer.getFlags().has("trymute")) {
			return;
		}

		// Set the flag to avoid to duplicate mutes
		badPlayer.getFlags().set("trymute", 500);

		// Is this a key
		boolean isKey = true;

		// Get the raw mute reason
		String rawMuteReason = args[2];

		// Get the mute reason
		PunishmentReason muteReason = PunishmentReasons.getInstance().getMuteReasons().get(rawMuteReason);

		// If the ban reason doesn't exist
		if (muteReason == null) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknownreason"), null);
			// So we stop there
			return;
		} else {
			if (!sender.hasPermission(getPermission() + "." + rawMuteReason)) {
				I19n.sendMessage(sender, getPrefix("notenoughpermissions"), null, playerName);
				// So we stop there
				return;
			}
		}

		// Get the offline target player
		BadOfflinePlayer badOfflinePlayer = BadOfflinePlayer.get(playerName);

		// Get the reason
		String reason = isKey ? getPrefix("reason." + muteReason.getName()) : rawMuteReason;

		long time = Time.YEAR.convert(1L, Time.MILLIS_SECOND);

		if (muteReason != null && isKey) {

			// Get mongo service
			MongoService mongoService = BadBungee.getInstance().getMongoService();
			// Get database collection
			DBCollection dbCollection = mongoService.getDb().getCollection("punishments");
			// Create query
			BasicDBObject query = new BasicDBObject();
			// Add punished
			query.put("punishedUuid", badOfflinePlayer.getUniqueId().toString().toLowerCase());
			// Add type
			query.put("type", PunishType.MUTE.name());
			// Add reason
			query.put("reason", reason);
			// Get data
			DBCursor cursor = dbCollection.find(query);

			int ban = cursor.count() + 1;

			if (muteReason != null) {
				PunishmentIndex index = null;
				for (PunishmentIndex banIndex : muteReason.getPunishments()) {
					if (index == null || (banIndex.getIndex() > index.getIndex() && ban >= banIndex.getIndex())) {
						index = banIndex;
					}
				}

				if (index != null) {
					String rawTime = index.getTime();
					time = Time.MILLIS_SECOND.matchTime(rawTime);
					if (time == 0L) {
						I19n.sendMessage(sender, getPrefix("unknowntime"), null, rawTime);
						return;
					}
				} else {
					I19n.sendMessage(sender, getPrefix("unknownreason"), null, playerName);
					return;
				}
			}
		}

		PunishmentType.MUTE.process(sender, playerName, reason, isKey, time);

		// Send banned message
		I19n.sendMessage(sender, getPrefix("muted"), isKey ? new int[] { 1 } : null, badOfflinePlayer.getName(),
				reason);
	}

}