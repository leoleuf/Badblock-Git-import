package fr.badblock.game.core112R1.defaultinventories;

import fr.badblock.gameapi.utils.itemstack.ItemStackExtra;

public abstract class DefaultInventory {
	public abstract String getKey();
	
	public abstract ItemStackExtra create(String key);
	
	public abstract DefaultInventory getPrevious();
}
