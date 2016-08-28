package fr.badblock.gameapi.packets.watchers;

/**
 * Repr�sente les DataWatchers d'une entit� vivante.<br>
 * Tous ceux existants ne sont pas repr�sentes car parfois peu utile dans notre
 * utilisation.
 * 
 * @author LeLanN
 */
public interface WatcherLivingEntity extends WatcherEntity {
	public WatcherLivingEntity setArrowsInEntity(int arrows);
}
