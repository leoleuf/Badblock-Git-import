package com.lelann.bungee.party;

import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import com.lelann.bungee.BungeeUtils;
import com.lelann.bungee.utils.ChatUtils;

public class PartyListener implements Listener {
	@EventHandler
	public void onDisconnect(PlayerDisconnectEvent e){
		Party party = PartiesManager.getInstance().getParty(e.getPlayer());
		
		if(party != null)
			party.disconnect(e.getPlayer());
	}
	
	@EventHandler
	public void onServerSwitch(ServerConnectEvent e){
		boolean minigameServer = false;
		
		for(String minigame : BungeeUtils.minigamesServer){
			if(e.getTarget().getName().startsWith(minigame)){
				minigameServer = true; break;
			}
		}
		
		if(minigameServer){
			final Party party = PartiesManager.getInstance().getParty(e.getPlayer());
			if(party == null){
				return;
			} else if(!party.isLeader(e.getPlayer()) && !party.inGame()){
				ChatUtils.sendMessagePlayer(e.getPlayer(), "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Vous n'êtes pas leader de votre partie, vous ne pouvez pas entrer dans un jeu. /party leave pour quitter votre partie.");
				e.setCancelled(true);
			} else if(party.isLeader(e.getPlayer())){
				party.inGame(true);
				for(UUID member : party.getMembers()){
					ProxiedPlayer theMember = ProxyServer.getInstance().getPlayer(member);
					if(theMember == null || member.equals(e.getPlayer().getUniqueId())
							|| theMember.getServer().getInfo().getName().startsWith("login")
							|| theMember.getServer().getInfo().getName().equals("login"))
						continue;
					
					theMember.connect(e.getTarget());
				}
				new Thread(){
					@Override
					public void run(){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						party.inGame(false);
					}
				}.start();
			}
		}
	}
	
	@EventHandler
	public void onSpeak(ChatEvent e){
		if(e.getMessage().startsWith("&") && e.getSender() instanceof ProxiedPlayer){
			Party party = PartiesManager.getInstance().getParty((ProxiedPlayer) e.getSender());
			if(party != null){
				String message = "%gray%[Party] " + ((ProxiedPlayer)e.getSender()).getName() + " > " + ChatUtils.colorDelete(e.getMessage().substring(1, e.getMessage().length()));
				party.sendMessage(message);
				e.setCancelled(true);
			}
		}
	}
}
