package logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

	private static MyLogger myLogger;
	
	private Logger logger;
	private FileHandler fh;

	public static MyLogger getInstance(){
		try{
			if(null == myLogger){
				myLogger = new MyLogger();
			}
		}catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		return myLogger;
	}
	
	private MyLogger() throws SecurityException, IOException {
		this.logger = Logger.getLogger("MyLog");
		this.fh = new FileHandler("src/main/resources/logFile.log", true);
		this.logger.addHandler(this.fh);
		this.fh.setFormatter(new SimpleFormatter());
	}
	
	public void logError(String s) {
		logger.log(Level.SEVERE, s);
	}
	
	public void logInfo(String s){
		logger.info(s);
	}
}
