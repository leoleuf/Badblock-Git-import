package fr.badblock.gameapi.commands;

import com.mojang.brigadier.context.CommandContext;

/**
 * Liste d'arguments pour une commande
 * @author LeLanN
 *
 * @param <T>
 */
public class ArgumentList<T>
{
	public final CommandContext<Object> context;
	private ISourceCastFunction<T> cast;
	
	public ArgumentList(CommandContext<Object> context, ISourceCastFunction<T> cast)
	{
		this.context = context;
		this.cast = cast;
	}

	/**
	 * @return Source ayant lancé la commande
	 */
	public T getSource()
	{
		return cast.cast(context.getSource());
	}

	/**
	 * @param <V>
	 * @param type
	 * @return Source ayant lancé la commande, dans une autre classe.
	 */
	@SuppressWarnings("unchecked")
	public <V> V getSourceAs(Class<T> type)
	{
		return (V) getSource();
	}
	
	/**
	 * Récupère un argument de la commande avec une valeur par défaut (utile pour les arguments
	 * optionels)
	 * @param <V>
	 * @param name Le nom de l'argument
	 * @param clazz La classe dans laquelle l'argument doit être récupéré
	 * @param def La valeure par défaut
	 * @return
	 */
	public <V> V get(String name, Class<V> clazz, V def)
	{
		try
		{
			return context.getArgument(name, clazz);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	/**
	 * Comme ${@link #get(String, Class, Object)} sans valeur par défaut
	 * @param <V>
	 * @param name
	 * @param clazz
	 * @return
	 */
	public <V> V get(String name, Class<V> clazz)
	{
		return context.getArgument(name, clazz);
	}
}
