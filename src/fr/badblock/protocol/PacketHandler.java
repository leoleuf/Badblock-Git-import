package fr.badblock.protocol;

import fr.badblock.protocol.packets.PacketHelloworld;
import fr.badblock.protocol.packets.PacketLadderStop;
import fr.badblock.protocol.packets.PacketPlayerChat;
import fr.badblock.protocol.packets.PacketPlayerData;
import fr.badblock.protocol.packets.PacketPlayerJoin;
import fr.badblock.protocol.packets.PacketPlayerLogin;
import fr.badblock.protocol.packets.PacketPlayerNickSet;
import fr.badblock.protocol.packets.PacketPlayerPlace;
import fr.badblock.protocol.packets.PacketPlayerQuit;
import fr.badblock.protocol.packets.PacketReconnectionInvitation;
import fr.badblock.protocol.packets.PacketSimpleCommand;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingJoin;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingKeepalive;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingPing;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingPong;

public interface PacketHandler {
	public void handle(PacketPlayerChat  			packet);
	public void handle(PacketPlayerData  			packet);
	
	public void handle(PacketPlayerLogin 			packet);
	public void handle(PacketPlayerJoin  			packet);
	public void handle(PacketPlayerQuit  			packet);
	public void handle(PacketPlayerNickSet			packet);
	public void handle(PacketPlayerPlace 			packet);
	public void handle(PacketReconnectionInvitation packet);
	
	public void handle(PacketHelloworld  			packet);
	
	public void handle(PacketMatchmakingJoin 	  	packet);
	public void handle(PacketMatchmakingKeepalive 	packet);
	public void handle(PacketMatchmakingPing 	  	packet);
	public void handle(PacketMatchmakingPong 	  	packet);
	
	public void handle(PacketSimpleCommand          packet);
	
	public void handle(PacketLadderStop			  	packet);
}