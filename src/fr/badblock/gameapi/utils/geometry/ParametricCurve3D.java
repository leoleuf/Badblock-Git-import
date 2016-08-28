package fr.badblock.gameapi.utils.geometry;

import java.util.ArrayList;
import java.util.List;

import fr.badblock.gameapi.utils.selections.Vector3f;

/**
 * Repr�sente une courbe param�tr�e tridimensionnelle pour �tre utiliser pour
 * des particules, par <b>exemple</b>. La courbe peut �tre bidimensionnelle, il
 * suffit de d�finir une de ses coordonn�es comme fixe.<br>
 * On rappel qu'une courbe param�tr�e peut �tre :
 * <ul>
 * <li>De la famille des cercles (cercle, ellipse, spirale) et leurs �quivalents
 * tridimensionnels</li>
 * <li>Une droite</li>
 * <li>Pleins de choses g�niales que je ne pourrais pas d�crire ... ;)</li>
 * </ul>
 * 
 * @author LeLanN
 */
public abstract class ParametricCurve3D {
	/**
	 * Renvoit la coordonn�e x en fonction de t
	 * 
	 * @param t
	 *            La variable t c'est � dire la 'position' dans la courbe
	 *            param�trique
	 * @return La coordonn�e
	 */
	public abstract double x(double t);

	/**
	 * Renvoit la coordonn�e y en fonction de t
	 * 
	 * @param t
	 *            La variable t c'est � dire la 'position' dans la courbe
	 *            param�trique
	 * @return La coordonn�e
	 */
	public abstract double y(double t);

	/**
	 * Renvoit la coordonn�e z en fonction de t
	 * 
	 * @param t
	 *            La variable t c'est � dire la 'position' dans la courbe
	 *            param�trique
	 * @return La coordonn�e
	 */
	public abstract double z(double t);

	public final List<Vector3f> getPoints(double tMinValue, double tMaxValue, int pointsCount) {
		List<Vector3f> result = new ArrayList<>();
		double add = (tMaxValue - tMinValue) / pointsCount;

		for (double t = tMinValue; t <= tMaxValue; t += add) {
			try {
				result.add(new Vector3f(x(t), y(t), z(t)));
			} catch (Exception e) {
				/*
				 * Si t est une valeur interdite d'une des fonctions ... bobo ;)
				 */
			}
		}

		return result;
	}
}
