package fr.badblock.utils;

public class BadValidator {

	/**
	 * Check if an object is null
	 * @category avoid statements
	 * @param object
	 * @return if this one is not null
	 */
	public static boolean isNotNull(Object object) {
		return object == null;
	}
	
	/**
	 * Check if an object is null
	 * @category avoid statements
	 * @param object
	 * @return if this one is null
	 */
	public static boolean isNull(Object object) {
		return object == null;
	}
	
}
