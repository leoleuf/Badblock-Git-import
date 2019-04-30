package fr.badblock.gameapi.commands;

/**
 * Interface fonctionnelle représentant une exécution de commande
 * @author LeLanN
 *
 * @param <T>
 */
@FunctionalInterface
public interface ICommandFunction<T>
{
	public int executeCommand(ArgumentList<T> list);
}
