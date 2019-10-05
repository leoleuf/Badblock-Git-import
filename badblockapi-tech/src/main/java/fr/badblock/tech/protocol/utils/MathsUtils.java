package fr.badblock.tech.protocol.utils;

/**
 * Classe contenant plusieurs m�thodes utiles pour l'utilisation des nombres
 * @author LeLanN
 */
public class MathsUtils {
	/**
	 * Ajoute un certains pourcentage � une valeur de base
	 * @param base La valeur de base
	 * @param percent Le pourcentage � ajouter
	 * @return La nouvelle valeur
	 */
	public static double addPercentage(double base, double percent){
		return (1d + percent / 100d) * base;
	}
	
	/**
	 * Ajoute un certains pourcentage, un certains nombre de fois, � une valeur de base
	 * @param base La valeur de base
	 * @param percent Le pourcentage � ajouter
	 * @param n Le nombre de fois o� il faut ajouter ce pourcentage
	 * @return La nouvelle valeur
	 */
	public static double addPercentage(double base, double percent, int n){
		return Math.pow(1d + percent / 100d, n) * base;
	}
	
	/**
	 * Arrondit un nombre � un certains nombre de d�cimales
	 * @param number La valeur
	 * @param dec Le nombre de d�cimales
	 * @return La nouvelle valeur
	 */
	public static double round(double number, int dec){
		int div = (int) Math.pow(10, dec);
		return (double) ((int)(number * div)) / (double) div;
	}
	
	/**
	 * R�cup�re la plus petite valeur entre deux valeurs
	 * @param one La premi�re
	 * @param scd La seconde
	 * @return La plus petite
	 */
	public static double min(double one, double scd){
		return one < scd ? one : scd;
	}
	
	/**
	 * R�cup�re la plus grande valeur entre deux valeurs
	 * @param one La premi�re
	 * @param scd La seconde
	 * @return La plus grande
	 */
	public static double max(double one, double scd){
		return one > scd ? one : scd;
	}
}
