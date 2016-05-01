package fr.badblock.gameapi.utils.merchants;

import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Repr�sente une vente d'un marchand.
 * @author LeLanN
 */
@Data@AllArgsConstructor
public class CustomMerchantRecipe {
	private ItemStack firstItem, secondItem, result;
}
