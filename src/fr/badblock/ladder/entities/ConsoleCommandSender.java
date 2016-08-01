package fr.badblock.ladder.entities;

import java.util.logging.Level;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.CommandSender;

public class ConsoleCommandSender implements CommandSender {
	@Override
	public String getName() {
		return "CONSOLE";
	}

	@Override
	public boolean hasPermission(String permission) {
		return true;
	}

	@Override
	public void sendMessages(String... messages) {
		for(String message : messages)
			Ladder.getInstance().getLogger().log(Level.INFO, message);
	}

	@Override
	public void sendMessage(String message) {
		sendMessages(message);
	}

	@Override
	public void forceCommand(String... commands) {
		for(String command : commands){
			Ladder.getInstance().getPluginsManager().executeCommand(this, command);
		}
	}
}
