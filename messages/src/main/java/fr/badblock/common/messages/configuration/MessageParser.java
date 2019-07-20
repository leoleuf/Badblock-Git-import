package fr.badblock.common.messages.configuration;

import java.util.HashMap;
import java.util.Map;

import fr.badblock.common.messages.configuration.MessageToken.TokenType;

public class MessageParser
{
	private static Map<String, String> readVars(MessageTokenizer tkz)
	{
		Map<String, String> result = new HashMap<>();
		MessageToken tkn = tkz.readToken();
		
		while (tkn.type == TokenType.WORD)
		{
			String name = tkn.value;
			tkz.consumeToken();
			tkz.eatVarToken(TokenType.ASSIGN);
			String value = tkz.eatVarToken(TokenType.WORD);

			result.put(name, value);
		}
		
		return result;
	}
	
	public static Message readMessageHeader(MessageTokenizer tkz)
	{
		String name = tkz.eatVarToken(TokenType.WORD);
		Map<String, String> result = readVars(tkz);

		tkz.eatVarToken(TokenType.ENDLINE);

		return new Message(name, result);
	}
	
	public static Message readMessageBody(MessageTokenizer tkz)
	{
		return null;
	}
}