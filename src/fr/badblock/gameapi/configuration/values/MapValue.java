package fr.badblock.gameapi.configuration.values;

/**
 * Représente une valeur pour la configuration des maps
 * 
 * @author LelanN
 *
 * @param <T>
 *            Le type de valeur
 */
public interface MapValue<T> {
	/**
	 * Récupčre la véritable valeur
	 * 
	 * @return La véritable valeur
	 */
	public T getHandle();

	default void postLoad() {
	}
}
