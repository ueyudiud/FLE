package flapi.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import flapi.FleAPI;

public class FleLog
{	
    public static Logger getLogger()
    {
		return logger == null ? LogManager.getLogger(FleAPI.MODID) : logger;
	}

	public static void setLogger(Logger logger)
	{
		FleLog.logger = logger;
	}
	
	public static void addExceptionToCache(Throwable e)
	{
		if(e == null) return;
		exceptionList.add(e);
	}
	
	public static void resetAndCatchException(String cause)
	{
		if(!exceptionList.isEmpty())
		{
			getLogger().error(cause);
			for(Throwable e : exceptionList)
				getLogger().catching(e);
			exceptionList.clear();
		}
	}
	
	public static void log(String contain, Level level)
	{
		getLogger().log(level, contain);
	}
	
	public static void log(String contain, Throwable throwable, Level level)
	{
		getLogger().log(level, contain, throwable);
	}

	public static void debug(String string)
	{
		log(string, Level.DEBUG);
	}
	
	public static void error(String string, Throwable throwable)
	{
		log(string, throwable, Level.ERROR);
	}

	private static Logger logger;
	
	private static List<Throwable> exceptionList = new ArrayList();
}