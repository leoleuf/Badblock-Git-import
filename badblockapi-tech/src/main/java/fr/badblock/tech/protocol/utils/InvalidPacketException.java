package fr.badblock.tech.protocol.utils;

import java.io.IOException;
import java.net.Socket;

import fr.badblock.tech.protocol.packets.Packet;

public class InvalidPacketException extends IOException {
	private static final long serialVersionUID = 1L;
	
	public InvalidPacketException(Packet packet, Throwable e){
		super("Invalid Packet " + packet, e);
	}
	
	public InvalidPacketException(Socket socket, Throwable e){
		super("Invalid Packet received from " + socket.getInetAddress(), e);
	}
}
