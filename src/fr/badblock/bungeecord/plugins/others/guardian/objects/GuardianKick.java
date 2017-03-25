package fr.badblock.bungeecord.plugins.others.guardian.objects;

import java.util.UUID;

public class GuardianKick {
	
	private UUID uuid;
	private String message;
	
	public GuardianKick(UUID uuid, String message) {
		this.uuid = uuid;
		this.message = message;
	}
	
	public UUID getUniqueId() {
		return this.uuid;
	}
	
	public String getMessage() {
		return this.message;
	}
	
}
