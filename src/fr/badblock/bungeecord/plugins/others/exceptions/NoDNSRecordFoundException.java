package fr.badblock.bungeecord.plugins.others.exceptions;

public class NoDNSRecordFoundException extends RuntimeException {

	private static final long serialVersionUID = 3619074117853391198L;

	public NoDNSRecordFoundException(int recordId) {
		super("No DNS record found (" + recordId + ")");
	}

}
