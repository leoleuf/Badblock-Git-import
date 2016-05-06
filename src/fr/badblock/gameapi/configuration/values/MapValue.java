package fr.badblock.gameapi.configuration.values;

/**
 * Repr�sente une valeur pour la configuration des maps
 * @author LelanN
 *
 * @param <T> Le type de valeur
 */
public interface MapValue<T> {
	/**
	 * R�cup�re la v�ritable valeur
	 * @return La v�ritable valeur
	 */
	public T getHandle();
}
