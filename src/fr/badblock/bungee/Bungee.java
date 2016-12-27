package fr.badblock.bungee;

import java.util.Collection;

import fr.badblock.bungee.data.players.BadPlayer;
import lombok.Data;

@Data public class Bungee {

	public String 				  bungeeName;
	public long   			      keepAlive;
	public Collection<BadPlayer>  players;
	
	public Bungee(String bungeeName, Collection<BadPlayer> players) {
		this.bungeeName = bungeeName;
		this.keepAlive = System.currentTimeMillis() + 30_000L;
		this.players = players;
	}
	
	public boolean isAvailable() {
		return keepAlive > System.currentTimeMillis();
	}
	
}
