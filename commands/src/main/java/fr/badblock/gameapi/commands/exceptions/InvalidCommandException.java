package fr.badblock.gameapi.commands.exceptions;

public class InvalidCommandException extends RuntimeException
{
	private static final long serialVersionUID = -686972513614972200L;

	public InvalidCommandException(String reason)
	{
		super(reason);
	}
}
