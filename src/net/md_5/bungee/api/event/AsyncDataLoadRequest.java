package net.md_5.bungee.api.event;

import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.connection.InitialHandler;

@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class AsyncDataLoadRequest extends Event
{

	/**
	 * The connection asking for a ping response.
	 */
	private final String player;

	private final InitialHandler handler;
	
	private final Callback<Result> done;
	
	public AsyncDataLoadRequest(String player, InitialHandler handler, Callback<Result> done)
	{
		this.player = player;
		this.handler = handler;
		this.done   = done;
	}
	
	@AllArgsConstructor
	public static class Result {
		public B object;
		public String 	  kickReason;
	}
}
