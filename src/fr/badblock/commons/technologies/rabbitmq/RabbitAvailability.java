package fr.badblock.commons.technologies.rabbitmq;

import lombok.Data;

/**
 * A class who arrange the availability of each message who contains these availability data
 * @author xMalware
 */
@Data
public class RabbitAvailability {

	// The availability type
	private RabbitAvailabilityType  rabbitAvailabilityType;
	// The time to live, if reached out, the message will be dead
	private long 					timeToLive;

	/**
	 * Constructor of RabbitAvailabity who let have an availability state for each message
	 * @param rabbitAvailabilityType > the availability type, very important
	 * @param timeToLive > the time to live
	 * @throws TTLSetWithoutTemporaryPersistentMessageTypeException > an exception who is fired if the developer set a TTL to recurrent/no persistent messages
	 */
	public RabbitAvailability(RabbitAvailabilityType rabbitAvailabilityType, long timeToLive) throws TTLSetWithoutTemporaryPersistentMessageTypeException {
		if (!rabbitAvailabilityType.equals(RabbitAvailabilityType.TEMPORARY_PERSISTENT) && timeToLive != -1)
			throw new TTLSetWithoutTemporaryPersistentMessageTypeException();
		this.setRabbitAvailabilityType(rabbitAvailabilityType);
		this.setTimeToLive(rabbitAvailabilityType.equals(RabbitAvailabilityType.RECURRENT_PERSISTENT) ? Long.MAX_VALUE : timeToLive);
	}
	
	/**
	 * Constructor of RabbitAvailabity without TTL, who let have an availability state for each message
	 * The developer should use this constructor if he doesn't need to set a temporary persistence with these specific values
	 * @param rabbitAvailabilityType > the availability type, very important
	 * @throws TTLSetWithoutTemporaryPersistentMessageTypeException > an exception who is fired if the developer set a TTL to recurrent/no persistent messages
	 */
	public RabbitAvailability(RabbitAvailabilityType rabbitAvailabilityType) throws TTLSetWithoutTemporaryPersistentMessageTypeException {
		this(rabbitAvailabilityType, -1);
	}

}
