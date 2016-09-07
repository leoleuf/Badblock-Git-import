package fr.badblock.ladder.plugins.utils;

import java.util.UUID;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.chat.RawMessage;
import fr.badblock.ladder.api.chat.RawMessage.ClickEventType;
import fr.badblock.ladder.api.chat.RawMessage.HoverEventType;
import fr.badblock.ladder.api.entities.Bukkit;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.protocol.packets.PacketPlayerChat;
import fr.badblock.protocol.packets.PacketPlayerChat.ChatAction;
import lombok.Getter;

@Getter public class Animation extends Thread {
	public static Animation ANIMATION;
	
	private boolean online;
	private Bukkit	server,
					fallback;
	private String	eventName;
	private int		timeBeforeEnd;
	private int		timeBetweenAlerts;
	
	private int		time;
	
	public void close(){
		online = false;
	}
	
	public Animation(Bukkit server, Bukkit fallback, String eventName, int timeBetweenAlerts, int timeBeforeEnd){
		ANIMATION = this;
		
		this.server			   = server;
		this.fallback		   = fallback;
		this.eventName 	   	   = eventName;
		this.timeBeforeEnd 	   = timeBeforeEnd;
		this.timeBetweenAlerts = timeBetweenAlerts;
	
		this.time			   = 0;
		this.online			   = false;
	}
	
	@Override
	public void run(){
		online = true;
		
		RawMessage firstMessage = Ladder.getInstance().createRawMessage(ChatColor.GOLD + "Une animation '" + ChatColor.RED + eventName 
				+ ChatColor.GOLD + "' débute ! ");
		
		RawMessage message = Ladder.getInstance().createRawMessage(ChatColor.GOLD + "Une animation '" + ChatColor.RED + eventName 
				+ ChatColor.GOLD + "' est en cours ! ");
		
		RawMessage endMessage = Ladder.getInstance().createRawMessage(ChatColor.RED + "L'animation '" + ChatColor.RED + eventName 
				+ ChatColor.RED + "' est terminée ! Merci à tous !");
		
		RawMessage clickable = Ladder.getInstance().createRawMessage(ChatColor.GOLD + "Cliquez" + ChatColor.RED + " ici " + ChatColor.GOLD + " pour le rejoindre")
				.setClickEvent(ClickEventType.RUN_COMMAND, false, "/event join")
				.setHoverEvent(HoverEventType.SHOW_TEXT, true, ChatColor.GOLD + "Cliquez ici pour rejoindre l'animation " + ChatColor.RED + eventName);
		
		firstMessage.add(clickable);
		message.add(clickable);
		
		
		broadcast(firstMessage);
		time = timeBetweenAlerts; // pas de message en double
		
		while(online){
			try {
				if(time == 0){
					broadcast(message);
					time = timeBetweenAlerts;
				} else if(timeBeforeEnd == 0){
					online = false;
				}
				
				Thread.sleep(1000L);
				
				time--;
				timeBeforeEnd--;
			} catch(Exception exception){}
		}
		
		broadcast(endMessage);
		
		for(UUID player : server.getPlayers()){
			Player p = Ladder.getInstance().getPlayer(player);
			p.connect(fallback);
		}
		
		ANIMATION = null;
	}
	
	public void broadcast(RawMessage message){
		Ladder.getInstance().broadcast(ChatColor.replaceColor("&8&l«&b&l-&8&l»&m------&f&8&l«&b&l-&8&l»&b &b&lAnimation &8&l«&b&l-&8&l»&m------&f&8&l«&b&l-&8&l»"), " ");
		Ladder.getInstance().broadcastPacket(new PacketPlayerChat(null, ChatAction.MESSAGE_JSON, message.asJsonObject().toString()));
		Ladder.getInstance().broadcast(" ", ChatColor.replaceColor("&8&l«&b&l-&8&l»&m-----------------------------&f&8&l«&b&l-&8&l»&b"));
	}
}
