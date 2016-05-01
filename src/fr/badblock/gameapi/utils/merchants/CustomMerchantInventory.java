package fr.badblock.gameapi.utils.merchants;

import org.bukkit.entity.Villager;

/**
 * Repr�sente les offres d'un marchant
 * @author LeLanN
 */
public interface CustomMerchantInventory {
	/**
	 * R�cup�re les objets vendables par le marchant
	 * @return Les objets vendables
	 */
	public CustomMerchantRecipe[] getRecipes();
	
	/**
	 * Enl�ve un objet vendable
	 * @param recipe L'objet
	 */
	public void removeRecipe(CustomMerchantRecipe recipe);
	
	/**
	 * Ajoute un objet vendable
	 * @param recipe L'objet
	 */
	public void addRecipe(CustomMerchantRecipe recipe);
	
	/**
	 * Applique les offres choisies � un villageois
	 * @param villager Le villageois
	 */
	public void applyTo(Villager villager);
}
