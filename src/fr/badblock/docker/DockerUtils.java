package fr.badblock.docker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DockerUtils {

	/**
	 * Fetch all timestamps of all files and subfolders
	 * @param folder > the parent folder of that fucking shit
	 * @return map of files names and last modified timestamps
	 */
	public static Map<String, Long> getFileTimestamps(String folder, String... excludedFolders) { 
		File file = new File(folder);
		if (!file.exists()) return new HashMap<>();
		ArrayList<File> files = new ArrayList<>();
		listf(folder, files, excludedFolders);
		Map<String, Long> result = new HashMap<>();
		for (File filze : files) {
			if (filze == null) continue;
			boolean oops = false;
			for (String excludedFolder : excludedFolders)
				if (filze.getAbsolutePath().startsWith(excludedFolder)) {
					oops = true;
					break;
				}
			if (oops) continue;
			result.put(filze.getAbsolutePath().replace(folder, ""), filze.lastModified());
		}
		return result;
	}

	private static void listf(String directoryName, ArrayList<File> files, String... excludedFolders) {
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();
		for (File file : fList) {
			boolean oops = false;
			for (String excludedFolder : excludedFolders)
				if (file.getAbsolutePath().startsWith(excludedFolder)) {
					oops = true;
					break;
				}
			if (oops) continue;
			if (file.isFile()) {
				files.add(file);
			} else if (file.isDirectory()) {
				listf(file.getAbsolutePath(), files);
			}
		}
	}

}
