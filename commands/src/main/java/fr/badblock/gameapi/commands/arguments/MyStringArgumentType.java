package fr.badblock.gameapi.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * Utilitaire pour représenter un argument 'simple' (n'importe quelle séquence entre deux espaces)
 * @author LeLanN
 */
public class MyStringArgumentType implements ArgumentType<String>
{
	public static ArgumentType<String> simpleWord()
	{
		return new MyStringArgumentType();
	}
	
	@Override
	public String parse(StringReader reader) throws CommandSyntaxException
	{
		final int start = reader.getCursor();

		while (reader.canRead() && reader.peek() != ' ')
		{
			reader.skip();
		}

		return reader.getString().substring(start, reader.getCursor());
	}
}
