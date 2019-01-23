package fr.badblock.bungee.modules.modo.commands.subcommands;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.utils.bungee.PunishType;
import fr.badblock.api.common.utils.bungee.Punishment;
import fr.badblock.api.common.utils.i18n.Locale;
import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.api.common.utils.permissions.PermissionUser;
import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.modo.commands.AbstractModCommand;
import fr.badblock.bungee.packets.item.ItemStack;
import fr.badblock.bungee.packets.item.ItemType;
import fr.badblock.bungee.packets.item.ItemTypes;
import fr.badblock.bungee.packets.window.impl.ChestWindow;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * BadCommand
 * 
 * @author xMalware
 *
 */
public class SanctionCommand extends AbstractModCommand {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	/**
	 * Constructor
	 */
	public SanctionCommand() {
		// Super!
		super("sanction", new String[] { "casier", "case", "s", "c", "cases", "records", "record", "r" });
	}

	/**
	 * If a player can view ban history
	 * 
	 * @param sender
	 * @param playerName
	 * @return
	 */
	public boolean canViewHistory(CommandSender sender, String playerName) {
		// If the checker is a player
		boolean checkerPlayer = sender instanceof ProxiedPlayer;

		// Get the target player
		BadOfflinePlayer badOfflinePlayer = BungeeManager.getInstance().getBadOfflinePlayer(playerName);

		// If he doesn't exist
		if (badOfflinePlayer == null || !badOfflinePlayer.isLoaded() || !badOfflinePlayer.isFound()) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null, playerName);
			// So we stop there
			return false;
		}

		// If the checker is a player
		if (checkerPlayer) {
			// Get the checker
			BadPlayer badPlayer = BadPlayer.get((ProxiedPlayer) sender);

			// If the checker is null
			if (badPlayer == null) {
				// Send an error message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 1);
				// So we stop there
				return false;
			}

			// Get the checker permissions
			PermissionUser perm = badPlayer.getPermissions();

			// If the checker doesn't have any permissions
			if (perm == null) {
				// Send a message
				I19n.sendMessage(sender, getPrefix("erroroccurred"), null, 2);
				// So we stop there
				return false;
			}

			// Get the highest checker rank
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

			// If the checker has a higher rank than the target player (or a bypass)
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
		// If arg length != 2
		if (args.length != 2) {
			// Send the message
			I19n.sendMessage(sender, getPrefix("usage"), null);
			// So we stop there
			return;
		}

		if (!(sender instanceof ProxiedPlayer))
		{
			I19n.sendMessage(sender, getPrefix("onlyforplayers"), null);
			return;
		}

		// Get the player name
		String playerName = args[1];

		BungeeManager bungeeManager = BungeeManager.getInstance();

		// Get the offline target player
		BadOfflinePlayer badOfflinePlayer = bungeeManager.getBadOfflinePlayer(playerName);

		if (badOfflinePlayer == null) {
			I19n.sendMessage(sender, getPrefix("unknownplayer"), null);
			return;
		}

		// If he can't view ban history
		if (!canViewHistory(sender, playerName)) {
			// So we stop there
			return;
		}

		I19n.sendMessage(sender, getPrefix("intro"), null, badOfflinePlayer.getName());

		// Get mongo service
		MongoService mongoService = BadBungee.getInstance().getMongoService();
		// Get database collection
		DBCollection dbCollection = mongoService.getDb().getCollection("punishments");
		// Create query
		BasicDBObject query = new BasicDBObject();
		// Add punished
		query.put("punishedUuid", badOfflinePlayer.getUniqueId().toString().toLowerCase());
		// Get data
		DBCursor cursor = dbCollection.find(query);
		// Data?
		boolean data = false;
		// Get sender locale
		Locale senderLocale = !(sender instanceof ProxiedPlayer) ? Locale.FRENCH_FRANCE : null;
		// Get the proxied player
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		// Get the bad player
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
		// If the sender locale is null
		if (senderLocale == null) {
			if (badPlayer != null) {
				senderLocale = badPlayer.getLocale();
			} else {
				senderLocale = Locale.FRENCH_FRANCE;
			}
		}

		long count = dbCollection.count(query);
		int lines = (int) (Math.ceil((double) count / (double) 9) * 9D);
		if (lines < 9) lines = 9;
		if (lines > 54) lines = 54;
		
		ChestWindow chestWindow = new ChestWindow(lines);
		chestWindow.setTitle(new TextComponent(badOfflinePlayer.getTranslatedMessage(getPrefix("inventory_name"), null, playerName)));

		int i = 0;
		// If there's data
		while (cursor.hasNext()) {
			try
			{
				// There is a data
				data = true;
				// Get the database object
				DBObject dbObject = cursor.next();
				// Create a punishment
				Punishment punishment = new Punishment(dbObject);

				ItemType itemType;

				if (PunishType.BAN.equals(punishment.getType()))
				{
					itemType = ItemTypes.RED_WOOL;
				}
				else if (PunishType.MUTE.equals(punishment.getType()))
				{
					itemType = ItemTypes.ORANGE_WOOL;
				}
				else if (PunishType.KICK.equals(punishment.getType()))
				{
					itemType = ItemTypes.YELLOW_WOOL;
				}
				else if (PunishType.WARN.equals(punishment.getType()))
				{
					itemType = ItemTypes.CYAN_WOOL;
				}
				else
				{
					itemType = ItemTypes.WHITE_WOOL;
				}

				ItemStack itemStack = new ItemStack(itemType);
				itemStack.setDisplayName(badOfflinePlayer.getTranslatedMessage(getPrefix("itemname_" + punishment.getType().name().toLowerCase()), null));
				String reason = punishment.isReasonKey() ? badOfflinePlayer.getTranslatedMessage(punishment.getReason(), null) : punishment.getReason();
				
				itemStack.setLore(Arrays.asList(badOfflinePlayer.getTranslatedMessages(getPrefix("itemname_" + punishment.getType().name().toLowerCase()
						+ "_lore"), null, 
						badOfflinePlayer.getName(), reason, sdf.format(new Date(punishment.getTimestamp())), punishment.getPunisher())));

				chestWindow.set(i, itemStack);
				i++;
			}
			catch (Exception e)
			{

			}
		}

		badPlayer.open(chestWindow);

		// If there's no data
		if (!data) {
			// Send a message
			I19n.sendMessage(sender, getPrefix("norecords"), null, badOfflinePlayer.getName());
		}
	}

}