package fr.badblock.gameapi.configuration;

import java.io.File;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonObject;

import fr.badblock.gameapi.configuration.values.MapList;
import fr.badblock.gameapi.configuration.values.MapValue;

/**
 * Repr�sente une configuration simplifi�e
 * 
 * @author LeLanN
 */
public interface BadConfiguration {
	/**
	 * R�cup�re une sous-section � la configuration
	 * 
	 * @param key
	 *            La cl� de la sous-section
	 * @return La sous-section
	 */
	public BadConfiguration getSection(String key);

	/**
	 * R�cup�re une valeur
	 * 
	 * @param key
	 *            La cl� de la valeur
	 * @param clazzValue
	 *            Le type de la valeur
	 * @return La valeur (les modifications ne s'appliquent pas � la
	 *         configuration)
	 */
	public <T extends MapValue<?>> T getValue(String key, Class<T> clazzValue);

	/**
	 * R�cup�re une valeur
	 * 
	 * @param key
	 *            La cl� de la valeur
	 * @param clazzValue
	 *            Le type de la valeur
	 * @param def
	 *            La valeur par d�faut (sera set si non trouv�ee)
	 * @return La valeur (les modifications ne s'appliquent pas � la
	 *         configuration)
	 */
	public <T extends MapValue<?>> T getValue(String key, Class<T> clazzValue, T def);

	/**
	 * R�cup�re une liste de valeurs
	 * 
	 * @param key
	 *            La cl� de la liste
	 * @param clazzValue
	 *            Le type de valeur
	 * @return La liste de valeurs (les modifications ne s'appliquent pas � la
	 *         configuration)
	 */
	public <T extends MapValue<K>, K> MapList<T, K> getValueList(String key, Class<T> clazzValue);

	/**
	 * R�cup�re une liste de valeurs
	 * 
	 * @param key
	 *            La cl� de la liste
	 * @param clazzValue
	 *            Le type de valeur
	 * @param def
	 *            La valeur par d�faut (sera set si non trouv�e)
	 * @return La liste de valeurs (les modifications ne s'appliquent pas � la
	 *         configuration)
	 */
	public <T extends MapValue<K>, K> MapList<T, K> getValueList(String key, Class<T> clazzValue, List<T> def);

	/**
	 * Sauvegarde la configuration sous forme de JsonObject
	 * 
	 * @return Le JsonObject
	 */
	public JsonObject save();

	/**
	 * Sauvegarde la ocnfiguration dans un fichhier
	 * 
	 * @param file
	 *            Le fichier
	 */
	public void save(File file);

	/**
	 * D�finit une valeur
	 * 
	 * @param key
	 *            La cl� de la valeur
	 * @param value
	 *            La valeur
	 */
	public <T extends MapValue<?>> void setValue(String key, T value);

	/**
	 * D�finit une liste de valeurs
	 * 
	 * @param key
	 *            La cl� de la liste
	 * @param value
	 *            La liste de valeurs
	 */
	public <T extends MapValue<?>> void setValueList(String key, Collection<T> value);
}
