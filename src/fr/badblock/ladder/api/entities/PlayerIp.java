package fr.badblock.ladder.api.entities;

import java.util.Collection;
import java.util.UUID;

import fr.badblock.ladder.api.data.DataHandler;
import fr.badblock.ladder.api.utils.Punished;

public interface PlayerIp extends DataHandler {
	public Punished 		getAsPunished();
	public void				savePunishions();
	public String       	getIp();
	public Collection<UUID> getCurrentPlayers();
	public void				sendToServers();
}
