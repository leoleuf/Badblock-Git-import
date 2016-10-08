package fr.badblock.bungee.utils.party;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fr.badblock.bungee.utils.BungeeUtils;
import fr.badblock.bungee.utils.commands.PartyCommand;
import fr.badblock.bungee.utils.utils.ChatUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartiesManager {
	private static PartiesManager instance;
	public static PartiesManager getInstance(){
		return instance;
	}
	
	private Map<UUID, UUID> partiesMember;
	private Map<UUID, Party> parties;
	
	public PartiesManager(){
		instance = this;
		
		partiesMember = new HashMap<UUID, UUID>();
		parties = new HashMap<UUID, Party>();
		
		ProxyServer.getInstance().getPluginManager().registerListener(BungeeUtils.instance, new PartyListener());
		ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtils.instance, new PartyCommand());
    }
	
	public int count(){
		return parties.size();
	}
	
	public Party getParty(ProxiedPlayer player){
		UUID partyLeader = partiesMember.get(player.getUniqueId());
		if(partyLeader == null) return null;
		
		return parties.get(partyLeader);
	}
	
	public void create(ProxiedPlayer player){
		Party party = new Party(player);
		partiesMember.put(player.getUniqueId(), player.getUniqueId());
		parties.put(player.getUniqueId(), party);
	}
	
	public void end(UUID player){
		parties.remove(player);
	}
	
	public void leave(UUID uniqueId){
		partiesMember.remove(uniqueId);
	}
	
	public void join(UUID uniqueId, UUID leader){
		partiesMember.put(uniqueId, leader);
	}
	
	public void sendHelp(ProxiedPlayer player){
		ChatUtils.sendMessagePlayer(player, 
				"%yellow%[%aqua%BadBlock-Party%yellow%] %red%Aide",
				"%red% > %aqua%/%yellow%party create %aqua%> %yellow%créé une partie",
				"%red% > %aqua%/%yellow%party disband %aqua%> %yellow%arrête la partie",
				"%red% > %aqua%/%yellow%party invite %red%player %aqua%> %yellow%invite un joueur à la partie",
				"%red% > %aqua%/%yellow%party kick %red%player %aqua%> %yellow%kick un joueur de la partie",
				"%red% > %aqua%/%yellow%party join %red%party %aqua%> %yellow%rejoindre une partie",
				"%red% > %aqua%/%yellow%party leave %aqua%> %yellow%quitte la partie",
				"%red% > %aqua%/%yellow%party list %red%player %aqua%> %yellow%fait la liste des joueurs de l'équipe",
				"%red% > %aqua%& %yellow%devant les messages pour parler avec les joueurs de votre partie"
			);
	}
}
