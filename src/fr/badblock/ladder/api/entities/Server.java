package fr.badblock.ladder.api.entities;

import java.util.Collection;
import java.util.UUID;

public interface Server extends Connection {
	public String		    getName();
	public void 	  	    sendPermissions();
	public Collection<UUID> getPlayers();
	public void				broadcast(String... message);
}
