package fr.badblock.bungee;

import lombok.Getter;
import lombok.Setter;

/**
 * This SingletonInstancier class is at the main package and it's useful to instanciate from this location some classes/packages by using {@link fr.badblock.bungee.utils.Instancier}
 * @author root
 *
 */
public class SingletonInstancier {
	
	@Getter @Setter private static SingletonInstancier	instance = new SingletonInstancier();

}
