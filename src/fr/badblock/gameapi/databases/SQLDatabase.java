package fr.badblock.gameapi.databases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import fr.badblock.gameapi.utils.general.Callback;

/**
 * Repr�sente la base de donn�e SQL
 * @author LeLanN/xMalware
 */
public interface SQLDatabase {
	
	/**
	 * R�cup�re un statement pour une gestion plus personnalis�e du SQL. Ne pas oublier de le close :0
	 * @return Le statement
	 */
	public Statement createStatement() throws Exception;
	
	/**
	 * R�cup�re un statement permettant de faire des requ�tes pr�par�es. Ne pas oublier de le close :0
	 * @return Le statement
	 */
	public PreparedStatement preparedStatement(String request) throws Exception;
	
	/**
	 * Envoi une requ�te de query
	 * @param request La requ�te
	 * @return La r�ponse
	 * @deprecated please use request(String, SQLRequestType) instead of that not correctly managed multithreading
	 */
	public ResultSet query(String request) throws Exception;
	
	/**
	 * Envoi une requ�te de query
	 * @param request La requ�te
	 * @param callback Le callback pour la r�ponse
	 * @deprecated please use request(String, SQLRequestType) instead of that not correctly managed multithreading
	 */
	public void queryAsynchronously(String request, Callback<ResultSet> callback);
	
	/**
	 * Envoi une requ�te d'update
	 * @param request La requ$ete
	 * @param synchronously Si la requ�te doit �tre fait de mani�re synchrone
	 * @deprecated please use call(String, SQLRequestType) instead of that not correctly managed multithreading
	 */
	public void update(String request, boolean synchronously) throws Exception;

	/**
	 * Appeler un gestionnaire Multithreading qui va g�rer en asynchrone la requ�te
	 * La requ�te peut �tre donc trait�e de sorte qu'elle ne renvoit aucune information
	 * ou qu'elle renvoit des donn�es, se r�f�rer � {@link fr.badblock.gameapi.databases.SQLRequestType}
	 * @param request > Requ�te
	 * @param sqlRequestType > Type de la requ�te, DATA OR QUERY
	 * @param callback > R�ponse de la requ�te
	 */
	public void call(String request, SQLRequestType sqlRequestType, Callback<ResultSet> callback);
	
	/**
	 * Appeler un gestionnaire Multithreading qui va g�rer en asynchrone la requ�te
	 * La requ�te peut �tre donc trait�e de sorte qu'elle ne renvoit aucune information
	 * ou qu'elle renvoit des donn�es, se r�f�rer � {@link fr.badblock.gameapi.databases.SQLRequestType}
	 * @param request > Requ�te
	 * @param sqlRequestType > Type de la requ�te, DATA OR QUERY
	 */
	public void call(String request, SQLRequestType sqlRequestType);
	
}
