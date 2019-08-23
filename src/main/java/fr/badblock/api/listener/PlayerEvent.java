package fr.badblock.api.listener;

import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.data.player.PlayerData;
import fr.badblock.api.data.rank.RankData;
import fr.badblock.api.utils.TeamTag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

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
        RankData rankData = BadBlockAPI.getPluginInstance().getRankManager().getRankData(playerData.getRankID());
        playerData.setName(player.getName());
        playerData.setPlayerID(player.getUniqueId().toString());
        playerData.setOnline(true);
        try {
            TeamTag teamTag = new TeamTag(rankData.getRankName(), rankData.getRankPrefix(), rankData.getRankSuffix());
            teamTag.set(player);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        PlayerData playerData = BadBlockAPI.getPluginInstance().getPlayerManager().getPlayerData(player.getName());
        playerData.setOnline(false);
        BadBlockAPI.getPluginInstance().getPlayerManager().unloadPlayer(player.getName());
    }



}
