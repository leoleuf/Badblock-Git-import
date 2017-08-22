package fr.badblock.rabbitconnector;

import fr.badblock.commons.technologies.rabbitmq.RabbitMessage;
import fr.badblock.commons.technologies.rabbitmq.RabbitPacketType;
import fr.badblock.commons.utils.Encodage;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data public class RabbitPacket {

	private String 			 queueName;
	private Encodage		 encodage;
	private RabbitPacketType type;
	private boolean			 debug;
	private RabbitMessage	 rabbitMessage;
	
}
