package fr.badblock.gameapi.commands;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import fr.badblock.gameapi.commands.arguments.MyStringArgumentType;
import fr.badblock.gameapi.commands.exceptions.InvalidCommandException;
import lombok.Getter;

/**
 * Commande ou sous-commande
 * @author LeLanN
 *
 * @param <T>
 */
public class CommandNode<T>
{
	@Getter
	private final String name;
	@Getter
	private final String description;
	@Getter
	private String permission;
	@Getter
	private Class<? extends T> sourceClass;

	private Map<String, ArgumentType<?>> arguments; 
	private List<CommandNode<T>> subCommands;

	private int firstOptional = -1;
	private ICommandFunction<T> def = null;

	public CommandNode(String name, String description)
	{
		this.name = name;
		this.description = description;

		this.arguments = new LinkedHashMap<>();
		this.subCommands = new ArrayList<>();
	}

	/**
	 * Définis la classe dont doit hériter la source pour l'acceptée. Par exemple (sur Bukkit)
	 * setSourceClass(Player.class) permettra d'être certain qu'un joueur a lancé la commande
	 * (et non la console / un command block).
	 * @param clazz
	 */
	public void setSourceClass(Class<? extends T> clazz)
	{
		this.sourceClass = clazz;
	}
	
	/**
	 * Ajoute un argument à la commande
	 * @param name
	 * @param type
	 */
	public void addArgument(String name, ArgumentType<?> type)
	{
		this.arguments.put(name, type);
	}

	/**
	 * Ajoute un argument 'simple' (n'importe quelle String entre deux espaces)
	 * @param name
	 */
	public void addSimpleArgument(String name)
	{
		addArgument(name, MyStringArgumentType.simpleWord());
	}
	
	/**
	 * Ajoute un mot ([a-zA-Z0-9_+.-]*)
	 * @param name
	 */
	public void addWordArgument(String name)
	{
		addArgument(name, StringArgumentType.word());
	}

	/**
	 * Ajoute une String entres guillemets
	 * @param name
	 */
	public void addStringArgument(String name)
	{
		addArgument(name, StringArgumentType.string());
	}
	
	/**
	 * String jusqu'à la fin de la ligne
	 * @param name
	 */
	public void addGreedyStringArgument(String name)
	{
		addArgument(name, StringArgumentType.greedyString());
	}
	
	/**
	 * Un entier sur 32 bits
	 * @param name
	 */
	public void addIntegerArgument(String name)
	{
		addArgument(name, IntegerArgumentType.integer());
	}

	/**
	 * Un entier sur 32 bits >= min
	 * @param name
	 * @param min
	 */
	public void addIntegerArgument(String name, int min)
	{
		addArgument(name, IntegerArgumentType.integer(min));
	}

	/**
	 * Un entier sur 32 bits dans [min; max]
	 * @param name
	 * @param min
	 * @param max
	 */
	public void addIntegerArgument(String name, int min, int max)
	{
		addArgument(name, IntegerArgumentType.integer(min, max));
	}

	/**
	 * Ajoute une sous-commande à la commande
	 * @param command
	 */
	public void addSubCommand(CommandNode<T> command)
	{
		this.subCommands.add(command);
	}

	/**
	 * Définis la commande actuelle comme étant exécutable. Les arguments ajoutés après l'appel à cette méthode
	 * seront considérés comme optionels
	 * @param def
	 */
	public void setExecutable(ICommandFunction<T> def)
	{
		this.firstOptional = arguments.size();
		this.def = def;
	}

	public boolean isAllowed(T source)
	{
		if (sourceClass != null)
			return sourceClass.isInstance(source);

		return true;
	}

	@SuppressWarnings("unchecked")
	public T getSource(Object o)
	{
		return (T) o;
	}

	private boolean isAllowedObj(Object o)
	{
		return isAllowed(getSource(o));
	}
	
	/**
	 * Retourne la commande sous la forme de l'API Mojang
	 * @return
	 */
	public LiteralArgumentBuilder<Object> createCommand()
	{
		if (def == null && subCommands.size() == 0)
			throw new InvalidCommandException(this + ": no execution");

		LiteralArgumentBuilder<Object> result = LiteralArgumentBuilder.literal(this.name);
		result.requires(this::isAllowedObj);

		Deque<ArgumentBuilder<Object, ?>> stack = new ArrayDeque<>();

		stack.addFirst(result);
		
		int i = 0;

		for (Map.Entry<String, ArgumentType<?>> entry : arguments.entrySet())
		{
			ArgumentBuilder<Object, ?> arg = RequiredArgumentBuilder.argument(entry.getKey(), entry.getValue());

			if (firstOptional != -1 && i >= this.firstOptional && def != null)
			{
				stack.peek().executes(c -> this.def.executeCommand(new ArgumentList<T>(c, this::getSource)));
			}

			i++;
			stack.addFirst(arg);
		}

		if (def != null)
		{
			stack.peek().executes(c -> this.def.executeCommand(new ArgumentList<T>(c, this::getSource)));
		}
		
		ArgumentBuilder<Object, ?> prev = stack.pop();

		for (CommandNode<T> sub : subCommands)
		{
			prev.then(sub.createCommand());
		}

		for (ArgumentBuilder<Object, ?> next : stack)
		{
			next.then(prev);
			prev = next;
		}
		
		return result;
	}

	@Override
	public String toString()
	{
		return String.format("[%s: %s (%s)]", this.getClass().getName(), this.name, this.description);
	}
}