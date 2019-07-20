package fr.badblock.common.messages.configuration;

public class MessageToken
{
	public final TokenType type;
	public final String value;

	public final int startCol;
	public final int line;

	public MessageToken(TokenType type, String value, int startCol, int line)
	{
		this.type = type;
		this.value = value;
		this.startCol = startCol;
		this.line = line;
	}
	
	public static enum TokenType
	{
		WORD,
		ENDLINE,

		MESSAGE_START,

		VAR_START,
		VAR_END,
		VAR_STOP,
		VAR_DOT,
		VAR_SEP,

		OPEN_TAG,
		CLOSE_TAG,

		ASSIGN;
	}
}