package fr.badblock.gameapi.servers;

import java.io.File;
import java.util.List;
import java.util.function.Function;

import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.AllArgsConstructor;

/**
 * Permet de g�n�rer des coffres al�atoires avec une configuration
 * 
 * @author LeLanN
 */
public interface ChestGenerator {
	/**
	 * Ajoute un item dans la configuration charg�e
	 * 
	 * @param item
	 *            L'item
	 * @param probability
	 *            La probabilit� d'avoir cet item
	 * @param save
	 *            Si la configuration doit �tre sauvegard�e avec cet item
	 */
	public void addItemInConfiguration(ItemStack item, int probability, boolean save);

	/**
	 * Apr�s que cette m�thode soit appel�e, les coffres ouverts par les joueurs
	 * seront automatiquement g�n�r�s
	 */
	public void beginJob();

	/**
	 * Permet de g�n�rer un inventaire d'un certains nombre de lignes avec la
	 * configuration charg�e
	 * 
	 * @param lines
	 *            Le nombre de lignes
	 * @return Le contenu de l'inventaire
	 */
	public ItemStack[] generateChest(BadblockPlayer player, int lines);

	/**
	 * V�rifie si le g�n�rateur � �t� configur�
	 * 
	 * @return Si il a �t� configur�
	 */
	public boolean isConfigurated();

	/**
	 * V�rifie si le g�n�rateur a �t� d�marr� (remplissage automatique des
	 * coffres)
	 * 
	 * @return Si il a �t� d�marr�
	 */
	public boolean isWorking();

	/**
	 * R�initialise le contenu des coffres
	 */
	public void resetChests();

	/**
	 * Permet de configurer les coffres
	 * 
	 * @param file
	 *            Le fichier de configuration
	 */
	public void setConfigurationFile(File file);

	/**
	 * D�finit si le coffre se supprime lorsqu'un joueur le ferme
	 * 
	 * @param removeOnOpen
	 *            Si le coffre se supprime
	 */
	public void setRemoveOnOpen(boolean removeOnOpen);
	
	/**
	 * D�finit si les coffres sont individuels (faux coffres)
	 * @param individualChest Si les coffres sont individuels
	 */
	public void setIndividualChest(boolean individualChest);
	
	public void setAlternateItemsProvider(Function<BadblockPlayer, List<ISProb>> provider);
	
	@AllArgsConstructor
	public static class ISProb {
		public int prob;
		public ItemStack stack;
	}
}
