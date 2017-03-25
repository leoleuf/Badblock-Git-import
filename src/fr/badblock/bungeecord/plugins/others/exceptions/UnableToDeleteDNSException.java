package fr.badblock.bungeecord.plugins.others.exceptions;

public class UnableToDeleteDNSException extends RuntimeException {

	private static final long serialVersionUID = 3619074117853391198L;

	public UnableToDeleteDNSException() {
		super("Unable to delete DNS :(");
	}
	
}
