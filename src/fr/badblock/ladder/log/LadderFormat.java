package fr.badblock.ladder.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LadderFormat extends Formatter {
    private final DateFormat date = new SimpleDateFormat("HH:mm:ss");

	@Override
	public String format(LogRecord logRecord) {
        StringBuilder formatted = new StringBuilder();
        String 		  message   = formatMessage(logRecord);
        
        String[]	  lines		= message.split("\n");
        
        for(String line : lines)
        	append(logRecord, formatted, line);
        
        
        if(logRecord.getThrown() != null) {
            StringWriter writer = new StringWriter();
            logRecord.getThrown().printStackTrace( new PrintWriter( writer ) );
            formatted.append( writer );
        }

        return formatted.toString();
	}
	
	protected void append(LogRecord logRecord, StringBuilder formatted, String message){
		formatted.append(date.format(logRecord.getMillis()));
        formatted.append(" [");
        formatted.append(logRecord.getLevel().getLocalizedName());
        formatted.append("] ");
        formatted.append(message);
        formatted.append('\n');
	}
}
