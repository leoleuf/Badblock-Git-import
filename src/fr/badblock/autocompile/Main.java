package fr.badblock.autocompile;

import java.io.File;

import org.eclipse.jetty.server.Server;

import fr.badblock.autocompile.compile.AutoCompiler;
import fr.badblock.autocompile.compile.Configuration;
import fr.badblock.autocompile.utils.JsonUtils;

public class Main {
	private static boolean working   = false;
	private static boolean workAfter = false;
	
	public static void main(String[] args){
		new Thread(){
			@Override
			public void run(){
				try {
					Server server = new Server(getConfig().port);

					server.setHandler(new AutoCompilerHttpHandler());
					server.start();
					server.join();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
		
		doJob(null);
	}
	
	public static void doJob(String targetName)
	{
		if(working){
			workAfter = true;
			return;
		}
		
		working = true;
		
		Configuration configuration = getConfig();
		new AutoCompiler(configuration, targetName);
		
		working = false;
		
		if(workAfter){
			workAfter = false;
			doJob(targetName);
		}
	}
	
	public static Configuration getConfig(){
		Configuration configuration = JsonUtils.load(new File("config.json"), Configuration.class);

		JsonUtils.save(new File("config.json"), configuration, true);

		return configuration;
	}
}
