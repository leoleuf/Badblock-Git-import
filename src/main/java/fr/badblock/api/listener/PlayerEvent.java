package fr.badblock.api.listener;

import fr.badblock.api.BadBlockAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        BadBlockAPI.getPluginInstance().getPlayerManager().loadPlayer(player.getName());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        BadBlockAPI.getPluginInstance().getPlayerManager().unloadPlayer(player.getName());
    }
}
