package fr.badblock.bungeecord.plugins.utils;

import fr.badblock.bungeecord.api.ChatColor;
import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.config.ServerInfo;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.plugin.Command;

public class HubCommand extends Command{
	public HubCommand(){
		super("hub", "randomhub.use", "lobby");
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer){} else return;
		ProxiedPlayer player = (ProxiedPlayer) sender;

		if(player.getServer().getInfo().getName().startsWith("hub")){
			player.sendMessage(ChatColor.RED + "Vous êtes déjà au hub.");
			return;
		} else {
			ServerInfo lobby = BungeeUtils.instance.roundrobinHub();
			if (lobby != null)
				player.connect(lobby);
		}
	}
}
