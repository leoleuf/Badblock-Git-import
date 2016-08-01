package fr.badblock.ladder.api.entities;

import fr.badblock.ladder.api.chat.Motd;

public interface BungeeCord extends CommandSender, Server {
	public void sendCommands();
	public void sendServers();
	public void sendMotd(Motd motd);

	public void addBukkitServer(Bukkit bukkit);
	public void removeBukkitServer(Bukkit bukkit);
}
