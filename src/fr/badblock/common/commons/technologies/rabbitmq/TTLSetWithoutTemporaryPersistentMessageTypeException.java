package fr.badblock.common.commons.technologies.rabbitmq;

public class TTLSetWithoutTemporaryPersistentMessageTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5351047067020698420L;

	public TTLSetWithoutTemporaryPersistentMessageTypeException() {
		super("A TTL is set without the temporary persistent type.");
	}
	
}
