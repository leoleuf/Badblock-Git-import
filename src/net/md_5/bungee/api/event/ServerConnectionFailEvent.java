package net.md_5.bungee.api.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

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
