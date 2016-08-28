package fr.badblock.gameapi.databases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import fr.badblock.gameapi.utils.general.Callback;

/**
 * Repr�sente la base de donn�e SQL
 * 
 * @author LeLanN/xMalware
 */
public interface SQLDatabase {

	/**
	 * R�cup�re un statement pour une gestion plus personnalis�e du SQL. Ne pas
	 * oublier de le close :0
	 * 
	 * @return Le statement
	 */
	public Statement createStatement() throws Exception;

	/**
	 * R�cup�re un statement permettant de faire des requ�tes pr�par�es. Ne pas
	 * oublier de le close :0
	 * 
	 * @return Le statement
	 */
	public PreparedStatement preparedStatement(String request) throws Exception;

	/**
	 * Appeler un gestionnaire Multithreading qui va g�rer en asynchrone la
	 * requ�te La requ�te peut �tre donc trait�e de sorte qu'elle ne renvoit
	 * aucune information ou qu'elle renvoit des donn�es, se r�f�rer �
	 * {@link fr.badblock.gameapi.databases.SQLRequestType}
	 * 
	 * @param request
	 *            > Requ�te
	 * @param sqlRequestType
	 *            > Type de la requ�te, DATA OR QUERY
	 * @param callback
	 *            > R�ponse de la requ�te
	 */
	public void call(String request, SQLRequestType sqlRequestType, Callback<ResultSet> callback);

	/**
	 * Appeler un gestionnaire Multithreading qui va g�rer en asynchrone la
	 * requ�te La requ�te peut �tre donc trait�e de sorte qu'elle ne renvoit
	 * aucune information ou qu'elle renvoit des donn�es, se r�f�rer �
	 * {@link fr.badblock.gameapi.databases.SQLRequestType}
	 * 
	 * @param request
	 *            > Requ�te
	 * @param sqlRequestType
	 *            > Type de la requ�te, DATA OR QUERY
	 */
	public void call(String request, SQLRequestType sqlRequestType);

}
