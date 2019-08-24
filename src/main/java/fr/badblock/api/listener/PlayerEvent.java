package fr.badblock.api.listener;

import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.data.player.PlayerBean;
import fr.badblock.api.data.player.PlayerData;
import fr.badblock.api.data.rank.RankData;
import fr.badblock.api.utils.TeamTag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerEvent implements Listener {

    private BadBlockAPI badBlockAPI;
    public PlayerEvent(BadBlockAPI badBlockAPI){
        this.badBlockAPI = badBlockAPI;
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        String player = event.getName();
        String uuid = event.getUniqueId().toString();
        if(badBlockAPI.getPlayerDataManager().getPlayer(uuid) != null){
            if(!badBlockAPI.getPlayerDataManager().getPlayer(uuid).getNormalName().equals(player)){
                badBlockAPI.getPlayerDataManager().updatePlayer(badBlockAPI.getPlayerDataManager().getPlayer(uuid));
            }
        }
        badBlockAPI.getPlayerManager().loadPlayer(player);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        String name = event.getPlayer().getName();
        PlayerData playerData = badBlockAPI.getPlayerManager().getPlayerData(name);
        String ip = event.getAddress().getHostAddress();

        if(playerData.getIP() == null) {
           playerData.setIP(ip);
        }else if(!playerData.getIP().equals(ip)){
            playerData.setIP(ip);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = badBlockAPI.getPlayerManager().getPlayerData(player.getName());
        RankData rankData = badBlockAPI.getRankManager().getRankData(playerData.getRankID());

        if(playerData.getNormalName() == null) {
            playerData.setNormalName(player.getName());
        }
        if (playerData.getPlayerID() == null) {
            playerData.setPlayerID(player.getUniqueId().toString());
        }
        playerData.setOnline(true);

        try {
            TeamTag teamTag = new TeamTag(rankData.getRankName(), rankData.getRankTag(), rankData.getRankSuffix());
            teamTag.set(player);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = badBlockAPI.getPlayerManager().getPlayerData(player.getName());
        playerData.setOnline(false);
        badBlockAPI.getPlayerManager().unloadPlayer(player.getName());
    }


}
