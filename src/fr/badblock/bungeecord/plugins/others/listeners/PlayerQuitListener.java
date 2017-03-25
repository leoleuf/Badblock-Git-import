package fr.badblock.bungeecord.plugins.others.listeners;

import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.event.PlayerDisconnectEvent;
import fr.badblock.bungeecord.api.plugin.Listener;
import fr.badblock.bungeecord.event.EventHandler;
import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.bungeecord.plugins.others.Player;
import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import fr.badblock.commons.technologies.rabbitmq.RabbitPacketType;
import fr.badblock.commons.utils.Encodage;

public class PlayerQuitListener implements Listener {
	
	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
		ProxiedPlayer proxiedPlayer = event.getPlayer();
		Player player = Player.get(proxiedPlayer);
		BadBlockBungeeOthers.getInstance().getRabbitService().sendPacket("quit", proxiedPlayer.getName(), Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
		BadblockDatabase.getInstance().addRequest(new Request("UPDATE cheatReports SET lastLogin = '0' WHERE pseudo = '" + BadblockDatabase.getInstance().mysql_real_escape_string(proxiedPlayer.getName()) + "'", RequestType.SETTER));
		player.remove();
	}

}
