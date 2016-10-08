package fr.badblock.bungee.utils.crash;

import java.lang.reflect.Method;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.Protocol.DirectionData;

public class CrashUtils {
	public static void registerCrashPacket() throws Exception {
		Class<?> clazz = DirectionData.class;
		
		Method method = clazz.getDeclaredMethod("registerPacket", int.class, Class.class);
		method.invoke(Protocol.GAME.TO_CLIENT, 232, Crash.class);
	}
	
	public static void sendPacket(ProxiedPlayer player){
		player.unsafe().sendPacket(new Crash(""));
	}
}
