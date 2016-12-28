package fr.badblock.bungee;

import java.util.Collection;

import com.google.gson.annotations.Expose;

import fr.badblock.bungee.data.ip.BadIpData;
import fr.badblock.bungee.data.players.BadPlayer;
import lombok.Data;

@Data public class Bungee {

	@Expose public String 				  bungeeName;
	@Expose public long   			      keepAlive;
	@Expose public Collection<BadPlayer>  players;
	@Expose public Collection<BadIpData>  ips;
	
	public Bungee(String bungeeName, Collection<BadPlayer> players, Collection<BadIpData> ips) {
		this.bungeeName = bungeeName;
		this.keepAlive = System.currentTimeMillis() + 30_000L;
		this.players = players;
		this.ips = ips;
	}
	
	public boolean isAvailable() {
		return keepAlive > System.currentTimeMillis();
	}
	
}
