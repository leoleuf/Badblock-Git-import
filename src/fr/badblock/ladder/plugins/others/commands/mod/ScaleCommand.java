package fr.badblock.ladder.plugins.others.commands.mod;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.permissions.PermissiblePlayer;

public class ScaleCommand extends Command {

	public ScaleCommand() {
		super("onlinestaff", "onlinestaff.scale", "os");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		new Thread() {
			@Override
			public void run() {
				sender.sendMessage("§8§l§m---------------------------------------------");
				try {
					Thread.sleep(50L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Ladder.getInstance().getPermissions().getGroups().stream().sorted((a, b) -> {
					return Integer.compare(b.getPower(), a.getPower());}).filter(group -> group.isStaff()).forEach(group -> {
					String o = group.getDisplayName() + "&8» &7";
					List<Player> players = Ladder.getInstance().getOnlinePlayers().stream().filter(plo -> ((PermissiblePlayer) plo.getAsPermissible()).getSuperGroup().equals(group.getName()) ||
							((PermissiblePlayer) plo.getAsPermissible()).getAlternateGroups().containsKey(group.getName())).collect(Collectors.toList());
					if (players != null && !players.isEmpty()) {
						Iterator<Player> iterator = players.iterator();
						while (iterator.hasNext()) {
							Player player = iterator.next();
							o += player.getName() + " §8[" + player.getBukkitServer().getName() + "]§7" + (iterator.hasNext() ? ", " : "");
						}
					}else o += "§cAucun.";
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', o));
					try {
						Thread.sleep(50L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				});
				try {
					Thread.sleep(50L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sender.sendMessage("§8§l§m---------------------------------------------");
			}
		}.start();
	}

}