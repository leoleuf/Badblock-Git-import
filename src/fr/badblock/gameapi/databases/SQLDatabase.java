package fr.badblock.gameapi.databases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import fr.badblock.gameapi.utils.general.Callback;

/**
 * Repr�sente la base de donn�e SQL
 * @author LeLanN
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
	 * Envoit une requ�te de query
	 * @param request La requ�te
	 * @return La r�ponse
	 */
	public ResultSet query(String request) throws Exception;
	
	/**
	 * Envoit une requ�te de query
	 * @param request La requ�te
	 * @param callback Le callback pour la r�ponse
	 */
	public void queryAsynchronously(String request, Callback<ResultSet> callback);
	
	/**
	 * Envoit une requ�te d'update
	 * @param request La requ$ete
	 * @param synchronously Si la requ�te doit �tre fait de mani�re synchrone
	 */
	public void update(String request, boolean synchronously) throws Exception;
}
