package fr.badblock.gameapi.utils.i18n;

import fr.badblock.gameapi.utils.i18n.Word.WordDeterminant;

/**
 * Impl�mentation d'une langue, va g�rer la configuration et le formattage des
 * messages.
 * 
 * @author LeLanN
 */
public interface Language {
	/**
	 * 
	 * Formatte le message envoy� dans cette langue (pour ce qui est du
	 * header/footer, la langue d�pend de celle du Message).
	 * 
	 * @param message
	 *            Le message � formatter.
	 * @param args
	 *            Les arguments � remplacer dans le message (%0 le premier, %1
	 *            le deuxi�me, ..., %n le �ni�me)
	 * 
	 * @return Le message formatt� et traduit.
	 */
	public String[] formatMessage(Message message, Object... args);

	/**
	 * 
	 * R�cup�re un message dans cette langue.
	 * 
	 * @param key
	 *            La cl� du message dans le fichier configuration
	 * @param args
	 *            Les arguments � remplacer dans le message (%0 le premier, %1
	 *            le deuxi�me, ..., %n le �ni�me)
	 * 
	 * @return Le message formatt� et traduit
	 */
	public String[] get(String key, Object... args);

	/**
	 * Le footer (c'est � dire partie de message succ�dant la principale). Il
	 * s'agit ici d'une ligne. Utilis� lors du formattage du message.
	 * 
	 * @return Le footer trouv�, n'est pas cens� �tre null.
	 */
	public String getFooter();

	/**
	 * La version longue du header (c'est � dire partie de message pr�c�dent la
	 * principale). Dans le cas du header long, il s'agit d'une ligne. Utilis�
	 * lors du formattage du message.
	 * 
	 * @return Le header trouv�, n'est pas cens� �tre null.
	 */
	public String[] getHeader();

	/**
	 * Renvoit la langue g�r�e par la classe.
	 * 
	 * @return La langue.
	 */
	public Locale getLocale();

	/**
	 * R�cup�re le message de mani�re non formatt�, tel qu'il a �t� r�cup�r�
	 * dans la configuration.
	 * 
	 * @param key
	 *            La cl� du message dans le fichier configuration
	 * @return Le message non formatt�. Si il n'existe pas, retourne null.
	 */
	public Message getMessage(String key);

	/**
	 * La version courte du header (c'est � dire partie de message pr�c�dent la
	 * principale). Dans le cas du header long, il s'agit d'un simple pr�fixe.
	 * Utilis� lors du formattage du message.
	 * 
	 * Ex : [BadBlock]
	 * 
	 * @return Le header trouv�, n'est pas cens� �tre null.
	 */
	public String getShortHeader();

	/**
	 * R�cup�re le mot de mani�re non formatt�, tel qu'il a �t� r�cup�r� dans la
	 * configuration.
	 * 
	 * @param key
	 *            La cl� du mot dans le fichier configuration
	 * @return Le mot non formatt�. Si il n'existe pas, retourne null.
	 */
	public Word getWord(String key);

	/**
	 * R�cup�re un message d'une seule ligne dans cette langue
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
}
