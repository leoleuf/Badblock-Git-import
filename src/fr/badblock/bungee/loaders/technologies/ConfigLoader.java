package fr.badblock.bungee.loaders.technologies;

import java.io.File;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public abstract class ConfigLoader {

	private ConfigurationProvider cp = ConfigurationProvider.getProvider(YamlConfiguration.class);
	
	public ConfigLoader(File folder, String fileName) {
		try {
			// If the folder doesn't exists
			if (!folder.exists())
				folder.mkdirs(); // create it
			File f = new File(folder, fileName);
			// If the file doesn't exists
			if(!f.exists())
				f.createNewFile(); // create it
			Configuration configuration = cp.load(f);
			done(configuration);
			/*
			 * 
			// Bungee
			String bungeeName = config.getString("bungeeName");
			this.bungeeName = bungeeName;
			System.out.println(this.bungeeName);
			// RabbitMQ
			loadRabbitMQ();
			// MongoDB
			loadMongoDB();
			// Load Redis
			loadRedis();
			 */
		} catch(Exception e){
			e.printStackTrace();
			return;
		}
	}
	
	public abstract void done(Configuration configuration);	
	
}
