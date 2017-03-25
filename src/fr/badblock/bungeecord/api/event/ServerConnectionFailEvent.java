package fr.badblock.bungeecord.api.event;

import fr.badblock.bungeecord.api.config.ServerInfo;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.plugin.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class ServerConnectionFailEvent extends Event {
	/**
	 * Player whom the server is for.
	 */
	private final ProxiedPlayer player;
	/**
	 * The server itself.
	 */
	private final ServerInfo fallback;
	
	private final boolean kick;
}
