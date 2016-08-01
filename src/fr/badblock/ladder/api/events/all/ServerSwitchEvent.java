package fr.badblock.ladder.api.events.all;

import fr.badblock.ladder.api.entities.Bukkit;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.events.PlayerEvent;
import lombok.Getter;

@Getter public class ServerSwitchEvent extends PlayerEvent {
	private final   Bukkit from;
	private final   Bukkit to;
	
	public ServerSwitchEvent(Player player, Bukkit from, Bukkit to) {
		super(player);
		this.from = from;
		this.to   = to;
	}
}
