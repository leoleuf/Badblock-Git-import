package fr.badblock.ladder.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.Bukkit;
import fr.badblock.ladder.api.entities.BungeeCord;
import fr.badblock.ladder.entities.LadderBukkit;
import fr.badblock.ladder.entities.LadderBungee;
import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.Protocol;
import fr.badblock.protocol.socket.SocketHost;

public class LadderSocketHost extends SocketHost {
	public LadderSocketHost(InetAddress address, int port) throws IOException {
		super(address, port);
	}
	
	@Override
	public Protocol getOutputProtocol(InetSocketAddress trueAddress, InetSocketAddress serverAddress) {
		if(!trueAddress.getHostName().equals(serverAddress.getHostName()))
			return null;
		
		BungeeCord bungeeCord = Ladder.getInstance().getServer(serverAddress);
		if(bungeeCord != null)
			return Protocol.LADDER_TO_BUNGEE;
		
		Bukkit	   bukkit     = Ladder.getInstance().getBukkitServer(serverAddress);
		
		if(bukkit != null)
			return Protocol.LADDER_TO_BUKKIT;
		
		return null;
	}

	@Override
	public Protocol getInputProtocol(InetSocketAddress trueAddress, InetSocketAddress serverAddress) {
		if(!trueAddress.getHostName().equals(serverAddress.getHostName()))
			return null;
		
		BungeeCord bungeeCord = Ladder.getInstance().getServer(serverAddress);
		if(bungeeCord != null)
			return Protocol.BUNGEE_TO_LADDER;
		
		Bukkit	   bukkit     = Ladder.getInstance().getBukkitServer(serverAddress);
		
		if(bukkit != null)
			return Protocol.BUKKIT_TO_LADDER;
		
		return null;
	}

	@Override
	public PacketHandler createHandler(InetSocketAddress trueAddress, InetSocketAddress serverAddress) {
		if(!trueAddress.getHostName().equals(serverAddress.getHostName()))
			return null;
		
		BungeeCord bungeeCord = Ladder.getInstance().getServer(serverAddress);
		if(bungeeCord != null)
			return (LadderBungee) bungeeCord;
		
		Bukkit	   bukkit     = Ladder.getInstance().getBukkitServer(serverAddress);
		
		if(bukkit != null)
			return (LadderBukkit) bukkit;
		
		return null;
	}
}
