package fr.badblock.commons.rabbitconnector;

/**
 * An enumeration for set a custom time to live in Rabbit messages
 * @author xMalware
 */
public enum RabbitAvailabilityType {

	/**
	 * Temporary persistence (timeToLive should be set, ten seconds for expire by default)
	 */
	TEMPORARY_PERSISTENT,
	/**
	 * Reccurrent persistence, the message will keeping alive util the pull of it
	 */
	RECURRENT_PERSISTENT,
	/**
	 * No persistence
	 */
	NO_PERSISTENT;
	
}
