package fr.badblock.bungee;

import java.util.ArrayList;
import java.util.List;

import fr.badblock.bungee.data.players.BadPlayer;
import lombok.Data;

@Data public class Bungee {

	public String 			bungeeName;
	public long   			keepAlive;
	public List<BadPlayer>  players;
	
	public Bungee(String bungeeName) {
		this.bungeeName = bungeeName;
		this.keepAlive = System.currentTimeMillis() + 30_000L;
		this.players = new ArrayList<>();
	}
	
	public boolean isAvailable() {
		return keepAlive > System.currentTimeMillis();
	}
	
}
