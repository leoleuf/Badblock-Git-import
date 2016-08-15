package fr.badblock.api.listeners.minigame;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import fr.badblock.api.MJPlugin;
import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;

public class ProtectionListener implements Listener {
	private MJPlugin plugin = null;

	public ProtectionListener(MJPlugin plugin){
		this.plugin = plugin;
	}

	public BPlayer getPlayer(Entity p){
		return BPlayersManager.getInstance().getPlayer(p);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		BPlayer player = BPlayersManager.getInstance().getPlayer(e.getPlayer());
		if(player.isAdmin())
			return;
		else if(e.getClickedBlock() != null && !plugin.canInteract(e.getClickedBlock().getType()))
			if(!(plugin.canUseFlintAndSteel() && e.getItem() != null && e.getItem().getType() == Material.FLINT_AND_STEEL))
			e.setCancelled(true);
	}
	@EventHandler
	public void onLostFood(FoodLevelChangeEvent e){
		BPlayer player = getPlayer(e.getEntity());

		if(player != null && player.isSpectator())
			e.setCancelled(true);
		else if(!plugin.canLostFood()) e.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onDamage(EntityDamageEvent e){
		if(e.getEntity().getType() == EntityType.ITEM_FRAME)
			e.setCancelled(true);
		if(e.getEntity() instanceof Player && !plugin.canBeDamaged())
			e.setCancelled(true);
		else if(e.getCause() == DamageCause.VOID && !e.isCancelled() && e.getDamage() != 0d){
			e.setDamage(20000d);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onClickEntity(PlayerInteractEntityEvent e){
		if(e.getRightClicked() instanceof ItemFrame)
			e.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void hangingBreak(HangingBreakEvent e){
		if(e.getCause() != RemoveCause.ENTITY){
			e.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void hangingBreakByEntity(HangingBreakByEntityEvent e){
		BPlayer player = BPlayersManager.getInstance().getPlayer(e.getEntity());
		if(player == null) return;
		if(!player.isAdmin()){
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onSpawn(CreatureSpawnEvent e){
		if(plugin.canSpawnCustom() && e.getSpawnReason() == SpawnReason.CUSTOM) return;
		else if(!plugin.canSpawn()) e.setCancelled(true);
	}
	@EventHandler
	public void onBurn(BlockBurnEvent e){
		if(!plugin.canBurn())
			e.setCancelled(true);
	}
	@EventHandler
	public void onSpread(BlockSpreadEvent e){
		if(!plugin.canBurn())
			e.setCancelled(true);
	}
	@EventHandler
	public void onExplode(EntityExplodeEvent e){
		if(!plugin.canExplode()){
			e.blockList().clear();
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onExplode(ExplosionPrimeEvent e){
		if(!plugin.canExplode())
			e.setCancelled(true);
	}
	@EventHandler
	public void onBreakBlock(BlockBreakEvent e){
		BPlayer player = getPlayer(e.getPlayer());

		if(player.isAdmin() || e.getPlayer().isOp())
			return;
		else if(player.isSpectator())
			e.setCancelled(true);
		else if(!plugin.canModifyMap()) e.setCancelled(true);
	}
	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent e){
		BPlayer player = getPlayer(e.getPlayer());

		if(player.isAdmin())
			return;
		else if(player.isSpectator())
			e.setCancelled(true);
		else if(e.getItemInHand() != null && e.getItemInHand().getType() == Material.FLINT_AND_STEEL && !plugin.canUseFlintAndSteel())
			e.setCancelled(true);
		else if(!plugin.canModifyMap()) e.setCancelled(true);
	}
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e){
		if(e.toWeatherState())
			e.setCancelled(true);
	}
	@EventHandler
	public void onDrop(PlayerDropItemEvent e){
		BPlayer player = getPlayer(e.getPlayer());

		if(player.isAdmin())
			return;
		else if(player.isSpectator())
			e.setCancelled(true);
		else if(!plugin.canDropOrPickup()) e.setCancelled(true);
	}
	@EventHandler
	public void onPickUp(PlayerPickupItemEvent e){
		BPlayer player = getPlayer(e.getPlayer());

		if(player.isAdmin())
			return;
		else if(player.isSpectator())
			e.setCancelled(true);
		else if(!plugin.canDropOrPickup()) e.setCancelled(true);	
	}
}
