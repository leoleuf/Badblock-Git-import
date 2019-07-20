package fr.badblock.common.messages.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import fr.badblock.common.messages.configuration.MessageToken.TokenType;

public class MessageTokenizer
{
	private static boolean isWord(char c)
	{
		return Character.isAlphabetic(c) || Character.isDigit(c) || c == '_';
	}
	
	private String path;
	private String[] lines;

	private int currentLine;
	private int currentPos;

	private int startLine;
	private int startPos;

	private StringBuilder token;
	private MessageToken current;

	public MessageTokenizer(String path) throws IOException
	{
		this.lines = Files.readAllLines(Paths.get(path)).toArray(new String[0]);
	}

	public MessageTokenizer(File path) throws IOException
	{
		this(path.getPath());
	}

	protected RuntimeException badToken()
	{
		return new RuntimeException("unexpected token at line " + currentLine + " column " + currentPos + " in " + path);
	}
	
	protected void startToken()
	{
		this.token = new StringBuilder();
		this.startLine = this.currentLine;
		this.startPos = this.currentPos;
	}

	protected MessageToken endToken(TokenType type)
	{
		current = new MessageToken(type, token.toString(), this.startPos, this.startLine);
		return current;
	}
	
	protected char readChar()
	{
		if (currentLine >= this.lines.length)
			return '\0';

		if (currentPos >= this.lines[this.currentLine].length())
		{
			currentPos = 0;
			currentLine++;

			return readChar();
		}

		return this.lines[this.currentLine].charAt(currentPos);
	}
	
	protected void consumeChar()
	{
		this.token.append(readChar());
		this.currentPos++;
	}

	protected void forgetChar()
	{
		this.currentPos++;
	}
	
	public MessageToken readVarToken()
	{
		if (current != null)
			return current;

		startToken();
		char c = readChar();

		if (c == ' ')
			c = ignoreBlanks();

		if (c == '.')
		{
			consumeChar();
			return endToken(TokenType.VAR_DOT);
		}

		if (c == ':')
		{
			consumeChar();
			return endToken(TokenType.VAR_SEP);
		}

		if (c == '=')
		{
			consumeChar();
			return endToken(TokenType.ASSIGN);
		}
		
		if (c == '}')
		{
			consumeChar();
			return endToken(TokenType.VAR_STOP);
		}

		if (c == '\n')
		{
			consumeChar();
			return endToken(TokenType.ENDLINE);
		}

		if (!isWord(c))
			throw badToken();

		while (isWord(c))
		{
			consumeChar();
			c = readChar();
		}

		return endToken(TokenType.WORD);
	}

	private char ignoreBlanks()
	{
		char c = readChar();

		while (c == ' ')
		{
			forgetChar();
			c = readChar();
		}

		return c;
	}
	
	public MessageToken readToken()
	{
		if (current != null)
			return current;

		char c = readChar();

		if (c == '{')
		{
			consumeChar();
			return endToken(readChar() == '/' ? TokenType.VAR_END : TokenType.VAR_START);
		}

		if (c == '\n')
		{
			consumeChar();
			return endToken(TokenType.ENDLINE);
		}

		if (c == '-')
		{
			consumeChar();
			
			if (readChar() == '-')
			{
				consumeChar();

				if (readChar() == '-')
				{
					consumeChar();
					return endToken(TokenType.MESSAGE_START);
				}
			}
		}

		while (c != '{' && c != '-' && c != '\n')
		{
			consumeChar();
			c = readChar();
		}

		return endToken(TokenType.WORD);
	}

	public void consumeToken()
	{
		current = null;
	}

	public void eatToken(TokenType type)
	{
		if (readToken().type != type)
			throw new RuntimeException("expected token of type " + type + ", not " + readToken().type);

		consumeToken();
	}

	public String eatVarToken(TokenType type)
	{
		if (readVarToken().type != type)
			throw new RuntimeException("expected token of type " + type + ", not " + readVarToken().type);

		String value = current.value;
		consumeToken();
		return value;
	}
}