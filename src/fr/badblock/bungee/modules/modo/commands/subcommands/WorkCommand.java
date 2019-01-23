package fr.badblock.bungee.modules.modo.commands.subcommands;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.StringTag;

import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.modo.commands.AbstractModCommand;
import fr.badblock.bungee.packets.item.ItemStack;
import fr.badblock.bungee.packets.item.ItemTypes;
import fr.badblock.bungee.packets.window.impl.ChestWindow;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.ChatColor;
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
public class WorkCommand extends AbstractModCommand {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	/**
	 * Constructor
	 */
	public WorkCommand() {
		// Super!
		super("work", new String[] { "w" });
	}

	/**
	 * Run
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer))
		{
			// Send the message
			I19n.sendMessage(sender, getPrefix("onlyforplayers"), null);
			return;
		}

		if (args.length < 2) {
			// Send the message
			I19n.sendMessage(sender, getPrefix("usage"), null);
			// So we stop there
			return;
		}

		// Get the player name
		String playerName = args[1];

		BungeeManager bungeeManager = BungeeManager.getInstance();

		BadOfflinePlayer badOfflinePlayer = bungeeManager.getBadOfflinePlayer(playerName);

		if (badOfflinePlayer == null || !badOfflinePlayer.isFound())
		{
			// Send the message
			I19n.sendMessage(sender, getPrefix("unknown"), null);
			return;
		}

		boolean online = bungeeManager.hasUsername(playerName);
		// Get the online target player
		BadPlayer badOnlinePlayer = bungeeManager.getBadPlayer(playerName);

		ProxiedPlayer senderProxied = (ProxiedPlayer) sender;
		BadPlayer senderPlayer = BadPlayer.get(senderProxied);

		if (senderPlayer == null)
		{
			// Send the message
			I19n.sendMessage(sender, getPrefix("error"), null);
			return;
		}

		ChestWindow window = new ChestWindow(9);
		String inventoryName = getInvMessage(senderPlayer, "name", badOfflinePlayer.getName());
		window.setTitle(new TextComponent(inventoryName));

		ItemStack itemStack = new ItemStack(ItemTypes.MOB_HEAD_HUMAN);
		itemStack.setDisplayName(getInvMessage(senderPlayer, "playerinfo_name", badOfflinePlayer.getName()));
		CompoundMap tags = new CompoundMap();
		tags.put(new StringTag("Name", badOfflinePlayer.getName()));
		itemStack.setNbt(new CompoundTag("Owner", tags));

		List<String> lore;
		String[] playerInfoLore;

		String noPermission = getInvMessage(senderPlayer, "notenoughperms");

		String ip = hasPermission(senderPlayer, "showip") ? badOfflinePlayer.getLastIp() : noPermission;
		String doubleAuth = noPermission;

		if (hasPermission(senderPlayer, "doubleauth"))
		{
			if (senderPlayer.getAuthKey() != null && !senderPlayer.getAuthKey().isEmpty())
			{
				doubleAuth = getInvMessage(senderPlayer, "doubleauth_enabled");
			}
			else
			{
				doubleAuth = getInvMessage(senderPlayer, "doubleauth_disabled");
			}
		}

		String onlineMode = getInvMessage(senderPlayer, "onlinemode_" + (badOfflinePlayer.isOnlineMode() ? "enabled" : "disabled"));

		if (!online)
		{
			playerInfoLore = getInvMessages(senderPlayer, "playerinfo_lore_offline", badOfflinePlayer.getName(), sdf.format(new Date(badOfflinePlayer.getLastLogin())),
					badOfflinePlayer.getVersion(), ip, doubleAuth, ChatColor.translateAlternateColorCodes('&', senderPlayer.getTranslatedMessage(badOfflinePlayer.getRawChatPrefix(), null)),
					onlineMode);
		}
		else
		{
			playerInfoLore = getInvMessages(senderPlayer, "playerinfo_lore_online", badOnlinePlayer.getName(), sdf.format(new Date(badOfflinePlayer.getLastLogin())),
					badOnlinePlayer.getVersion(), ip, doubleAuth, ChatColor.translateAlternateColorCodes('&', senderPlayer.getTranslatedMessage(badOfflinePlayer.getRawChatPrefix(), null)),
					onlineMode, badOnlinePlayer.getCurrentServer(), badOnlinePlayer.getPing());
		}

		lore = Arrays.asList(playerInfoLore);
		itemStack.setLore(lore);

		window.set(0, itemStack);

		if (online)
		{
			itemStack = new ItemStack(ItemTypes.LEAD);
			itemStack.setDisplayName(getInvMessage(senderPlayer, "teleportplayer_name", badOfflinePlayer.getName()));
			itemStack.setLore(Arrays.asList(getInvMessages(senderPlayer, "teleportplayer_lore", badOfflinePlayer.getName(),
					badOnlinePlayer.getCurrentServer())));
			window.set(1, itemStack);
		}
		else
		{
			itemStack = new ItemStack(ItemTypes.RED_STAINED_GLASS_PANE);
			itemStack.setDisplayName(getInvMessage(senderPlayer, "offline", badOfflinePlayer.getName()));
			window.set(1, itemStack);
		}

		if (online)
		{
			itemStack = new ItemStack(ItemTypes.CLOCK);
			itemStack.setDisplayName(getInvMessage(senderPlayer, "ghost_name", badOfflinePlayer.getName()));
			itemStack.setLore(Arrays.asList(getInvMessages(senderPlayer, "ghost_lore", badOfflinePlayer.getName(),
					badOnlinePlayer.getCurrentServer())));
			window.set(2, itemStack);
		}
		else
		{
			itemStack = new ItemStack(ItemTypes.RED_STAINED_GLASS_PANE);
			itemStack.setDisplayName(getInvMessage(senderPlayer, "offline", badOfflinePlayer.getName()));
			window.set(2, itemStack);
		}

		itemStack = new ItemStack(ItemTypes.ENCHANTED_BOOK);
		itemStack.setDisplayName(getInvMessage(senderPlayer, "records_name", badOfflinePlayer.getName()));
		itemStack.setLore(Arrays.asList(getInvMessages(senderPlayer, "records_lore", badOfflinePlayer.getName())));
		window.set(3, itemStack);

		itemStack = new ItemStack(ItemTypes.CYAN_WOOL);
		itemStack.setDisplayName(getInvMessage(senderPlayer, "warn_name", badOfflinePlayer.getName()));
		itemStack.setLore(Arrays.asList(getInvMessages(senderPlayer, "warn_lore", badOfflinePlayer.getName())));
		window.set(5, itemStack);

		if (online)
		{
			itemStack = new ItemStack(ItemTypes.YELLOW_WOOL);
			itemStack.setDisplayName(getInvMessage(senderPlayer, "kick_name", badOfflinePlayer.getName()));
			itemStack.setLore(Arrays.asList(getInvMessages(senderPlayer, "kick_lore", badOfflinePlayer.getName())));
			window.set(6, itemStack);
		}
		else
		{
			itemStack = new ItemStack(ItemTypes.RED_STAINED_GLASS_PANE);
			itemStack.setDisplayName(getInvMessage(senderPlayer, "offline", badOfflinePlayer.getName()));
			window.set(6, itemStack);
		}

		if (badOfflinePlayer.getPunished() != null && badOfflinePlayer.getPunished().isMute())
		{
			itemStack = new ItemStack(ItemTypes.SPLASH_POTION);
			itemStack.setDisplayName(getInvMessage(senderPlayer, "unmute_name", badOfflinePlayer.getName()));
			itemStack.setLore(Arrays.asList(getInvMessages(senderPlayer, "unmute_lore", badOfflinePlayer.getName())));
			window.set(7, itemStack);
		}
		else
		{
			itemStack = new ItemStack(ItemTypes.ORANGE_WOOL);
			itemStack.setDisplayName(getInvMessage(senderPlayer, "mute_name", badOfflinePlayer.getName()));
			itemStack.setLore(Arrays.asList(getInvMessages(senderPlayer, "mute_lore", badOfflinePlayer.getName())));
			window.set(7, itemStack);
		}

		if (badOfflinePlayer.getPunished() != null && badOfflinePlayer.getPunished().isBan())
		{
			itemStack = new ItemStack(ItemTypes.SPLASH_POTION);
			itemStack.setDisplayName(getInvMessage(senderPlayer, "unban_name", badOfflinePlayer.getName()));
			itemStack.setLore(Arrays.asList(getInvMessages(senderPlayer, "unban_lore", badOfflinePlayer.getName())));
			window.set(8, itemStack);
		}
		else
		{
			itemStack = new ItemStack(ItemTypes.RED_WOOL);
			itemStack.setDisplayName(getInvMessage(senderPlayer, "ban_name", badOfflinePlayer.getName()));
			itemStack.setLore(Arrays.asList(getInvMessages(senderPlayer, "ban_lore", badOfflinePlayer.getName())));
			window.set(8, itemStack);
		}

		senderPlayer.open(window);
	}

	private boolean hasPermission(BadPlayer badPlayer, String permission)
	{
		return badPlayer.hasPermission(getPermission() + "." + permission);
	}

	private String getInvMessage(BadPlayer badPlayer, String message, Object... strings)
	{
		return badPlayer.getTranslatedMessage(getPrefix("inventory." + message), null, strings);
	}

	private String[] getInvMessages(BadPlayer badPlayer, String message, Object... strings)
	{
		return badPlayer.getTranslatedMessages(getPrefix("inventory." + message), null, strings);
	}

}