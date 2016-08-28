package fr.badblock.gameapi.utils.i18n;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.Data;

/**
 * Repr�sente une cha�ne de caract�re traductible. Utiliser � plusieurs endroit dans l'API pour simplifier.
 * @author LeLanN
 */
@Data public class TranslatableString {
	private String 	 key;
	private Object[] objects;
	
	/**
	 * Cr�e une nouvelle cha�ne traduisible
	 * @param key La key
	 * @param objects Les arguments
	 */
	public TranslatableString(String key, Object... objects){
		this.key 	 = key;
		this.objects = objects;
	}
	
	/**
	 * Envoit le message � un command sender
	 * @param sender Le sender
	 */
	public void send(CommandSender sender){
		GameAPI.i18n().sendMessage(sender, key, objects);
	}
	
	/**
	 * Envoit le message � tous les joueurs
	 */
	public void broadcast(){
		GameAPI.i18n().broadcast(key, objects);
	}
	
	/**
	 * R�cup�re la premi�re ligne du message
	 * @param player Le joueur (pour la langue)
	 * @return La ligne
	 */
	public String getAsLine(BadblockPlayer player){
		return getAsLine(player.getPlayerData().getLocale());
	}
	
	/**
	 * R�cup�re la premi�re ligne du message
	 * @param player Le joueur (pour la langue)
	 * @return La ligne
	 */
	public String getAsLine(CommandSender sender){
		if(sender instanceof BadblockPlayer)
			return getAsLine((BadblockPlayer) sender);
		
		return getAsLine(Locale.ENGLISH_US);
	}
	
	/**
	 * R�cup�re la premi�re ligne du message
	 * @param locale La langue
	 * @return La ligne
	 */
	public String getAsLine(Locale locale){
		return GameAPI.i18n().get(locale, key, objects)[0];
	}
	
	/**
	 * R�cup�re le message sur plusieurs lignes
	 * @param player Le joueur (pour avoir la langue)
	 * @return Le message
	 */
	public String[] get(BadblockPlayer player){
		return get(player.getPlayerData().getLocale());
	}
	
	/**
	 * R�cup�re le message sur plusieurs lignes
	 * @param locale La langue
	 * @return Le message
	 */
	public String[] get(Locale locale){
		return GameAPI.i18n().get(locale, key, objects);
	}
}
