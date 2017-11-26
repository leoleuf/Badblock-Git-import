package fr.badblock.common.autocompile.compile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.badblock.common.autocompile.compile.Configuration.Dependency;
import fr.badblock.common.autocompile.compile.Configuration.Project;
import fr.badblock.common.autocompile.utils.JsonUtils;
import fr.badblock.common.autocompile.utils.StringUtils;

public class ProjectLoader
{

	public static final String DELOMBOKED_SOURCE = "src_delombok";
	public static final String MANIFEST_HEADER   = "Manifest-Version: 1.0" + System.lineSeparator();

	private List<String>  toExec;
	private List<File>    depends;
	private List<File>	  toExtractDepends;

	File		  builds, files, manifest, folder;

	private ByProject	  byProject;
	
	public ProjectLoader(ByProject byProject)
	{
		this.byProject		  = byProject;
		File projects = new File("projects/");
		if (!projects.exists())
		{
			projects.mkdirs();
		}
		this.folder 		  = new File(projects, byProject.folder);
		if (!this.folder.exists())
		{
			this.folder.mkdirs();
		}
		this.toExec		   	  = new ArrayList<>();
		this.depends	      = Arrays.stream(byProject.depends).map(depend -> { return depend.getFile(); }).collect(Collectors.toList());
		this.toExtractDepends = new ArrayList<>();
		this.builds		   	  = new File(folder, "builds");
		this.files		      = new File(folder, "files");
		this.manifest	      = new File(folder, "MANIFEST.MF");

		for(File file : loadOtherDepends()){
			depends.add(file);
			toExtractDepends.add(file);
		}

		toExec.add("rm -rf " + files.getAbsolutePath());
		toExec.add("rm -rf " + builds.getAbsolutePath());
		toExec.add("mkdir " + builds.getAbsolutePath());
		toExec.add("mkdir " + files.getAbsolutePath());

		compileAll(byProject.projects);
		finalize();
		saveFileTo("script.sh");
	}

	public List<File> loadOtherDepends(){
		if(byProject.projectAddLib == null){
			return new ArrayList<>();
		}

		List<File> result = new ArrayList<>();

		File otherlibs = new File(folder, "otherlibs");

		gitClone(byProject.projectAddLib.name, byProject.projectAddLib.gitrepos, byProject.projectAddLib.branch);
		toExec.add("rm -rf " + otherlibs.getAbsolutePath());
		toExec.add("cp -R libs " + otherlibs.getAbsolutePath());

		if(otherlibs.exists()){
			for(File lib : otherlibs.listFiles()){
				if(lib.isFile())
					result.add(lib);
			}
		}

		toExec.clear();

		return result;
	}

