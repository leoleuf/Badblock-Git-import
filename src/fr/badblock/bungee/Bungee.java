package fr.badblock.bungee;

import java.util.Collection;

import com.google.gson.annotations.Expose;

import fr.badblock.bungee.data.players.BadPlayer;
import lombok.Data;

@Data public class Bungee {

	@Expose public String 				  bungeeName;
	@Expose public long   			      keepAlive;
	@Expose public Collection<BadPlayer>  players;
	
	public Bungee(String bungeeName, Collection<BadPlayer> players) {
		this.bungeeName = bungeeName;
		this.keepAlive = System.currentTimeMillis() + 30_000L;
		this.players = players;
	}
	
	public boolean isAvailable() {
		return keepAlive > System.currentTimeMillis();
	}
	
}
