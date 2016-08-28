package fr.badblock.gameapi.configuration.values;

import com.google.gson.JsonElement;

/**
 * Repr�sente une valeur primitive
 * 
 * @author LelanN
 *
 * @param <T>
 *            Le type de valeur
 */
public interface MapValuePrimitive<T> extends MapValue<T> {
	/**
	 * Charge la valeur depuis un �l�ment JSON
	 * 
	 * @param json
	 *            L'�l�ment
	 */
	public void from(JsonElement json);

	/**
	 * R�cup�re la valeur sous forme d'�l�ment JSON
	 * 
	 * @return L'�l�ment
	 */
	public JsonElement to();
}
