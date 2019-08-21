package fr.badblock.api.chat;

import fr.badblock.api.BadBlockAPI;
import org.bukkit.ChatColor;

class ChatUtilities {
    /**
     * Utilities class adapted to Chat class
     **/
    static boolean isActivated = BadBlockAPI.getPluginInstance().getConfig().getBoolean("chat.enabled");

    /**
     * Use it to replace '§' to '&' on messaging
     **/
    static String f(String txt) {
        return ChatColor.translateAlternateColorCodes('&', txt);
    }
}
