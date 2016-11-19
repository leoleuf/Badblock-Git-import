package fr.badblock.protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.badblock.protocol.buffers.ByteInputStream;
import fr.badblock.protocol.buffers.ByteOutputStream;
import fr.badblock.protocol.packets.Packet;
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

public abstract class Protocol {
	public static final Protocol LADDER_TO_BUNGEE = new Protocol(){
		{
			addPacket(0, PacketHelloworld.class);
			addPacket(1, PacketPlayerChat.class);
			addPacket(2, PacketPlayerData.class);
			addPacket(3, PacketPlayerPlace.class);
			addPacket(4, PacketPlayerLogin.class);
			addPacket(5, PacketPlayerJoin.class);
			addPacket(6, PacketPlayerQuit.class);
			addPacket(7, PacketLadderStop.class);
			addPacket(8, PacketPlayerNickSet.class);
		}
	};
	
	public static final Protocol BUNGEE_TO_LADDER = new Protocol(){
		{
			addPacket(0, PacketHelloworld.class);
			addPacket(1, PacketPlayerChat.class);
			addPacket(2, PacketPlayerData.class);
			addPacket(3, PacketPlayerPlace.class);
			addPacket(4, PacketPlayerLogin.class);
			addPacket(5, PacketPlayerJoin.class);
			addPacket(6, PacketPlayerQuit.class);
			addPacket(8, PacketPlayerNickSet.class);
		}
	};
	
	public static final Protocol BUKKIT_TO_LADDER = new Protocol(){
		{
			addPacket(1, PacketMatchmakingKeepalive.class);
			addPacket(2, PacketMatchmakingJoin.class);
			addPacket(3, PacketMatchmakingPing.class);

			addPacket(4, PacketReconnectionInvitation.class);

			addPacket(5, PacketPlayerData.class);
			
			addPacket(10, PacketSimpleCommand.class);
			addPacket(8, PacketPlayerNickSet.class);
		}
	};
	
	public static final Protocol LADDER_TO_BUKKIT = new Protocol(){
		{
			addPacket(1, PacketMatchmakingPong.class);
		
			addPacket(5, PacketPlayerData.class);
			addPacket(6, PacketLadderStop.class);
			addPacket(8, PacketPlayerNickSet.class);
		}
	};
	
	private Map<Integer, Class<? extends Packet>> packets;
	
	private Protocol(){
		packets = new HashMap<Integer, Class<? extends Packet>>();	
	}
	
	public void addPacket(int packetId, Class<? extends Packet> packet){
		packets.put(packetId, packet);
	}
	
	public int getIdByClass(Class<? extends Packet> packet){
		for(Entry<Integer, Class<? extends Packet>> entries : packets.entrySet()){
			if(entries.getValue().equals(packet))
				return entries.getKey();
		}
		
		return 0;
	}
	
	public Class<? extends Packet> getClassById(int id){
		return packets.get(id);
	}
	
	public Packet getPacketById(int id) throws Exception {
		Class<? extends Packet> clazz = getClassById(id);
		return clazz.getConstructor().newInstance();
	}
	
	public void readPacket(ByteInputStream input, final PacketHandler handler) throws Exception {
		ProtocolThreading.getInstance().readPacket(input, handler, this);
	}
	
	public void writePacket(ByteOutputStream output, Packet packet) throws Exception {
		output.writeInt(getIdByClass(packet.getClass()));
		packet.write(output);
	}
}