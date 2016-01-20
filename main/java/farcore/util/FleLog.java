package farcore.util;

import java.util.ArrayList;
import java.util.List;

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
	
	public static void info(String message)
	{
		getLogger().info(message);
	}
	
	public static void debug(String message)
	{
		getLogger().debug(message);
	}

	public static void warn(String message)
	{
		getLogger().warn(message);
	}
	
	public static void error(String message, Throwable th)
	{
		getLogger().error(message, th);
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

	private static Logger logger;
	
	private static List<Throwable> exceptionList = new ArrayList();
}