package fr.badblock.bungeecord.plugins.ladder.skins.api;

import fr.badblock.bungeecord.plugins.ladder.skins.format.SkinProfile;
import fr.badblock.bungeecord.plugins.ladder.skins.storage.SkinStorage;
import fr.badblock.bungeecord.plugins.ladder.skins.utils.SkinFetchUtils;
import fr.badblock.bungeecord.plugins.ladder.skins.utils.SkinFetchUtils.SkinFetchFailedException;

public class SkinsRestorerAPI {
	/**
	 * This method is used to set player's skin.
	 * <p>
	 */
	   public static void setSkin(final String playerName, final String skinName) throws SkinFetchFailedException{
			SkinProfile skinprofile = SkinFetchUtils.fetchSkinsProfile(skinName, null);
			SkinStorage.getInstance().setSkinData(playerName, skinprofile);
	   }
	   
		/**
		 * This method is used to check if player has
		 * saved skin data. If player skin data equals null, the method will return false.
		 * Else if player has saved data, it will return true.
		 */
       public static boolean hasSkin(String playerName){
    	   if (SkinStorage.getInstance().getSkinData(playerName)==null){
    		   return false;
    	   }
    	   return true;
       }
       
       /**
        * This method is used to get player's skin name.
        * If player doesn't have skin, the method will return null.
        * Else it will return player's skin name.
        */
       public static String getSkinName(String playerName){
    	   SkinProfile data = SkinStorage.getInstance().getSkinData(playerName);
    	   if (data==null){
    		   return null;
    	   }
    	   return data.getName();
       }
       
       /**
        * Used for instant skin applying.
        * This method can be used on
        * Bungeecord side only!
        */
       /*public static void applySkinBungee(ProxiedPlayer player){
    	   	//SkinFactoryBungee.getFactory().applySkin(player);
       }*/
}