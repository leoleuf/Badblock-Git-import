package fr.badblock.bungeecord.plugins.ladder.bungee;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BungeeKeep
{

	public String 			bungeeName;
	public List<String>		players;
	public long				expired;
	
	public boolean isExpired()
	{
		return expired < System.currentTimeMillis();
	}
	
}
