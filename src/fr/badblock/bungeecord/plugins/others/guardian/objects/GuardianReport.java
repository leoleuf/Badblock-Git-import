package fr.badblock.bungeecord.plugins.others.guardian.objects;

import java.util.UUID;

public class GuardianReport {

	private String message;
	private UUID uuid;

	public GuardianReport(UUID uuid, String message) {
		this.message = message;
		this.uuid = uuid;
	}

	public UUID getUniqueId() {
		return this.uuid;
	}

	public String getMessage() {
		return this.message;
	}

}
