package fr.badblock.common.autocompile.compile;

public class AutoCompiler {
	
	private ByProject[]		projects;
	
	public AutoCompiler(Configuration configuration, String targetName)
	{
		this.projects = configuration.projects;
		for (ByProject byProject : projects)
		{
			if (targetName != null && targetName.equalsIgnoreCase(byProject.folder))
			{
				continue;
			}
			new ProjectLoader(byProject);
		}
	}

}
