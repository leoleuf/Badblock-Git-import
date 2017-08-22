package fr.badblock.commons.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * Classe contenant plusieurs m�thodes utiles pour l'utilisation des nombres
 * 
 * @author LeLanN
 */
public class MathsUtils {
	/**
	 * Ajoute un certains pourcentage � une valeur de base
	 * 
	 * @param base
	 *            La valeur de base
	 * @param percent
	 *            Le pourcentage � ajouter
	 * @return La nouvelle valeur
	 */
	public static double addPercentage(double base, double percent) {
		return (1d + percent / 100d) * base;
	}

	/**
	 * Ajoute un certains pourcentage, un certains nombre de fois, � une valeur
	 * de base
	 * 
	 * @param base
	 *            La valeur de base
	 * @param percent
	 *            Le pourcentage � ajouter
	 * @param n
	 *            Le nombre de fois o� il faut ajouter ce pourcentage
	 * @return La nouvelle valeur
	 */
	public static double addPercentage(double base, double percent, int n) {
		return Math.pow(1d + percent / 100d, n) * base;
	}

	/**
	 * Renvoit un nombre al�atoire
	 *
	 * @param max
	 *            Le maximum
	 * @param min
	 *            Le minimum
	 * @return Le nombre
	 */
	public static double doubleRandomInclusive(double max, double min) {
		if (max < min) {
			double tmp = min;
			min = max;
			max = tmp;
		}

		double r = Math.random();
		if (r < 0.5) {
			return ((1 - Math.random()) * (max - min) + min);
		}
		return (Math.random() * (max - min) + min);
	}

	/**
	 * Renvoit un nombre al�atoire
	 *
	 * @param max
	 *            Le maximum
	 * @param min
	 *            Le minimum
	 * @return Le nombre
	 */
	public static int integerRandomInclusive(int max, int min) {
		if (max < min) {
			int tmp = min;
			min = max;
			max = tmp;
		}

		return new Random().nextInt(max) + min;
	}

	/**
	 * Arrondit un nombre � un certains nombre de d�cimales
	 * 
	 * @param number
	 *            La valeur
	 * @param dec
	 *            Le nombre de d�cimales
	 * @return La nouvelle valeur
	 */
	public static double round(double number, int dec) {
		if (dec < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(number);
		bd = bd.setScale(dec, RoundingMode.HALF_UP);

		return bd.doubleValue();
		// int div = (int) Math.pow(10, dec);
		// return (double) ((int)(number * div)) / (double) div;
	}
}
