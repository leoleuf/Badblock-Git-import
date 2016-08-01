package fr.badblock.ladder.api.commands;

import fr.badblock.ladder.api.entities.CommandSender;
import lombok.Getter;

public abstract class Command {
	@Getter protected String 	command;
	@Getter protected String[]  aliases;
	@Getter protected String 	permission;
	
	public Command(String command, String permission, String...aliases){
		this.command    = command;
		this.permission = permission;
		this.aliases    = aliases;
	}
	
	public Command(String command){
		this.command 	= command;
		this.permission = null;
		this.aliases    = new String[]{};
	}
	
	public abstract void executeCommand(CommandSender sender, String[] args);
	
	public boolean isBypassable(){
		return false;
	}
}
