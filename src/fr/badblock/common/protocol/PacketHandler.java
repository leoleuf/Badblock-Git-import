package fr.badblock.common.protocol;

import fr.badblock.common.protocol.matchmaking.PacketMatchmakingJoin;
import fr.badblock.common.protocol.matchmaking.PacketMatchmakingKeepalive;
import fr.badblock.common.protocol.matchmaking.PacketMatchmakingPing;
import fr.badblock.common.protocol.matchmaking.PacketMatchmakingPong;
import fr.badblock.common.protocol.packets.PacketHelloworld;
import fr.badblock.common.protocol.packets.PacketLadderStop;
import fr.badblock.common.protocol.packets.PacketPlayerChat;
import fr.badblock.common.protocol.packets.PacketPlayerData;
import fr.badblock.common.protocol.packets.PacketPlayerJoin;
import fr.badblock.common.protocol.packets.PacketPlayerLogin;
import fr.badblock.common.protocol.packets.PacketPlayerNickSet;
import fr.badblock.common.protocol.packets.PacketPlayerPlace;
import fr.badblock.common.protocol.packets.PacketPlayerQuit;
import fr.badblock.common.protocol.packets.PacketReconnectionInvitation;
import fr.badblock.common.protocol.packets.PacketSimpleCommand;

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