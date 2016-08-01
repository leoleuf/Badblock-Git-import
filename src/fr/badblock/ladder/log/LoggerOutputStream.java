package fr.badblock.ladder.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoggerOutputStream extends ByteArrayOutputStream {

	private static final String separator = System.getProperty( "line.separator" );
	/*========================================================================*/
	private final Logger logger;
	private final Level level;

	@Override
	public void flush() throws IOException {
		String contents = toString("UTF-8");
		super.reset();
		if(!contents.isEmpty() && !contents.equals(separator)) {
			logger.logp( level, "", "", contents );
		}
	}
}
