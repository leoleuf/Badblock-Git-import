package fr.badblock.api.bukkit.listeners.players;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;

import fr.badblock.api.bukkit.listeners.BadListener;

public class PlayerLoginListener extends BadListener
{
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		/*Reflector 			  reflector 	= new Reflector(ReflectionUtils.getHandle(event.getPlayer()));
		BadblockPlayer    	  player        = null;
		try {
			player = new BadblockPlayer((CraftServer) Bukkit.getServer(), (EntityPlayer) reflector.getReflected());
			reflector.setFieldValue("bukkitEntity", player);
		} catch (Exception exception) {
			System.out.println("Impossible de modifier la classe du joueur : ");
			exception.printStackTrace();
		}*/
	}

}
