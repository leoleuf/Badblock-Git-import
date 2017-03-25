package fr.badblock.bungeecord.api.event;

import fr.badblock.bungeecord.api.plugin.Event;
import fr.badblock.bungeecord.connection.InitialHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class LoginFailEvent extends Event
{

	/**
	 * The connection asking for a ping response.
	 */
	private final InitialHandler handler;
}
