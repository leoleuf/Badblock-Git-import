package fr.badblock.gameapi.servers;

import java.io.File;

import org.bukkit.inventory.ItemStack;

/**
 * Permet de g�n�rer des coffres al�atoires avec une configuration
 * @author LeLanN
 */
public interface ChestGenerator {
	/**
	 * Permet de configurer les coffres
	 * @param file Le fichier de configuration
	 */
	public void setConfigurationFile(File file);

	/**
	 * V�rifie si le g�n�rateur � �t� configur�
	 * @return Si il a �t� configur�
	 */
	public boolean isConfigurated();
	
	/**
	 * Permet de g�n�rer un inventaire d'un certains nombre de lignes
	 * avec la configuration charg�e
	 * @param lines Le nombre de lignes
	 * @return Le contenu de l'inventaire
	 */
	public ItemStack[] generateChest(int lines);
	
	/**
	 * Apr�s que cette m�thode soit appel�e, les coffres ouverts par les joueurs seront automatiquement g�n�r�s
	 */
	public void beginJob();

	/**
	 * V�rifie si le g�n�rateur a �t� d�marr� (remplissage automatique des coffres)
	 * @return Si il a �t� d�marr�
	 */
	public boolean isWorking();
	
	/**
	 * R�initialise le contenu des coffres
	 */
	public void resetChests();
	
	/**
	 * Ajoute un item dans la configuration charg�e
	 * @param item L'item
	 * @param probability La probabilit� d'avoir cet item
	 */
	public void addItemInConfiguration(ItemStack item, int probability);
}
