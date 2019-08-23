package fr.badblock.api.chat;

import fr.badblock.api.BadBlockAPI;
import org.bukkit.ChatColor;

public class ChatUtilities {
    /** Utilities class adapted to Chat class **/

    /** Use it to replace '§' to '&' on messaging **/
        public static String f(String txt) { return ChatColor.translateAlternateColorCodes('&', txt); }
}
