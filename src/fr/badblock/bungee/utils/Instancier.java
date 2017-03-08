package fr.badblock.bungee.utils;

import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import fr.badblock.bungee.SingletonInstancier;

public class Instancier {

	public static void instanciate(SingletonInstancier singletonInstancier, String... paths) throws IOException {
		URL url = singletonInstancier.getClass().getProtectionDomain().getCodeSource().getLocation();

		ZipInputStream zip = new ZipInputStream(url.openStream());
		ZipEntry entry = null;

		while((entry = zip.getNextEntry()) != null)
		{
			String finded = null;
			
			for(String path : paths){
				if(entry.getName().startsWith( path.replace(".", "/") ))
				{
					finded = entry.getName().replace("/", ".");
					break;
				}}

			if(finded != null && entry.getName().endsWith(".class"))
			{
				try {
					String className = finded.substring(0, finded.length() - 6);

					Class<?> clazz = singletonInstancier.getClass().getClassLoader().loadClass(className);
					instanciate(clazz);
				} catch(Exception e){
					e.printStackTrace();
				}
			}

		}
	}

	// Not used a this time :(
	@SuppressWarnings("unused")
	private static boolean inheritFrom(Class<?> clazz, Class<?> from){
		while(clazz != Object.class)
		{
			if(clazz == from)
				return true;

			clazz = clazz.getSuperclass();
		}

		return false;
	}

	private static Object instanciate(Class<?> clazz) throws Exception {
		try {
			return clazz.getConstructor().newInstance();
		} catch(NoSuchMethodException e){
			return null;
		}
	}
	
}
