package fr.customentity.badblockwarps.listeners;

import fr.customentity.badblockwarps.BadBlockWarps;
import fr.customentity.badblockwarps.data.Warp;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

/**
 * Created by CustomEntity on 27/01/2019 for BadBlockWarps.
 */
public class WarpListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        BadBlockWarps.getInstance().getSelectionHashMap().remove(event.getPlayer().getName());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = event.getClickedBlock();
            if(b.getType() == Material.SIGN || b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST) {
                Sign sign = (Sign)b.getState();
                if(BadBlockWarps.getInstance().getSelectionHashMap().containsKey(player.getName())) {
                    Warp warp = BadBlockWarps.getInstance().getSelectionHashMap().get(player.getName());

                } else {

                }
            }
        }
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        BadBlockWarps.getInstance().updateSigns();
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        BadBlockWarps.getInstance().updateSigns();
    }
}
