package fr.badblock.gameapi.utils.itemstack;

import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.players.BadblockPlayer;

/**
 * Repr�sente un inventaire agissant en tant que GUI.<br>
 * Utiliser
 * {@link fr.badblock.gameapi.GameAPI#createCustomInventory(int, String)} pour
 * l'obtenir.
 * 
 * @author LeLanN
 */
public interface CustomInventory {
	/**
	 * Permet d'ajouter un item cliquable dans l'inventaire. Si d�j� plac�, il
	 * sera simplement remplac�. Le callback sera appel� si un joueur clique sur
	 * l'item.
	 * 
	 * @param line
	 *            La ligne de l'item (commence � 0 !)
	 * @param slot
	 *            Le slot � utilis� (commence � 0 !)
	 * @param event
	 *            Le callback qui sera appel�
	 */
	public CustomInventory addClickableItem(int slot, ItemStack item, ItemEvent event);

	/**
	 * Permet d'ajouter un item cliquable dans l'inventaire. Si d�j� plac�, il
	 * sera simplement remplac�. Le callback sera appel� si un joueur clique sur
	 * l'item.
	 * 
	 * @param slot
	 *            Le slot � utilis� (commence � 0 !)
	 * @param item
	 *            L'item � placer. Si null, l'item sera supprim� [comme avec
	 *            removeItem()]. Ici, on attend un itemExtra d�j� 'configur�'.
	 */
	public CustomInventory addClickableItem(int slot, ItemStackExtra item);

	/**
	 * Permet de supprimer un item de l'inventaire.
	 * 
	 * @param slot
	 *            Le slot � utilis� (commence � 0 !)
	 */
	public CustomInventory removeItem(int slot);

	/**
	 * Ajouter un item d�coratif (aucune action lorsque l'on clique)
	 * 
	 * @param slot
	 *            Le slot � utilis� (commence � 0 !)
	 * @param item
	 *            L'item d�coratif � ajouter
	 */
	public CustomInventory addDecorationItem(int slot, ItemStack item);

	/**
	 * Permet d'afficher l'inventaire au joueur.
	 * 
	 * @param player
	 *            Le joueur auquel afficher l'inventaire
	 */
	public void openInventory(BadblockPlayer player);

	/**
	 * R�cup�re le nombre de lignes de l'inventaire
	 * 
	 * @return Le nombre de lignes
	 */
	public int getLineCount();

	/**
	 * R�cup�re la taille de l'inventaire
	 * 
	 * @return La taille
	 */
	public int size();
}
