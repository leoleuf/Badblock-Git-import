package fr.badblock.gameapi.utils.i18n;

/**
 * Repr�sente la version traduite d'un mot, tel que trouvable dans la configuration.
 * La classe n'est uniquement que la repr�sentation brute de la configuration, et ne permet pas de formatter le message.
 * Passer par {@link fr.badblock.gameapi.utils.i18n.Language} pour cela.
 * 
 * @author LelanN
 */
public interface Word {
	/**
	 * R�cup�re le mot
	 * @param plural Si le mot est au pluriel
	 * @param determinant Le d�terminant du mot
	 * @return Le mot
	 */
	public String get(boolean plural, WordDeterminant determinant);
	
	/**
	 * Repr�sente les diff�rents d�terminants possible
	 * @author LeLanN
	 */
	public enum WordDeterminant {
		/**
		 * En fran�ais un / une / du / de la / des
		 */
		UNDEFINED,
		/**
		 * En fran�ais l'/ le / la / les
		 */
		DEFINED,
		/**
		 * Le mot, tout simplement :o
		 */
		SIMPLE;
	}
}
