package fr.customentity.badblockwarps.listeners;

import fr.customentity.badblockwarps.BadBlockWarps;
import fr.customentity.badblockwarps.data.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

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

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = event.getClickedBlock();
            if (b.getType() == Material.SIGN || b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST) {
                if (BadBlockWarps.getInstance().getSelectionHashMap().containsKey(player.getName())) {
                    Warp warp = BadBlockWarps.getInstance().getSelectionHashMap().get(player.getName());
                    if (warp.getSigns().contains(b.getLocation())) return;
                    warp.getSigns().add(b.getLocation());
                    player.sendMessage(BadBlockWarps.getInstance().getMessage("sign-created-warp").replace("%warp%", warp.getName()));
                    BadBlockWarps.getInstance().updateSigns(warp);
                    BadBlockWarps.getInstance().getSelectionHashMap().remove(player.getName());
                } else {
                    Warp warp = Warp.getWarpBySign(b.getLocation());
                    if (warp != null) {
                        if (!warp.isEnabled() || Bukkit.getWorld(warp.getWorld()) == null) return;
                        player.teleport(warp.getLocation());
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("teleport-warp").replace("%warp%", warp.getName()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block b = event.getBlock();

        if (!player.hasPermission("badblock.admin")) return;
        ;
        if (b.getType() == Material.SIGN || b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST) {
            Warp warp = Warp.getWarpBySign(b.getLocation());
            if (warp != null) {
                player.sendMessage(BadBlockWarps.getInstance().getMessage("sign-deleted-warp").replace("%warp%", warp.getName()));
                warp.getSigns().remove(b.getLocation());
            }
        }
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                BadBlockWarps.getInstance().updateSigns();
            }
        }.runTaskLater(BadBlockWarps.getInstance(), 60L);
    }


    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                BadBlockWarps.getInstance().updateSigns();
            }
        }.runTaskLater(BadBlockWarps.getInstance(), 60L);
    }
}
