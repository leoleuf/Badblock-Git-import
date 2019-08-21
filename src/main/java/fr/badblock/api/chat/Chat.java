package fr.badblock.api.chat;

import fr.badblock.api.BadBlockAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        /** Setting str as the string present in a config.yml file when the chat is muted **/
        String str = BadBlockAPI.getPluginInstance().getConfig().getString("chat.messageondisabled");
        /** Cancelling chat event when the chat is disabled **/
        if(ChatUtilities.isActivated == false){
            p.sendMessage(ChatUtilities.f(str));
            e.setCancelled(true);
        } else {
            /** Else, we don't care about it **/
            e.setCancelled(false);
        }
    }
}
