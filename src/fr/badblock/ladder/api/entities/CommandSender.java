package fr.badblock.ladder.api.entities;

public interface CommandSender {
	public String getName();
	
	public boolean hasPermission(String permission);
	
	public void sendMessage(String message);
	
	public void sendMessages(String... messages);
	
	public void forceCommand(String... commands);
}
