package fr.badblock.gameapi.utils.itemstack;

import fr.badblock.gameapi.players.BadblockPlayer;

/**
 * Appel� lorsqu'un ItemStackExtra est utilis�
 * @author LeLanN
 */
public abstract class ItemEvent {
	/**
	 * Appel�e quand l'item est utilis�
	 * @param action L'action
	 * @param player Le joueur
	 * @return Si l'action doit �tre annul�e
	 */
	public abstract boolean call(ItemAction action, BadblockPlayer player);
}
