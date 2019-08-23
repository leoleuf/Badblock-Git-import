package fr.badblock.api.listener;

import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.data.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvent implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event){
        String player = event.getName();
        BadBlockAPI.getPluginInstance().getPlayerManager().loadPlayer(player);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event){
        BadBlockAPI.getPluginInstance().getPlayerManager().getPlayerData(event.getPlayer().getName()).setIP(event.getAddress().getHostAddress());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PlayerData playerData = BadBlockAPI.getPluginInstance().getPlayerManager().getPlayerData(player.getName());
        playerData.setName(player.getName());
        playerData.setPlayerID(player.getUniqueId());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        BadBlockAPI.getPluginInstance().getPlayerManager().unloadPlayer(player.getName());
    }
}
