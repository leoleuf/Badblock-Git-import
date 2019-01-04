package fr.badblock.game.core112R1.technologies.rabbitlisteners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.utils.BukkitUtils;

public class VanishTeleportListener extends RabbitListener {

	public static Map<String, Long> time = new HashMap<>();
	public static Map<String, String[]> splitters = new HashMap<>();

	public VanishTeleportListener() {
		super(GameAPI.getAPI().getRabbitService(), "gameapi.ghost", RabbitListenerType.SUBSCRIBER, false);
		load();
	}

	@Override
	public void onPacketReceiving(String body) {
		String[] splitter = body.split(";");
		BadblockPlayer player = BukkitUtils.getPlayer(splitter[0]);
		if (player == null) {
			time.put(splitter[0].toLowerCase(), System.currentTimeMillis() + 10000);
			splitters.put(splitter[0].toLowerCase(), splitter);
		}
		else {
			manage(player, splitter);
		}
	}

	@SuppressWarnings("deprecation")
	public static void manage(BadblockPlayer player, String[] splitter) {
		if (player == null) return;
		player.closeInventory();
		player.setBadblockMode(BadblockMode.SPECTATOR);
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(true);
		player.setFlying(true);
		player.setVisible(false, pl -> !pl.hasPermission("gameapi.ghost"));
		for (Player plo : Bukkit.getOnlinePlayers())
		{
			plo.hidePlayer(player);
		}
		if (splitter == null) return;
		if (splitter.length > 1 && splitter[1] != null && !splitter[1].isEmpty()) {
			BadblockPlayer otherPlayer = BukkitUtils.getPlayer(splitter[1]);
			if (otherPlayer != null) {
				player.teleport(otherPlayer);
			}
		}
	}

}
