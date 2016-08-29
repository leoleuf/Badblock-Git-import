package fr.badblock.gameapi.packets.watchers;

/**
 * Repr�sente les watchers d'un blaze
 * 
 * @author LelanN
 */
public interface WatcherBlaze extends WatcherLivingEntity {
	/**
	 * D�finit si le blaze est en feu
	 * 
	 * @param onFire
	 *            Si il est en feu
	 * @return Le watcher
	 */
	@Override
	public WatcherBlaze setOnFire(boolean onFire);
}
