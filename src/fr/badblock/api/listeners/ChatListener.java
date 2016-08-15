package fr.badblock.api.listeners;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.badblock.api.MJPlugin;
import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.permissions.AbstractPermissions;
import fr.badblock.api.utils.bukkit.ChatUtils;
import fr.badblock.api.utils.bukkit.SoundUtils;

public class ChatListener implements Listener {
	private static final AbstractPermissions permissions = AbstractPermissions.getPermissions();
	private Map<UUID, Long> messages = new HashMap<UUID, Long>();

	@EventHandler(priority = EventPriority.NORMAL)
	public void onSpeak(AsyncPlayerChatEvent e){
		BPlayer player = BPlayersManager.getInstance().getPlayer(e.getPlayer());
		if(player.isSpectator()){
			if (!player.isVip()) {
				if (this.messages.containsKey(e.getPlayer().getUniqueId())) {
					if (((Long)this.messages.get(e.getPlayer().getUniqueId())).longValue() > new Date().getTime()) {
						ChatUtils.sendMessagePlayer(e.getPlayer(), "%red%Vous ne pouvez parler qu'une fois toutes les 15 secondes.");
						e.setCancelled(true);
						return;
					} else {
						this.messages.put(e.getPlayer().getUniqueId(), Long.valueOf(new Date().getTime() + 15 * 1000));
					}
				}
				else this.messages.put(e.getPlayer().getUniqueId(), Long.valueOf(new Date().getTime() + 15 * 1000));
			}
		}
		String message = format(e.getMessage(), e.getPlayer());
		if(message != null){
			for(Player p : e.getRecipients()){
				String toSend = message;
				
				if(!p.equals(e.getPlayer()) && toSend.toLowerCase().contains(p.getName().toLowerCase())){
					toSend = toSend.replaceAll("(?i)" + p.getName(), "%bold%&b" + p.getName() + "&r");
					SoundUtils.playSound(p, Sound.NOTE_PIANO);
				}
				
				ChatUtils.sendMessagesPlayer(p, toSend);
			}
			e.setCancelled(true);
		}
	}
	public static String format(String base, Player p){
		try {
			BPlayer player = BPlayersManager.getInstance().getPlayer(p);
			String message = "%dred%[%red%Lvl %green%" + player.getLevel() + "%dred%] ";

			message += permissions.getPrefix(p);
			message += permissions.getSuffix(p);

			if(player == null || player.getTeam() == null || player.isSpectator()){
				if(MJPlugin.getInstance().getGameName().equalsIgnoreCase("lobby"));
				else if(player.isSpectator())
					message += "%gray%[Spectateur] ";
				else message += "%dblue%[%blue%Joueur%dblue%] %blue%";
			} else {
				message += player.getTeam().getDisplayName() + " ";
			}
			message += player.getPlayerName();
			message += "%gray% > %white%";

			if(!player.isAdmin())
				message += ChatUtils.colorDelete(base);
			else message += base;

			return message;
		} catch(Exception exception){
			exception.printStackTrace();
			return null;
		}
	}
}
