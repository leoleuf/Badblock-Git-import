package fr.badblock.rabbitconnector;

public enum RabbitAvailabilityType {

	/**
	 * Persistence temporaire (TTL à préciser, 10 secondes par défaut)
	 */
	TEMPORARY_PERSISTENT,
	/**
	 * Persistence récurrente, le message restera persistant jusqu'à son traitement
	 */
	RECURRENT_PERSISTENT,
	/**
	 * Pas de persistance
	 */
	NO_PERSISTENT;
	
}
