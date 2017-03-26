package fr.badblock.bungeecord.plugins.others.modules.abstracts;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Listener;

public abstract class Module implements Listener {
	
	public Module() {
		BungeeCord.getInstance().getPluginManager().registerListener(BadBlockBungeeOthers.getInstance(), this);
	}
	
}
