package fr.badblock.gameapi.utils.itemstack;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

/**
 * Classe permettant de cr�er un item stack complexe de mani�re beaucoup plus simple.<br>
 * Utiliser {@link fr.badblock.gameapi.GameAPI} pour avoir une nouvelle factory.
 * @author LeLanN
 */
public interface ItemStackFactory {
	/**
	 * D�finit si le futur item est cassable ou non.
	 * @param unbreakable Si le futur item est cassable.
	 * @return La factory
	 */
	public ItemStackFactory unbreakable(boolean unbreakable);
	
	/**
	 * Permet d'enchanter le futur item
	 * @param enchantment L'enchantement � appliquer
	 * @param level Le niveau de l'enchantement
	 * @return La factory
	 */
	public ItemStackFactory enchant(Enchantment enchantment, int level);
	
	/**
	 * Permet de changer la description du futur item. Les codes couleurs seront remplac�e automatiquement.
	 * @param lore La liste de description
	 * @return La factory
	 */
	public ItemStackFactory lore(String... lore);
	
	/**
	 * Permet de changer la description du futur item. Les codes couleurs seront remplac�e automatiquement.
	 * @param lore La liste de description
	 * @return La factory
	 */
	public ItemStackFactory lore(TranslatableString lore);

	
	/**
	 * Permet de changer le displayname du futur item. Les codes couleurs seront remplac�e automatiquement.
	 * @param displayName Le displayName � mettre � l'item
	 * @return La factory
	 */
	public ItemStackFactory displayName(String displayName);
	
	/**
	 * Permet de changer le displayname du futur item. Les codes couleurs seront remplac�e automatiquement.
	 * @param displayName Le displayName � mettre � l'item
	 * @return La factory
	 */
	public ItemStackFactory displayName(TranslatableString displayName);
	
	/**
	 * Change la durabilit� (ou le data, c'est �quivalent) du futur item.
	 * @param data Le nouveau data
	 * @return La factory
	 */
	public ItemStackFactory durability(short data);
	
	/**
	 * Change le type du futur item
	 * @param material Le nouveau type
	 * @return La factory
	 */
	public ItemStackFactory type(Material material);
	
	/**
	 * Cr�e l'ItemStack en lui ajoutant une couleur de laine. type() et durability() automatique.
	 * @param amount Le nombre d'item � cr�er
	 * @param color La couleur de laine
	 * @return L'item cr��
	 */
	public ItemStack asWool(int amount, DyeColor color);
	
	/**
	 * Cr�e l'ItemStack en lui ajoutant une couleur d'armure. Attention, utiliser le bon type(material) avant.
	 * @param amount Le nombre d'item � cr�er
	 * @param color La couleur d'armure
	 * @return L'item cr��
	 */
	public ItemStack asLeatheredArmor(int amount, Color color);
	
	/**
	 * Cr�e l'ItemStack en lui ajoutant une couleur d'armure. type() et durability() automatique.
	 * @param amount Le nombre d'item � cr�er
	 * @param owner Le personnage du cr�ne cr��
	 * @return L'item cr��
	 */
	public ItemStack asSkull(int amount, String owner);
	
	/**
	 * Cr�e un ItemStack normal
	 * @param amount
	 * @return L'item cr��
	 */
	public ItemStack create(int amount);
	
	/**
	 * Transforme la factory en itemstackextra
	 * @param amount Le nom d'item � cr�er
	 * @return L'extra
	 */
	public ItemStackExtra asExtra(int amount);
	
	/**
	 * Permet de 'dire' � la factory d'utiliser l'I18n pour displayName et lore
	 * @param locale La langue utilis�e
	 */
	public ItemStackFactory doWithI18n(Locale locale);
	
	/**
	 * Retourne une nouvelle factory avec les m�mes param�tres
	 * @return Une nouvelle factory
	 */
	public ItemStackFactory clone();
}
