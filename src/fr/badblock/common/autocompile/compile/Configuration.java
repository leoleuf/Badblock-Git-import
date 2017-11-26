package fr.badblock.common.autocompile.compile;

import java.io.File;

public class Configuration {
	
	public int			port		  = 38138;
	public ByProject[]	projects;
	
	public static class Project {
		public String  name;
		public String  gitrepos;
		public String  branch;
		public String  encode = "cp1252";
		
		public boolean removeExample;
	}
	
	public static class Dependency {
		public String  link;
		public boolean addInJar;
		public boolean addInManifest;
		
		public File getFile() {
			return new File(link);
		}
	}
}
