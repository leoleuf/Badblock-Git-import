package fr.badblock.gameapi.game.result;

/**
 * Repr�sente une cat�gorie pour {@link Result}
 * 
 * @author LeLanN
 */
public interface ResultCategory {
	/**
	 * Repr�sente les diff�rents types de cat�gories
	 * 
	 * @author LeLanN
	 */
	public enum CategoryType {
		/**
		 * Une cat�gorie � entr�e simple
		 */
		LINED_DATA,
		/**
		 * Une cat�gorie � double entr�e (utile pour un classement par exemple)
		 */
		ARRAY_DATA;
	}

	/**
	 * R�cup�re le nom d'affichage de la cat�gorie
	 * 
	 * @return Le nom d'affichage
	 */
	public String getCategoryName();

	/**
	 * R�cup�re le type de cat�gorie
	 * 
	 * @return Le type de cat�gorie
	 */
	public CategoryType getType();
}
