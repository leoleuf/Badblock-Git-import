package fr.badblock.api.listeners;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ArrowBugFixListener implements Listener {

	UUID firstPlayer;

	@EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerShootArrow(EntityShootBowEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if (player.getUniqueId().equals(firstPlayer)) {
			// La flèche comprend une erreur
			Arrow oldArrow = (Arrow) event.getProjectile();
			// nouvelle flèche
			Arrow arrow = oldArrow.getLocation().getWorld().spawn(oldArrow.getLocation(), Arrow.class);
			arrow.setShooter(oldArrow.getShooter());
			((CraftArrow) arrow).getHandle().fromPlayer = pickable(event);
			arrow.setCritical(oldArrow.isCritical());
			arrow.setVelocity(oldArrow.getVelocity());
			arrow.setFireTicks(oldArrow.getFireTicks());
			arrow.setKnockbackStrength(oldArrow.getKnockbackStrength());
			// Remove de l'anciène flèche
			oldArrow.remove();
			// Set le nouveau projectile
			event.setProjectile(arrow);
		}
	}

	public int pickable(EntityShootBowEvent event) {
		if (event.getBow().containsEnchantment(Enchantment.ARROW_INFINITE) || ((Player)event.getEntity()).getGameMode().equals(GameMode.CREATIVE)) return 0;
		return 1;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST) 
	public void onPlayerJoin(PlayerJoinEvent e) {
		if(firstPlayer == null) 
			firstPlayer = e.getPlayer().getUniqueId();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST) 
	public void onPlayerQuit(PlayerQuitEvent e) {
		if(e.getPlayer().getUniqueId().equals(firstPlayer)) 
			firstPlayer = null;
	}
}
