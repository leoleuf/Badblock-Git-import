package fr.badblock.ladder.api.events.all;

import fr.badblock.ladder.api.commands.Command;
import lombok.Getter;

public class BukkitCommandEvent extends CommandEvent {
	@Getter private final Command command;
	
	public BukkitCommandEvent(Command command, String message) {
		super(null, message);
		
		this.command = command;
	}
}
