package fr.badblock.bungee.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command{
	public HubCommand(){
		super("hub", "randomhub.use", "lobby");
	}

	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer){} else return;
		ProxiedPlayer player = (ProxiedPlayer) sender;

		if(player.getServer().getInfo().getName().startsWith("hub")){
			player.sendMessage(ChatColor.RED + "Vous êtes déjà au hub.");
		} else {
			ServerInfo lobby = BungeeUtils.instance.roundrobinHub(player);
			if (lobby != null)
				player.connect(lobby);
		}
	}
}
