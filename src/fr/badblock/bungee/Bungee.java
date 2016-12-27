package fr.badblock.bungee;

import java.util.List;

import fr.badblock.bungee.data.players.BadPlayer;
import lombok.Data;

@Data public class Bungee {

	public String 			bungeeName;
	public long   			keepAlive;
	public List<BadPlayer>  players;
	
	public boolean isAvailable() {
		return keepAlive > System.currentTimeMillis();
	}
	
	
}
