package fr.badblock.bungee.modules.modo.commands.punishments;

import com.google.gson.Gson;

import fr.badblock.bungee.BadBungee;

public class PunishmentManager
{

	public static void handle(String body)
	{
		// Get the main class
		BadBungee badBungee = BadBungee.getInstance();
		// Get Gson object
		Gson gson = badBungee.getGson();
		// Get Punishment packet object
		PunishmentPacket punishmentPacket = gson.fromJson(body, PunishmentPacket.class);

		// If the packet is null
		if (punishmentPacket == null) {
			// Log the error
			System.out.println("Error: Received a null PunishmentPacket (" + body + ")");
			// We stop there
			return;
		}

		// Get the packet type
		PunishmentType punishmentType = punishmentPacket.getPunishmentType();

		// If the packet type is null
		if (punishmentType == null) {
			// Log the error
			System.out.println("Error: Working with a null PunishmentType (" + body + ")");
			// We stop there
			return;
		}

		// Process.
		punishmentPacket.process();
	}

}
