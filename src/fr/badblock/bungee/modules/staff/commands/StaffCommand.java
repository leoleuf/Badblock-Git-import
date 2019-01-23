package fr.badblock.bungee.modules.staff.commands;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.packets.item.ItemStack;
import fr.badblock.bungee.packets.item.ItemTypes;
import fr.badblock.bungee.packets.window.impl.ChestWindow;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * 
 * Online staff
 * 
 * @author xMalware
 *
 */
public class StaffCommand extends BadCommand {

	private static String prefix = "bungee.commands.staff.";

	/**
	 * Command constructor
	 */
	public StaffCommand(Plugin plugin) {
		super(plugin, "staff", "");
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer))
		{
			I19n.sendMessage(sender, "bungee.commands.playersonly", null);
			return;
		}
		
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		
		if (args.length == 1)
		{
			String playerName = args[0];
			
			if (!BungeeManager.getInstance().hasUsername(playerName))
			{
				I19n.sendMessage(sender, prefix + "offlineplayer", null);
				return;
			}
			
			String txt = I19n.getMessage(sender, prefix + "msg", null, playerName);
			TextComponent textComponent = new TextComponent(txt);
			textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + playerName + " <Votre message ici>"));
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] { new TextComponent(txt) }));
			
			proxiedPlayer.sendMessage(textComponent);
			return;
		}
		
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
		
		Set<Permissible> permissibles = BungeeManager.getInstance()
				.getLoggedPlayers(player -> player.getPermissions().getHighestRank("bungee", true) != null)
				.parallelStream()
				.map(player -> player.getPermissions().getHighestRank("bungee", true))
				.filter(perm -> perm != null && (perm.getName().contains("helper") || perm.getName().contains("modo")))
				.sorted(new Comparator<Permissible>() {
					
					public int compare(Permissible o1, Permissible o2) {
						return Integer.valueOf(o1.getPower()).compareTo(o2.getPower());
					}
				}).collect(Collectors.toSet());

		if (permissibles == null)
		{
			permissibles = new HashSet<>();
		}
		
		int lines = (int) (Math.ceil((double) permissibles.size() / (double) 9) * 9D);
		if (lines < 9) lines = 9;
		
		if (lines > 54)
		{
			lines = 54;
		}
		
		ChestWindow window = new ChestWindow(lines);
		window.setTitle(new TextComponent(I19n.getMessage(sender, prefix + "inventory_name", null)));
		
		int x = 0;
		for (Permissible permissible : permissibles)
		{
			List<BadPlayer> usernames = BungeeManager.getInstance().getLoggedPlayers(
					player -> player.getPermissions().getHighestRank("bungee", true) != null && player.getPermissions()
							.getHighestRank("bungee", false).getName().equalsIgnoreCase(permissible.getName()));
			
			for (BadPlayer bPlayer : usernames)
			{
				ItemStack itemStack = new ItemStack(ItemTypes.MOB_HEAD_HUMAN);
				itemStack.setDisplayName(I19n.getMessage(sender, prefix + "item_name", null, ChatColor.translateAlternateColorCodes('&', 
						badPlayer.getTranslatedMessage(bPlayer.getRawChatSuffix(), null)), bPlayer.getName()));
				String currentServer = badPlayer.getCurrentServer();
				itemStack.setLore(Arrays.asList(I19n.getMessages(sender, prefix + "item_lore", null, ChatColor.translateAlternateColorCodes('&', 
						badPlayer.getTranslatedMessage(bPlayer.getRawChatPrefix(), null)), bPlayer.getName(), currentServer)));
				
				window.set(x, itemStack);
				x++;
			}
		}
		
		if (x == 0)
		{
			I19n.sendMessage(sender, prefix + "nobody", null);
			return;
		}
		
		badPlayer.open(window);
	}

}