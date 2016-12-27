package fr.badblock.bungee.listeners;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.data.players.BadPlayer;
import net.md_5.bungee.api.event.AsyncDataLoadRequest;
import net.md_5.bungee.api.event.AsyncDataLoadRequest.Result;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListener implements Listener {

	@EventHandler
	public void onJoin(AsyncDataLoadRequest e) {
		BadPlayer badPlayer = new BadPlayer(e.getPlayer(), null, e.getHandler().getAddress().getAddress());
		BadBungee.getInstance().keepAlive();
		e.getDone().done(new Result(badPlayer.createResultObject(), null), null);
	}
	
	@EventHandler
	public void onLogin(PostLoginEvent event) {
		System.out.println(event.getPlayer().getName());
	}

}
