package fr.badblock.bungee.utils.commands;

import fr.badblock.bungee.utils.BungeeUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.protocol.ProtocolConstants;

public class HubCommand extends Command{
	public HubCommand(){
		super("hub", "randomhub.use", "lobby");
	}

	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer){} else return;
		ProxiedPlayer player = (ProxiedPlayer) sender;
		
		if(player.getPendingConnection().getVersion() < ProtocolConstants.MINECRAFT_1_8){
			ServerInfo target = BungeeUtils.instance.getProxy().getServerInfo("lobby_v17");
        	player.connect(target); return;
		}
		
		if(BungeeUtils.lobbysServer.contains(player.getServer().getInfo().getName())){
			if(args.length > 0){
				try{
					int lobby = Integer.parseInt(args[0]);
					lobby--;
					ServerInfo target = BungeeUtils.instance.getLobby(lobby);
					if(target == null) throw new NullPointerException();
					lobby++;
					if(target == player.getServer().getInfo()){
						player.sendMessage(ChatColor.RED + "Vous êtes déjà  au Lobby n°" + lobby + " !");
						return;
					}
					player.sendMessage(ChatColor.GREEN + "Téléportation au Lobby n°" + lobby + " en cours.");
					player.connect(target);
				} catch(Exception e){
					player.sendMessage(ChatColor.RED + "Le Lobby n°" + args[0] + " n'existe pas !");
				}
			} else {
				int lobby = BungeeUtils.lobbysServer.indexOf(player.getServer().getInfo().getName());
				lobby++;
				player.sendMessage(ChatColor.RED + "Vous êtes déjà au Lobby n°" + lobby + " !");
				player.sendMessage(ChatColor.RED + "Pour en rejoindre un autre tapez /hub 1~" + BungeeUtils.lobbysServer.size() + ".");
			}
		} else {
			int lobby = BungeeUtils.instance.getRandomLobby();
			ServerInfo target = BungeeUtils.instance.getLobby(lobby);
        	player.connect(target);
		}
	}
}
