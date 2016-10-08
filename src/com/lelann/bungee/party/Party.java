package com.lelann.bungee.party;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.lelann.bungee.utils.ChatUtils;

public class Party {
	public static int MAX_PLAYERS_IN_PARTY = 8;
	private List<UUID> members = new ArrayList<UUID>();
	private List<UUID> invitedPlayers = new ArrayList<UUID>();
	private UUID leader;
	private boolean inGame = false;
	
	public boolean inGame(){
		return inGame;
	}
	
	public void inGame(boolean inGame){
		this.inGame = inGame;
	}
	
	public void sendMessage(String... messages){
		for(UUID uniqueId : members){
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uniqueId);
			if(player != null)
				ChatUtils.sendMessagePlayer(player, messages);
		}
	}
	
	public Party(ProxiedPlayer leader){
		this.leader = leader.getUniqueId();
		this.members.add(leader.getUniqueId());
	}
	
	public List<UUID> getMembers(){
		return members;
	}
	
	public void enter(ProxiedPlayer player){
		if(members.size() >= MAX_PLAYERS_IN_PARTY){
			ChatUtils.sendMessagePlayer(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Les parties sont limitées à 8 joueurs. Tu ne peux pas entrer ! :(");
		} else {
			sendMessage("%yellow%[%aqua%BadBlock-Party%yellow%] %red%" + player.getName() + " %yellow%a rejoint la partie ! =D");
			ChatUtils.sendMessagePlayer(player, "%green%Tu as rejoint la partie ! :)");
			
			PartiesManager.getInstance().join(player.getUniqueId(), leader);
			members.add(player.getUniqueId());
			invitedPlayers.remove(player.getUniqueId());
		}
	}
	
	public void kick(ProxiedPlayer player){
		members.remove(player.getUniqueId());
		PartiesManager.getInstance().leave(player.getUniqueId());
		sendMessage("%yellow%[%aqua%BadBlock-Party%yellow%] %red%" + player.getName() + " a été éjécté de la partie !");
		ChatUtils.sendMessagePlayer(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Vous avez été éjécté de votre partie !");
	}
	
	public void quit(ProxiedPlayer player){
		members.remove(player.getUniqueId());
		PartiesManager.getInstance().leave(player.getUniqueId());
		sendMessage("%yellow%[%aqua%BadBlock-Party%yellow%] %red%" + player.getName() + " a quitté la partie !");
		ChatUtils.sendMessagePlayer(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Vous avez quitté votre partie !");
	}
	
	public void disband(){
		sendMessage("%yellow%[%aqua%BadBlock-Party%yellow%] %red%La partie a été supprimée !");
		for(UUID uniqueId : members){
			PartiesManager.getInstance().leave(uniqueId);
		}
		
		PartiesManager.getInstance().end(leader);
	}
	
	public void invite(ProxiedPlayer player, ProxiedPlayer by){
		ChatUtils.sendMessagePlayer(player, "%yellow%[%aqua%BadBlock-Party%yellow%] Vous avez été invité dans une Partie par " + by.getName(),
				"%yellow%[%aqua%BadBlock-Party%yellow%] Tape /party join " + by.getName() + " pour la rejoindre.");
		sendMessage("%yellow%[%aqua%BadBlock-Party%yellow%] %red%" + player.getName() + " %yellow%a été invité dans la Partie !");
		invitedPlayers.add(player.getUniqueId());
	}

	public void disconnect(ProxiedPlayer player){
		sendMessage("%yellow%[%aqua%BadBlock-Party%yellow%] %red%" + player.getName() + " a quitté la partie (déconnexion).");
		if(isLeader(player)){
			disband();
		}
	}
	
	public boolean isMember(ProxiedPlayer player){
		return members.contains(player.getUniqueId());
	}
	
	public boolean isLeader(ProxiedPlayer player){
		return leader.equals(player.getUniqueId());
	}
	
	public boolean isInvited(ProxiedPlayer player){
		return invitedPlayers.contains(player.getUniqueId());
	}
}
