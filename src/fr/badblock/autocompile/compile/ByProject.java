package fr.badblock.autocompile.compile;

import java.util.Map;

import fr.badblock.autocompile.compile.Configuration.Dependency;
import fr.badblock.autocompile.compile.Configuration.Project;

public class ByProject
{

	public String		folder		  = "API";
	public String       result		  = "GameAPI";
	public String    	exampleFolder = "example";
	public String       gitUrl		  = "http://username:password@gitlab.badblock-network.fr/";
	public Project[]    projects	  = new Project[0];
	public Dependency[] depends	      = new Dependency[0];

	public String 	 	manifestLink  = "../lib/";
	
	public String    	resultrepos   = "http://vps446463.ovh.net/Binaries/BinariesAPI.git";
	public String    	resultname    = "GameAPI";
	public String    	resultjarname = "GameAPI_v1.8.8.jar";
	
	public Project		projectAddLib;
	public String[]		toPaths;

	public String			   projectForCommitId;
	public Map<String, String> manifestData;
	
	public int				   buildId = 0;
	
}
