package fr.badblock.security.listeners;

import java.net.InetAddress;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import fr.badblock.security.BadBlockSecurity;

public class PlayerLoginListener implements Listener {

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		InetAddress address = event.getAddress();
		String hostAddress = address.getHostAddress();
		if (!BadBlockSecurity.getInstance().ips.contains(hostAddress)) {
			event.setResult(Result.KICK_OTHER);
			event.setKickMessage("§cNice try. But, bye.");
		}
	}
	
}
