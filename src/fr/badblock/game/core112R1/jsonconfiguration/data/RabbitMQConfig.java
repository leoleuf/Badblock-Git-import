package fr.badblock.game.core112R1.jsonconfiguration.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RabbitMQConfig {

	// Rabbit part
	public String[] rabbitHostname;
	public int rabbitPort;
	public String rabbitUsername;
	public String rabbitPassword;
	public String rabbitVirtualHost;

}