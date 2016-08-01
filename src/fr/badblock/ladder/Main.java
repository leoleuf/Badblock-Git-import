package fr.badblock.ladder;

import fr.badblock.ladder.api.Ladder;
import jline.console.ConsoleReader;

public class Main {
	public static boolean DEBUG = true;
	
	public static void main(String[] args){
		if(args.length != 0){
			String firstArg = args[0].toLowerCase();
			if(firstArg.equals("-v") || firstArg.equals("-version")){
				System.out.println("Used Ladder version is : " + Proxy.LADDER_VERSION);
				System.exit(0);
			} else if(firstArg.equals("-d") || firstArg.equals("-debug")){
				DEBUG = true;
			}
		}
		
		System.out.println("Welcome to " + Proxy.LADDER_VERSION + " ! Launching...");
		
		ConsoleReader reader = null;
		
		try {
			reader = new ConsoleReader();
			new Proxy(reader);
		} catch(Throwable t){
			System.err.println("Unable to launch Ladder :(");
			t.printStackTrace();
			System.err.println("Aborting ...");
			System.exit(1);
		}
		
		boolean one = true;
		
		while(true){
			try {
				String input = reader.readLine(one ? ">" : "");
				one = !one;

				if(input != null && (input.isEmpty() || input.equals("\n"))) {
					one = true;
					continue;
				}
				
				Ladder.getInstance().getConsoleCommandSender().forceCommand(input);
			} catch(Exception e){}
		}
	}
}
