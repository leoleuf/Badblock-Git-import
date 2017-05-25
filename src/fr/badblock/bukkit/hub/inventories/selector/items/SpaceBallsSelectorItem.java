package fr.badblock.bukkit.hub.inventories.selector.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.google.gson.Gson;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.run.BadblockGame;
import fr.badblock.gameapi.utils.ConfigUtils;
import fr.badblock.rabbitconnector.RabbitPacketType;
import fr.badblock.rabbitconnector.RabbitService;
import fr.badblock.sentry.SEntry;
import fr.badblock.utils.Encodage;

public class SpaceBallsSelectorItem extends GameSelectorItem {

	@SuppressWarnings("deprecation")
	public SpaceBallsSelectorItem() {
		// super("§bSpaceBalls", Material.getMaterial(153));
		super("hub.items.spaceballsselectoritem", Material.getMaterial(153), "hub.items.spaceballsselectoritem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public List<String> getGames() {
		return Arrays.asList("sb4v4");
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		if (itemAction.equals(ItemAction.INVENTORY_LEFT_CLICK)) {
			//CustomInventory.get(SpaceBallsChooserInventory.class).open(player);
			BadBlockHub instance = BadBlockHub.getInstance();
			RabbitService service = instance.getRabbitService();
			Gson gson = instance.getGson();
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					service.sendAsyncPacket("networkdocker.sentry.join", gson.toJson(new SEntry(player.getName(), "sb4v4", false)),
							Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
					/*if (!player.hasPermission("others.mod.connect")) {
						SEntryInfosListener.tempPlayers.put(player.getName(), System.currentTimeMillis() + SEntryInfosListener.tempTime);
						SEntryInfosListener.tempPlayersRank.put(player.getName(), player.getMainGroup());
						SEntryInfosListener.tempPlayersUUID.put(player.getName(), player.getUniqueId());
						SEntryInfosListener.tempPlayersPropertyMap.put(player.getName(), ((CraftPlayer)player).getHandle().getProfile().getProperties());
					}*/
				}
			};
			if (player.hasPermission("matchmaking.priority")) runnable.run();
			else {
				runnable.run();//TaskManager.runAsyncTaskLater(runnable, new Random().nextInt(20 * 9) + (20 * 3));
			}
			return;
		}
		Location location = ConfigUtils.getLocation(BadBlockHub.getInstance(), "spaceballs");
		if (location == null) // player.sendMessage("§cCe jeu est
								// indisponible.");
			player.sendTranslatedMessage("hub.gameunavailable");
		else
			player.teleport(location);
	}

	@Override
	public BadblockGame getGame() {
		return BadblockGame.SPACE_BALLS;
	}

	@Override
	public boolean isMiniGame() {
		return false;
	}
	
	@Override
	public String getGamePrefix() {
		return "sb";
	}

}
