package fr.badblock.bungee.listeners;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.data.ip.BadIpData;
import fr.badblock.bungee.data.players.BadPlayer;
import fr.badblock.bungee.utils.RedisUtils;
import net.md_5.bungee.api.event.AsyncDataLoadRequest;
import net.md_5.bungee.api.event.AsyncDataLoadRequest.Result;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListener implements Listener {

	@EventHandler
	public void onJoin(AsyncDataLoadRequest e) {
		if (BadBungee.getInstance().get(e.getPlayer()) != null) {
			e.getDone().done(new Result(null, "§cVous êtes déjà connecté sur BadBlock."), null);
			return;
		}
		BadIpData badIpData = new BadIpData(e.getHandler().getAddress().getAddress().getHostAddress());
		badIpData.addUsername(e.getPlayer());
		badIpData.setLastName(e.getPlayer());
		BadPlayer badPlayer = new BadPlayer(e.getPlayer(), null, e.getHandler().getAddress().getAddress());
		badIpData.addUUID(badPlayer.getUniqueId());
		badIpData.updateData();
		BadBungee.getInstance().getRedisService().set(RedisUtils.PLAYERDATA_PATTERN + e.getPlayer().toLowerCase(), badPlayer);
		BadBungee.getInstance().keepAlive();
		e.getDone().done(new Result(badPlayer.createResultObject(), null), null);
		// Set data
	}

	@EventHandler
	public void onLogin(PostLoginEvent event) {
		System.out.println(event.getPlayer().getName());
	}

}
