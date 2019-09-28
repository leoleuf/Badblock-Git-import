package fr.badblock.api.listener;

import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.data.player.PlayerBean;
import fr.badblock.api.data.player.PlayerData;
import fr.badblock.api.blacklist.BlackList;
import fr.badblock.api.data.rank.RankData;
import fr.badblock.api.utils.TeamTag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerEvent implements Listener {

    private BadBlockAPI badBlockAPI;

    public PlayerEvent(BadBlockAPI badBlockAPI) {
        this.badBlockAPI = badBlockAPI;
    }
    BlackList blackList = new BlackList();

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        String player = event.getName();
        String uuid = event.getUniqueId().toString();
        //String ip = event.getAddress().getHostAddress();
        if (badBlockAPI.getPlayerDataManager().getPlayer(uuid) != null) {
            if (badBlockAPI.getPlayerDataManager().getPlayer(uuid).getPlayerName() != null) {
                if (!badBlockAPI.getPlayerDataManager().getPlayer(uuid).getPlayerName().toLowerCase().equals(player.toLowerCase())) {
                    PlayerBean playerBean = badBlockAPI.getPlayerDataManager().getPlayer(uuid);
                    playerBean.setPlayerName(player.toLowerCase());
                    badBlockAPI.getPlayerDataManager().updatePlayer(playerBean);
                }
            }
        }
        badBlockAPI.getPlayerManager().loadPlayer(player);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        String name = event.getPlayer().getName();
        PlayerData playerData = badBlockAPI.getPlayerManager().getPlayerData(name);
        String ip = event.getAddress().getHostAddress();

        if (playerData.getIP() == null) {
            playerData.setIP(ip);
        } else if (!playerData.getIP().equals(ip)) {
            playerData.setIP(ip);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = badBlockAPI.getPlayerManager().getPlayerData(player.getName());
        RankData rankData = badBlockAPI.getRankManager().getRankData(playerData.getRankID());

        if (playerData.getNormalName() == null) {
            playerData.setNormalName(player.getName());
        }
        if(!playerData.getNormalName().equals(player.getName())){
            playerData.setNormalName(player.getName());
        }
        if (playerData.getPlayerID() == null) {
            playerData.setPlayerID(player.getUniqueId().toString());
        }
        playerData.setOnline(true);
        playerData.setLastLogin();
        try {
            playerData.setBukkitPermissions();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            TeamTag teamTag = new TeamTag(rankData.getRankPower()+rankData.getRankName(), rankData.getRankTag() + " ", " "+rankData.getRankSuffix());
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
