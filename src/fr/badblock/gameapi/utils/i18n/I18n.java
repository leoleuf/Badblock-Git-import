package fr.badblock.gameapi.utils.i18n;

import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.utils.i18n.Word.WordDeterminant;

/**
 * Repr�sente le syst�me de traduction et de messages configurables BadBlock.
 * Les diff�rentes langues pouvant �tre support�es sont represent�es par
 * l'�num�ration {@link fr.badblock.gameapi.utils.i18n.Locale}.
 * 
 * @author LeLanN
 */
public interface I18n {
	/**
	 * Broadcast un message � tous les joueurs. Le syst�me va rechercher la
	 * langue la plus adapt�e pour tous les joueurs.
	 * 
	 * @param key
	 *            La cl� du message dans le ficher configuration
	 * @param args
	 *            Les arguments � remplacer dans le message (%0 le premier, %1
	 *            le deuxi�me, ..., %n le �ni�me)
	 */
	public void broadcast(String key, Object... args);

	/**
	 * 
	 * R�cup�re un message via la langue du CommandSender.
	 * 
	 * @param sender
	 *            Le CommandSender (Bukkit)
	 * @param key
	 *            La cl� du message dans le fichier configuration
	 * @param args
	 *            Les arguments � remplacer dans le message (%0 le premier, %1
	 *            le deuxi�me, ..., %n le �ni�me)
	 * 
	 * @return Le message formatt� et traduit
	 */
	public String[] get(CommandSender sender, String key, Object... args);

	/**
	 * 
	 * R�cup�re un message via une langue choisie.
	 * 
	 * @param locale
	 *            La langue choisie
	 * @param key
	 *            La cl� du message dans le fichier configuration
	 * @param args
	 *            Les arguments � remplacer dans le message (%0 le premier, %1
	 *            le deuxi�me, ..., %n le �ni�me)
	 * 
	 * @return Le message formatt� et traduit
	 */
	public String[] get(Locale locale, String key, Object... args);

	/**
	 * R�cup�re un message via la langue par d�faut.
	 * 
	 * @param key
	 *            La cl� du message dans le fichier configuration
	 * @param args
	 *            Les arguments � remplac�s dans le message (%0 le premier, %1
	 *            le deuxi�me, ..., %n le �ni�me)
	 * 
	 * @return Le message formatt�
	 */
	public String[] get(String key, Object... args);

	/**
	 * Tous les langages de l'�num�ration Locale ne sont pas forc�mment traduit.
	 * Cette m�thode permet de r�cup�rer les quels le sont.
	 * 
	 * @return La liste des langages traduits
	 */
	public Collection<Locale> getConfiguratedLocales();

	/**
	 * R�cup�re une langue � partir de son nom
	 * 
	 * @param locale
	 *            La langue � trouver
	 * @return La langue trouv�e (si elle n'existe pas, la langue par d�faut est
	 *         envoy�e)
	 */
	public Language getLanguage(Locale locale);

	/**
	 * R�cup�re un message d'une seule ligne via une langue choisie.
	 * 
	 * @param locale
	 *            La langue choisie
	 * @param key
	 *            La cl� du message dans le fichier configuration
	 * @param plural
	 *            Si le mot doit �tre au pluriel
	 * @param determinant
	 *            Le type de d�terminant avant le mot
	 * 
	 * @return Le message formatt� et traduit
	 */
	public String getWord(Locale locale, String key, boolean plural, WordDeterminant determinant);

	/**
	 * R�cup�re un message d'une seule ligne via la langue par d�faut.
	 * 
	 * @param key
	 *            La cl� du message dans le fichier configuration
	 * @param plural
	 *            Si le mot doit �tre au pluriel
	 * @param determinant
	 *            Le type de d�terminant avant le mot
	 * 
	 * @return Le message formatt� et traduit
	 */
	public String getWord(String key, boolean plural, WordDeterminant determinant);

	/**
	 * Permet de remplacer de mani�re plus rapide les couleurs, pour plusieurs
	 * messages. Appel en r�alit� ChatColor.translateAlternateColorCodes.
	 * 
	 * @param base
	 *            Les messages � formatter
	 * @return Les messages remplac�s
	 */
	public List<String> replaceColors(List<String> base);

	/**
	 * Permet de remplacer de mani�re plus rapide les couleurs. Appel en r�alit�
	 * ChatColor.translateAlternateColorCodes.
	 * 
	 * @param base
	 *            Le message � formatter
	 * @return Le message remplac�
	 */
	public String replaceColors(String base);

	/**
	 * Permet de remplacer de mani�re plus rapide les couleurs, pour plusieurs
	 * messages. Appel en r�alit� ChatColor.translateAlternateColorCodes.
	 * 
	 * @param base
	 *            Les messages � formatter
	 * @return Les messages remplac�s
	 */
	public String[] replaceColors(String... base);

	/**
	 * Envoit un message � un CommandSender. Le syst�me va rechercher la langue
	 * la plus adapt�e.
	 * 
	 * @param sender
	 *            Le CommandSender (Bukkit)
	 * @param key
	 *            La cl� du message dans le ficher configuration
	 * @param args
	 *            Les arguments � remplacer dans le message (%0 le premier, %1
	 *            le deuxi�me, ..., %n le �ni�me)
	 */
	public void sendMessage(CommandSender sender, String key, Object... args);
}
