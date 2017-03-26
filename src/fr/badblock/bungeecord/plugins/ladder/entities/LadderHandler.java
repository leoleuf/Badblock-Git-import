package fr.badblock.bungeecord.plugins.ladder.entities;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import fr.badblock.common.protocol.PacketHandler;
import fr.badblock.common.protocol.Protocol;
import fr.badblock.common.protocol.packets.Packet;
import fr.badblock.common.protocol.socket.SocketHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.BungeeCord;

@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
public class LadderHandler {
	private final InetSocketAddress host;
	private final PacketHandler     handler;
	private final int				port;
	private final String		    ip;

	private boolean 				closed = false;
	
	private SocketHandler 	  		socketHandler;

	public LadderHandler(InetSocketAddress host, PacketHandler handler, String ip, int port) throws IOException {
		this(host, handler, port, ip);
		socketClosed(false); // on simule la fermeture du socket 
	}
	
	public void sendPacket(Packet packet){
		socketHandler.sendPacket(packet);
	}
	
	public void close() throws IOException {
		closed = true;
		socketHandler.close();
	}

	protected void socketClosed(boolean warn){
		if(closed) return;
		
		if(warn)
			System.out.println("Lost socket ... trying to reconnect.");

		try {
			socketHandler = new LadderSocketHandler(host, handler, ip, port);
		} catch (IOException e) {
			System.out.println("Fatal error : can not connect to Ladder anymore. Ladder is probably off, rebooting (waiting 10 seconds) :");
			try {
				Thread.sleep(5000L);
			} catch (InterruptedException exception){}

			e.printStackTrace();

			BungeeCord.getInstance().stop();
			return;
		}

		try {
			Thread.sleep(2L);
		} catch (InterruptedException e){}
	}

	class LadderSocketHandler extends SocketHandler {
		public LadderSocketHandler(InetSocketAddress host, PacketHandler handler, String ip, int port) throws IOException {
			super(new Socket(host.getAddress(), host.getPort()), Protocol.LADDER_TO_BUNGEE, Protocol.BUNGEE_TO_LADDER, handler, false);

			start();

			getOut().writeUTF(ip);
			getOut().writeInt(port);
		}

		@Override
		public void socketClosed(){
			LadderHandler.this.socketClosed(true);
		}
	}
}