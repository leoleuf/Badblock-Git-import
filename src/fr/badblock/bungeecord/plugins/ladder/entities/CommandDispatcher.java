package fr.badblock.bungeecord.plugins.ladder.entities;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.plugin.Command;
import fr.badblock.bungeecord.api.plugin.TabExecutor;
import fr.badblock.bungeecord.plugins.ladder.LadderBungee;
import fr.badblock.bungeecord.plugins.ladder.Player;
import fr.badblock.common.protocol.packets.Packet;
import fr.badblock.common.protocol.packets.PacketPlayerChat;
import fr.badblock.common.protocol.packets.PacketPlayerChat.ChatAction;
import fr.badblock.common.protocol.utils.StringUtils;

public class CommandDispatcher extends Command implements TabExecutor {
	private boolean bypassable;

	public CommandDispatcher(String name, boolean bypassable) {
		super(name);

		this.bypassable = bypassable;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		String command = getName() + " " + StringUtils.join(args, " ");
		if(bypassable && sender instanceof ProxiedPlayer && sender.hasPermission("ladder.command.bypass")){
			((ProxiedPlayer) sender).chat("/" + command); return;
		}

		Packet packet = null;

		String name = !(sender instanceof ProxiedPlayer) ? null : ((ProxiedPlayer) sender).getName();
		packet = new PacketPlayerChat(name, ChatAction.LADDER_COMMAND, command);

		LadderBungee.getInstance().getClient().sendPacket(packet);
	}

	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		if(args.length == 0){
			return ImmutableSet.of();
		}

		Set<String> matches = new HashSet<>();
		String search = args[args.length - 1].toLowerCase();
		if (sender.hasPermission("tab")) {
			for(String player : LadderBungee.getInstance().getConnectPlayers()) {
				if(player.toLowerCase().startsWith(search)) {
					matches.add(player);
				}
			}
			for(Player player : LadderBungee.getInstance().getPlayerList()) {
				matches.add(player.getName());
			}
		}

		return matches;
	}
}
