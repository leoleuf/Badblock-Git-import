package fr.badblock.ladder.api.events.all;

import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.Player;
import lombok.Getter;

public class BukkitCommandEvent extends CommandEvent {
	
	@Getter private final Command command;
	@Getter private final Player player;

	public BukkitCommandEvent(Command command, Player player, String message) {
		super(player, message);
		this.player = player;
		this.command = command;
	}
	
	public BukkitCommandEvent(Command command, String message) {
		super(null, message);
		this.player = null;
		this.command = command;
	}
	
}
