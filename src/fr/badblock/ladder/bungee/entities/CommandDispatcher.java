package fr.badblock.ladder.bungee.entities;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import fr.badblock.ladder.bungee.LadderBungee;
import fr.badblock.ladder.bungee.Player;
import fr.badblock.protocol.packets.Packet;
import fr.badblock.protocol.packets.PacketPlayerChat;
import fr.badblock.protocol.packets.PacketPlayerChat.ChatAction;
import fr.badblock.protocol.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class CommandDispatcher extends Command implements TabExecutor {
	private boolean bypassable;
	
	public CommandDispatcher(String name, boolean bypassable) {
		super(name);
		
		this.bypassable = bypassable;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		String command = getName() + " " + StringUtils.join(args, " ");
		if (sender instanceof ProxiedPlayer) {
		System.out.println("Tentative de tab / " + ((ProxiedPlayer) sender).getAddress().getHostString());
		}
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
		if(args.length == 0 || !sender.hasPermission("tab")){
			return ImmutableSet.of();
		}

		Set<String> matches = new HashSet<>();
		String search = args[args.length - 1].toLowerCase();

		for(Player player : LadderBungee.getInstance().getPlayers()) {
			if(player.getName().toLowerCase().startsWith(search)) {
				matches.add(player.getName());
			}
		}

		return matches;
	}
}
