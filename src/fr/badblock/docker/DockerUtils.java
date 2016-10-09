package fr.badblock.docker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DockerUtils {

	/**
	 * Fetch all timestamps of all files and subfolders
	 * @param folder > the parent folder of that fucking shit
	 * @return map of files names and last modified timestamps
	 */
	public static Map<String, Long> getFileTimestamps(String folder) { 
		File file = new File(folder);
		if (!file.exists()) return new HashMap<>();
		ArrayList<File> files = new ArrayList<>();
		listf(folder, files);
		Map<String, Long> result = new HashMap<>();
		for (File filze : files) {
			if (filze == null) continue;
			System.out.println("METHOD > " + filze.getAbsolutePath() + " / " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new File(filze.getAbsolutePath()).lastModified()));
			result.put(filze.getAbsolutePath().replace(folder, ""), filze.lastModified());
		}
		return result;
	}
	
	private static void listf(String directoryName, ArrayList<File> files) {
	    File directory = new File(directoryName);
	    File[] fList = directory.listFiles();
	    for (File file : fList) {
	        if (file.isFile()) {
	            files.add(file);
	        } else if (file.isDirectory()) {
	            listf(file.getAbsolutePath(), files);
	        }
	    }
	}
	
}