	public void saveFileTo(String name){
		File file = new File(folder, name);

		JsonUtils.save(file, StringUtils.join(toExec, System.lineSeparator()));
		file.setExecutable(true);

		try {
			Process p = new ProcessBuilder("/" + folder.getAbsolutePath() + "/" + name).start();
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void compileAll(Project[] projects){
		for(Project project : projects)
		{
			compile(project);
		}
	}


	public void compile(Project project){
		System.out.println("Build " + project.name);
		comment("Build " + project.name);

		File projectFile = new File(folder, project.name);

		gitClone(project.name, project.gitrepos, project.branch);

		goTo(projectFile);
		delombok(projectFile, "src", DELOMBOKED_SOURCE, project.encode);
		compile(projectFile, DELOMBOKED_SOURCE, project.encode);

		depends.add(createJar(project.name, null));

		goTo(projectFile);
		toExec.add("cp -R bin/* " + files.getAbsolutePath());
		toExec.add("mkdir files");
		toExec.add("cp -R files/* " + files.getAbsolutePath());
	}

	public void extractDepenencies(){
		goTo(files);

		for(Dependency dependency : byProject.depends){
			if(dependency.addInJar){
				comment("Extract depency " + dependency.getFile().getName());

				toExec.add("unzip -o " + dependency.getFile().getAbsolutePath());
				toExec.add("rm -rf META-INF");
			}
		}

		for(File file : toExtractDepends){
			comment("Extract depency " + file.getName());

			toExec.add("unzip -o " + file.getAbsolutePath());
			toExec.add("rm -rf META-INF");
		}
	}

	public void finalize(){
		System.out.println("Finalize");
		extractDepenencies();

		comment("Creating final JAR");

		createManifest(manifest);
		goTo(files);

		File file = createJar(byProject.result, manifest);

		comment("Push result");

		gitClone(byProject.resultname, byProject.resultrepos, null);
		toExec.add("cp " + file.getAbsolutePath() + " " + byProject.resultjarname);
		toExec.add("git add -A");
		toExec.add("git commit -am \"Mise à jour " + byProject.resultjarname + " (" + byProject.buildId + ")\"");
		toExec.add("git push");
		toExec.add("");
		for (String toPath : byProject.toPaths)
		{
			toExec.add("cp " + file.getAbsolutePath() + " " + toPath);
			toExec.add("chown -R package:package " + toPath);
			toExec.add("chmod +x " + toPath);
		}
	}

	public void goToHome(){
		goTo(folder);
	}

	public void goTo(File file){
		toExec.add("cd " + file.getAbsolutePath());
	}

	public void delombok(File base, String from, String to, String encode){
		toExec.add(
				"java -cp "+ buildCp(depends)
				+ " lombok.launch.Main delombok "
				+ new File(base, "src").getAbsolutePath()
				+ " -d "
				+ new File(base, "src_delombok").getAbsolutePath()
				+ " --encoding=" + encode
				);
	}

	public void compile(File base, String srcPath, String encode){
		File bin = new File(base, "bin");

		toExec.add("find " + new File(base, srcPath).getAbsolutePath() + " -name *.java -type f -follow > files.txt");
		toExec.add("mkdir " + bin.getAbsolutePath());
		toExec.add(StringUtils.join(" ",
				"javac",
				"-cp", buildCp(depends),
				"@files.txt",
				"-d", bin.getAbsolutePath(),
				"-encoding", encode));
		toExec.add("cd " + bin.getAbsolutePath());
	}

	public File createJar(String result, File manifest){
		File fileResult = new File(builds, result + ".jar");

		String command = "jar cf";

		if(manifest != null){
			command += "m";
		}

		command += " " + fileResult.getAbsolutePath() + " ";

		if(manifest != null){
			command += manifest.getAbsolutePath() + " ";
		}

		command += ".";
		toExec.add(command);

		return fileResult;
	}

	public void gitClone(String name, String repos, String branch){
		goToHome();

		String branchStr = branch == null ? "" : "-b " + branch + " ";

		toExec.add("rm -rf " + name);
		toExec.add("git clone " + branchStr + byProject.gitUrl + repos);
		toExec.add("cd " + name);
	}

	public void comment(String comment){
		toExec.add(System.lineSeparator() + "## " + comment + System.lineSeparator());
	}

	public void createManifest(File manifest){
		toExec.add("rm " + manifest.getAbsolutePath());
		String content = MANIFEST_HEADER;

		/* Class-Path */
		content += "Class-Path: " + getClassPath() + System.lineSeparator();

		/* Version */

		if(byProject.manifestData != null){
			content += StringUtils.join(byProject.manifestData, System.lineSeparator(), ": ");
		}

		if(byProject.projectForCommitId != null){
			goTo(new File(folder, byProject.projectForCommitId));
			toExec.add("lastcommit=$(git log --format=\"%H;%B\" -n 1)");

			content += "Implementation-Version: $lastcommit" + System.lineSeparator() + System.lineSeparator();
		}

		content = content.replace("<build-id>", byProject.buildId + "");
		byProject.buildId++;

		echo(content, manifest);
	}

	public void echo(String content, File to){
		content = content.replace("\n", "\n").replace("\"", "\\\"");
		content = '"' + content + '"';

		toExec.add("echo -e " + content + " >> " + to.getAbsolutePath());
	}

	private String getClassPath(){
		String result = "";
		boolean first = true;

		for(Dependency dependency : byProject.depends){
			if(dependency.addInManifest && !dependency.addInJar){
				if(!first)
					result += " ";
				else first = false;

				result += byProject.manifestLink;
				result += dependency.getFile().getName();
			}
		}

		return result;
	}

	private String buildCp(List<File> depends){
		return StringUtils.join(depends.stream().map(file -> { return '"' + file.getAbsolutePath() + '"'; }).collect(Collectors.toSet()), ":");
	}
}
