package fr.badblock.api.runnables;

import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.api.BPlugin;
import lombok.Getter;

public abstract class BRunnable extends BukkitRunnable {
	protected long update;
	@Getter protected int time;
	
	public BRunnable(long update){
		this.update = update;
		this.time = 0;
	}
	
	public void start(){
		runTaskTimer(BPlugin.getInstance(), 0, update);
	}
}
