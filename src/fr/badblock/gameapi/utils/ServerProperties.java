package fr.badblock.gameapi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Classe utilitaire permettant de r�cup�rer et d�finir facilement des donn�es configur�es dans le server.properties
 * @author LeLanN
 */
public class ServerProperties {
	private final static Properties properties;
	private final static File		propertiesFile = new File("server.properties");
	static {
		properties = new Properties();

		try {
			properties.load(new FileInputStream(propertiesFile));
		} catch (IOException e) {
			System.out.println("Can not read " + propertiesFile.getName() + " file :");
			e.printStackTrace();
		}
	}

	/**
	 * R�cup�re les donn�es
	 * @return {@link Properties}
	 */
	public static Properties getProperties(){
		return properties;
	}
	
	/**
	 * Sauvegarde les donn�es
	 */
	public static void saveProperties(){
		try {
			properties.store(new FileOutputStream(propertiesFile), "");
		} catch (IOException e) {
			System.out.println("Can not save " + propertiesFile.getName() +  "file :");
			e.printStackTrace();
		}
	}
}
