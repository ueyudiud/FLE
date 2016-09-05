package farcore.lib.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log
{
	private static Logger logLogger = LogManager.getLogger("Far Log");

	private static List<Throwable> cache = new ArrayList();
	
	public static void error(String message, Throwable throwable)
	{
		logger().error(message, throwable);
	}
	public static void warn(String message, Throwable throwable)
	{
		logger().warn(message, throwable);
	}
	public static void warn(String message)
	{
		logger().warn(message);
	}
	public static void info(String message)
	{
		logger().info(message);
	}
	public static void debug(String message)
	{
		logger().debug(message);
	}
	public static void reset()
	{
		synchronized (cache)
		{
			cache.clear();
		}
	}
	public static void cache(Throwable throwable)
	{
		synchronized (cache)
		{
			cache.add(throwable);
		}
	}
	public static void logCachedExceptions()
	{
		synchronized (cache)
		{
			for(Throwable throwable : cache)
			{
				logger().catching(throwable);
			}
		}
		reset();
	}
	
	public static Logger logger;
	
	public static Logger logger()
	{
		return logger != null ? logger :
			logLogger;
	}
}