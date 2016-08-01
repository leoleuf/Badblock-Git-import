package fr.badblock.ladder.log;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import jline.console.ConsoleReader;

public class LadderLogger extends Logger {
	public LadderLogger(ConsoleReader consoleReader, File folder) {
		super("Ladder", null);
		setLevel(Level.ALL);
		
		try {
			folder.mkdir();
			
			FileHandler fileHandler = new FileHandler(folder.getAbsolutePath() + File.separator + "ladder_%g.log", 1 << 24, 8, true);
            fileHandler.setFormatter(new LadderFormat());
            fileHandler.setLevel(Level.INFO);
            addHandler(fileHandler);
            
            Handler handler = new ColouredWriter(consoleReader);
            handler.setLevel(Level.INFO);
            handler.setFormatter(new LadderFormat());
            addHandler(handler);
		} catch(IOException e){
			
		}
	}

}
