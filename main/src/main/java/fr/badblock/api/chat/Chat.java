package fr.badblock.api.chat;

import fr.badblock.api.BadBlockAPI;
import fr.badblock.api.data.player.PlayerData;
import fr.badblock.api.data.rank.RankData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        PlayerData playerData = BadBlockAPI.getPluginInstance().getPlayerManager().getPlayerData(p.getName());
        RankData rankData = BadBlockAPI.getPluginInstance().getRankManager().getRankData(playerData.getRankID());
        /** Setting str as the string present in a config.yml file when the chat is muted **/
        String str = BadBlockAPI.getPluginInstance().getConfig().getString("chat.messageondisabled");
        /** Cancelling chat event when the chat is disabled **/
        if (!ChatCommand.isActivated) {
            p.sendMessage(ChatUtilities.f(str));
            e.setCancelled(true);
        } else {
            /** Else, we don't care about it **/
            e.setCancelled(false);
            if (rankData.isStaff()) {
                e.setFormat(ChatUtilities.f(rankData.getRankPrefix() + rankData.getRankName()) + " " + playerData.getNormalName() + " " + ChatUtilities.f(rankData.getRankSuffix()) + ChatColor.DARK_GRAY + "> " + ChatColor.WHITE + ChatUtilities.f(e.getMessage()));
            } else {
                e.setFormat(ChatUtilities.f(rankData.getRankPrefix() + rankData.getRankName()) + " " + playerData.getNormalName() + " " + ChatUtilities.f(rankData.getRankSuffix()) + ChatColor.DARK_GRAY + "> " + ChatColor.WHITE + e.getMessage());
            }
        }
    }
}
