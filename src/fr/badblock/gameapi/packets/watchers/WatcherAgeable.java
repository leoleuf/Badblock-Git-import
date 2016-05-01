package fr.badblock.gameapi.packets.watchers;

/**
 * Repr�sente les watchers d'une entit� pouvant se reproduire.
 * @author LeLanN
 */
public interface WatcherAgeable extends WatcherLivingEntity {
	/**
	 * D�finit si l'entit� est un b�b� ou non.
	 * @param baby Si l'entit� est un b�b�
	 * @return Le watcher
	 */
	public WatcherAgeable setBaby(boolean baby);
}
