package util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import core.Globals;

public class Log {

	private Logger report;
	private static final String FQCN = Log.class.getName();
	private static final String DEBUG = Globals.GC_LOG_DEBUG;
	private static final String INFO = Globals.GC_LOG_INFO;
	private static final String WARN = Globals.GC_LOG_WARN;
	private static final String ERROR = Globals.GC_LOG_ERR;
	private static final String TRACE = Globals.GC_LOG_TRACE;
	
	public enum Priority {
		DEBUG, INFO, WARN, ERROR, TRACE
	}
	
	public Log(Class<?> logClass){
		report = Logger.getLogger(logClass);
	}
		
	public void Report(Log.Priority level,String description){
		
		if(level.toString().equalsIgnoreCase(DEBUG)){
			report.log(FQCN,Level.DEBUG ,description,null);
			//report.debug(description);
		}
		
		if(level.toString().equalsIgnoreCase(INFO)){
			report.log(FQCN,Level.INFO ,description,null);
			//report.info(description);
		}
		
		if(level.toString().equalsIgnoreCase(WARN)){
			report.log(FQCN,Level.WARN ,description,null);
			//report.warn(description);
		}
		
		if(level.toString().equalsIgnoreCase(ERROR)){
			report.log(FQCN,Level.ERROR ,description,null);
			//report.error(description);
		}	
		
		if(level.toString().equalsIgnoreCase(TRACE)){
			report.log(FQCN,Level.TRACE ,description,null);
			//report.error(description);
		}
	}
}
