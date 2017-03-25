package fr.badblock.ladder.plugins.others.listeners;

import java.util.ArrayList;
import java.util.Map.Entry;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.events.EventHandler;
import fr.badblock.ladder.api.events.Listener;
import fr.badblock.ladder.api.events.all.PlayerQuitEvent;
import fr.badblock.ladder.plugins.others.BadBlockOthers;
import fr.badblock.ladder.plugins.others.friends.Friend;
import fr.badblock.ladder.plugins.others.friends.FriendPlayer;
import fr.badblock.ladder.plugins.others.friends.FriendStatus;
import fr.badblock.ladder.plugins.others.utils.I18N;

public class PlayerDisconnectListener implements Listener {

	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		// AntiBotManager.disconnect(player);
		FriendPlayer friendPlayer = FriendPlayer.unload(player);
		// Ladder.getInstance().broadcast("A");
		if (friendPlayer == null)
			return;
		// Ladder.getInstance().broadcast("B");
		if (!friendPlayer.connected)
			return;
		// Ladder.getInstance().broadcast("C");
		if (friendPlayer.getFriendsMap() == null)
			return;
		// Ladder.getInstance().broadcast("D");
		try {
			for (Entry<String, Friend> entry : friendPlayer.getFriendsMap().entrySet()) {
				if (!entry.getValue().getStatus().equals(FriendStatus.OK))
					continue;
				String otherPlayer = entry.getValue().getDemander().equalsIgnoreCase(player.getName())
						? entry.getValue().getReceiver() : entry.getValue().getDemander();
				Player pl = BadBlockOthers.getInstance().getLadder().getPlayer(otherPlayer);
				if (pl == null)
					return;
				pl.sendMessage(I18N.getTranslatedMessage("commands.friend.disconnected", player.getName()));

			}
			if (friendPlayer.party != null) {
				for (String pl : friendPlayer.party.getPlayers()) {
					if (!pl.equalsIgnoreCase(player.getName())) {
						Player ol = BadBlockOthers.getInstance().getLadder().getPlayer(pl);
						if (ol != null)
							ol.sendMessage(I18N.getTranslatedMessage("commands.party.disconnected", player.getName()));
					}
				}
				if (!friendPlayer.party.getLeader().equalsIgnoreCase(player.getName())) {
					Player ol = BadBlockOthers.getInstance().getLadder().getPlayer(friendPlayer.party.getLeader());
					if (ol != null) {
						FriendPlayer foPlayer = FriendPlayer.get(ol);
						if (foPlayer == null)
							return;
						if (foPlayer.party != null)
							if (foPlayer.groupFollow) {
								ol.setPlayersWithHim(foPlayer.party.getFollowUUIDs(player.getName()));
								ol.sendToBungee("playersWithHim"); ol.sendToBukkit("playersWithHim");
							}
					}
				} else {
					for (String playerName : friendPlayer.party.getPlayers()) {
						Player pl = Ladder.getInstance().getPlayer(playerName);
						if (pl == null)
							continue;
						pl.canJoinHimself(true);
						pl.setPlayersWithHim(new ArrayList<>());
						pl.sendToBungee("playersWithHim"); pl.sendToBukkit("playersWithHim");
					}
				}
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
}
