package fr.badblock.rabbitconnector;

public enum RabbitAvailabilityType {

	/**
	 * Persistence temporaire (TTL � pr�ciser, 10 secondes par d�faut)
	 */
	TEMPORARY_PERSISTENT,
	/**
	 * Persistence r�currente, le message restera persistant jusqu'� son traitement
	 */
	RECURRENT_PERSISTENCE,
	/**
	 * Pas de persistance
	 */
	NO_PERSISTENCE;
	
}
