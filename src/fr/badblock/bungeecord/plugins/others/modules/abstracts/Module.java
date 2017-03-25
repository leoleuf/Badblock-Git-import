package fr.badblock.bungeecord.plugins.others.modules.abstracts;

import fr.badblock.bungeecord.BungeeCord;
import fr.badblock.bungeecord.api.plugin.Listener;
import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;

public abstract class Module implements Listener {
	
	public Module() {
		BungeeCord.getInstance().getPluginManager().registerListener(BadBlockBungeeOthers.getInstance(), this);
	}
	
}
