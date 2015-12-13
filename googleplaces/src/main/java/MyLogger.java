

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

	private Logger logger = Logger.getLogger("MyLog");
	private FileHandler fh;

	public MyLogger() throws SecurityException, IOException{
		fh = new FileHandler("src/main/resources/logFile.log");
		logger.addHandler(fh);
		fh.setFormatter(new SimpleFormatter());
	}
	
	public void logError(String s) {
		logger.log(Level.SEVERE, s);
	}
	
	public void logInfo(String s){
		logger.info(s);
	}
}
