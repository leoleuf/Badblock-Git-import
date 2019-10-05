package fr.badblock.tech.protocol;

import fr.badblock.tech.protocol.packets.PacketHelloworld;
import fr.badblock.tech.protocol.packets.PacketLadderStop;
import fr.badblock.tech.protocol.packets.PacketPlayerChat;
import fr.badblock.tech.protocol.packets.PacketPlayerData;
import fr.badblock.tech.protocol.packets.PacketPlayerJoin;
import fr.badblock.tech.protocol.packets.PacketPlayerLogin;
import fr.badblock.tech.protocol.packets.PacketPlayerNickSet;
import fr.badblock.tech.protocol.packets.PacketPlayerPlace;
import fr.badblock.tech.protocol.packets.PacketPlayerQuit;
import fr.badblock.tech.protocol.packets.PacketReconnectionInvitation;
import fr.badblock.tech.protocol.packets.PacketSimpleCommand;
import fr.badblock.tech.protocol.packets.matchmaking.PacketMatchmakingJoin;
import fr.badblock.tech.protocol.packets.matchmaking.PacketMatchmakingKeepalive;
import fr.badblock.tech.protocol.packets.matchmaking.PacketMatchmakingPing;
import fr.badblock.tech.protocol.packets.matchmaking.PacketMatchmakingPong;

public interface PacketHandler {
	void handle(PacketPlayerChat packet);
	void handle(PacketPlayerData packet);
	
	void handle(PacketPlayerLogin packet);
	void handle(PacketPlayerJoin packet);
	void handle(PacketPlayerQuit packet);
	void handle(PacketPlayerNickSet packet);
	void handle(PacketPlayerPlace packet);
	void handle(PacketReconnectionInvitation packet);
	
	void handle(PacketHelloworld packet);
	
	void handle(PacketMatchmakingJoin packet);
	void handle(PacketMatchmakingKeepalive packet);
	void handle(PacketMatchmakingPing packet);
	void handle(PacketMatchmakingPong packet);
	
	void handle(PacketSimpleCommand packet);
	
	void handle(PacketLadderStop packet);
}