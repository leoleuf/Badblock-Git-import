package net.md_5.bungee.api.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.connection.InitialHandler;

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
