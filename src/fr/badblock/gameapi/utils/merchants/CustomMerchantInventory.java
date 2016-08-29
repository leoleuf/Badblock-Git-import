package fr.badblock.gameapi.utils.merchants;

import org.bukkit.entity.Villager;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

/**
 * Repr�sente les offres d'un marchant
 * 
 * @author LeLanN
 */
public interface CustomMerchantInventory {
	/**
	 * Ajoute un objet vendable
	 * 
	 * @param recipe
	 *            L'objet
	 */
	public void addRecipe(CustomMerchantRecipe recipe);

	/**
	 * Applique les offres choisies � un villageois
	 * 
	 * @param villager
	 *            Le villageois
	 */
	public void applyTo(Villager villager);

	/**
	 * R�cup�re les objets vendables par le marchant
	 * 
	 * @return Les objets vendables
	 */
	public CustomMerchantRecipe[] getRecipes();

	/**
	 * Permet d'ouvrir l'inventaire � un joueur
	 * 
	 * @param player
	 *            Le joueur
	 */
	public void open(BadblockPlayer player);

	/**
	 * Permet d'ouvrir l'inventaire � un joueur
	 * 
	 * @param player
	 *            Le joueur
	 * @param customName
	 *            Le nom de l'inventaire
	 */
	public void open(BadblockPlayer player, TranslatableString customName);

	/**
	 * Enl�ve un objet vendable
	 * 
	 * @param recipe
	 *            L'objet
	 */
	public void removeRecipe(CustomMerchantRecipe recipe);
}
