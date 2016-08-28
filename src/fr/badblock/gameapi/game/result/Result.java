package fr.badblock.gameapi.game.result;

import java.util.Map;

import com.google.common.collect.Maps;

import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Repr�sente un r�sultat de partie en fonction du joueur<br>
 * Ce r�sultat peut contenir :
 * <ul>
 * <li>Les statistiques des joueurs dans la partie</li>
 * <li>Un classement des teams/scores et</li>
 * <li>Des informations comme la dur�e de la partie</li>
 * </ul>
 * Le r�sultat doit en th�orie �tre traduit pour le joueur directemment (la
 * traduction n'est pas sur le web) et �tre sp�cifique � chaque
 * joueur/langue<br>
 * Utiliser {@link BadblockPlayer#postResult(Result)}<br>
 * Pour les noms, possibilit� d'utiliser [avatar:pseudo] et [img:example.png]
 * pour afficher des images.
 * 
 * @author LeLanN
 */
@RequiredArgsConstructor
public class Result {
	@Getter
	private final String displayName;
	private final Map<String, ResultCategory> categories = Maps.newHashMap();

	/**
	 * Register une nouvelle cat�gorie au r�sultat
	 * 
	 * @param name
	 *            Le nom (interne) de la cat�gorie
	 * @param category
	 *            La cat�gorie
	 * @return La cat�gorie
	 */
	public <T extends ResultCategory> T registerCategory(@NonNull String name, @NonNull T category) {
		name = name.toLowerCase();
		categories.put(name, category);

		return category;
	}

	/**
	 * R�cup�re une cat�gorie
	 * 
	 * @param name
	 *            Le nom (interne) de la cat�gorie
	 * @return La cat�gorie trouv�e
	 */
	public ResultCategory getCategory(String name) {
		return categories.get(name.toLowerCase());
	}

	/**
	 * Enl�ve une cat�gorie
	 * 
	 * @param name
	 *            Le nom (interne) de la cat�gorie
	 */
	public void removeCatagory(String name) {
		categories.remove(name.toLowerCase());
	}
}
