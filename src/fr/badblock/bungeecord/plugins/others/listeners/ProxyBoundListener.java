package fr.badblock.bungeecord.plugins.others.listeners;

import net.md_5.bungee.api.event.ProxyUnableToBindEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ProxyBoundListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onProxyUnableToBind(ProxyUnableToBindEvent event) {
		System.out.println("Unable to bind!");
		if (event.getThrowable() != null)
			event.getThrowable().printStackTrace();
		System.exit(-1);
	}

}
