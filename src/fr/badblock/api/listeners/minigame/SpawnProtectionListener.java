package fr.badblock.api.listeners.minigame;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import fr.badblock.api.utils.maths.Selection;

public class SpawnProtectionListener implements Listener{
	private static List<Selection> spawns = new ArrayList<Selection>();
	public static void addSpawn(Selection s){
		spawns.add(s);
	}
	public static boolean isProtected(Block block){
		for(Selection s : spawns)
			if(s.isInSelection(block.getLocation()))
				return true;
		return false;
	}
	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent e){
		if(isProtected(e.getBlock()))
			e.setCancelled(true);
	}
	@EventHandler
	public void onBreakBlock(BlockBreakEvent e){
		if(isProtected(e.getBlock()))
			e.setCancelled(true);
	}
	@EventHandler
	public void onExplode(EntityExplodeEvent e){
		for(Block b : e.blockList()){
			if(isProtected(b))
				e.setCancelled(true);
		}
	}
	@EventHandler
	public void onExplode(BlockExplodeEvent e){
		for(Block b : e.blockList()){
			if(isProtected(b))
				e.setCancelled(true);
		}
	}
	@EventHandler
	public void fallingBlockHitGround(EntityChangeBlockEvent e){
		if(isProtected(e.getBlock()))
			e.setCancelled(true);
	}

}
