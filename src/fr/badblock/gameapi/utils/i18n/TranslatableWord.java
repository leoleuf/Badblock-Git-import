package fr.badblock.gameapi.utils.i18n;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.i18n.Word.WordDeterminant;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Repr�sente un mot traductible. Utiliser � plusieurs endroit dans l'API pour
 * simplifier, ou pour int�grer un mot traduit en<br>
 * param�tre d'un message (ex : block ou entit�).
 * 
 * @author LeLanN
 */
@Data
@AllArgsConstructor
public class TranslatableWord {
	private String key;
	private boolean plural;
	private WordDeterminant determinant;

	/**
	 * R�cup�re le mot traduit dans une langue
	 * 
	 * @param locale
	 *            La langue
	 * @return Le mot
	 */
	public String getWord(Locale locale) {
		return GameAPI.i18n().getWord(locale, key, plural, determinant);
	}
}
