package fr.badblock.ladder.http;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Comparator {
	final File folderBungee17;
	final File folderBungee19;
	
	final File folderResult;
	
	final String extension17 = "_1.7";
	final String extension18 = "_1.9";
	
	final String removed     = "_old";
	final String added	     = "_new";
	
	public static void main(String[] args){
		Comparator comp = new Comparator(new File("D:\\BungeeCords\\v1.7"), new File("D:\\BungeeCords\\v1.9"), new File("D:\\BungeeCords\\vMedium"));
	
		comp.start();
	}
	
	public void start(){
		createAllFolder(folderBungee17, folderResult, false);
		createAllFolder(folderBungee19, folderResult, true);
		
		compare17(folderBungee17, folderBungee19, folderResult);
		compare18(folderBungee17, folderBungee19, folderResult);
	}
	
	protected void createAllFolder(File base, File createIn, boolean log){
		for(File file : base.listFiles()){
			if(file.isDirectory()){
				File detected = new File(createIn, file.getName());
				
				if(log && !detected.exists())
					System.out.println("New folder in the new version : " + detected);
				if(!detected.exists())
					detected.mkdirs();
				createAllFolder(file, detected, log);
			}
		}
	}
	
	protected void compare17(File folder17, File folder18, File folderResult){
		for(String name : folder17.list()){
			File v17 = new File(folder17, name);
			File v18 = new File(folder18, name);
			File res = new File(folderResult, name);
			
			if(v17.isDirectory()){
				compare17(v17, v18, res);
			} else if(!v18.exists()){
				copy(v17, toPrefix(res, removed));
				System.out.println("Removed file in 1.9 : " + v17);
			} else if(v17.length() != v18.length()){
				copy(v17, toPrefix(res, extension17));
				copy(v17, res);
				copy(v18, toPrefix(res, extension18));
				
				System.out.println("Modified file between 1.7 and 1.9 : " + v17);
			} else {
				copy(v17, res);
			}
		}
	}
	
	protected void compare18(File folder17, File folder18, File folderResult){
		for(String name : folder18.list()){
			File v17 = new File(folder17, name);
			File v18 = new File(folder18, name);
			File res = new File(folderResult, name);
			
			if(v18.isDirectory()){
				compare18(v17, v18, res);
			} else if(!v17.exists()){
				System.out.println("Added file in 1.9 : " + v18);
				copy(v18, res);
			}
		}
	}
	
	private File toPrefix(File res, String prefix){
		String name  = res.getName();
		String[] splitted = name.split("\\.");
		
		String nName = splitted[0] + prefix + "." + splitted[1];
		
		return new File(res.getParentFile(), nName);
	}
	
	private static void copy(File from, File to){
		try {
			Files.copy(from, to);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
