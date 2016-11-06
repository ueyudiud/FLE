package farcore.lib.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log
{
	private static Logger logLogger = LogManager.getLogger("Far Log");

	private static List<Object> cache = new ArrayList();

	public static void error(String message, Throwable throwable, Object...formats)
	{
		logger().error(message, throwable, formats);
	}
	public static void error(String message, Object...formats)
	{
		logger().error(message, formats);
	}
	public static void warn(String message, Throwable throwable, Object...formats)
	{
		logger().warn(message, throwable, formats);
	}
	public static void warn(String message, Object...formats)
	{
		logger().warn(message, formats);
	}
	public static void info(String message, Object...formats)
	{
		logger().info(message, formats);
	}
	public static void debug(String message, Object...formats)
	{
		logger().debug(message, formats);
	}
	public static void reset()
	{
		synchronized (cache)
		{
			cache.clear();
		}
	}
	public static void cache(Object object)
	{
		synchronized (cache)
		{
			cache.add(object);
		}
	}
	public static void logCachedInformations(Function<Object, String> function, String...informations)
	{
		synchronized (cache)
		{
			if(!cache.isEmpty())
			{
				for(String value : informations)
				{
					info(value);
				}
				for(Object object : cache)
				{
					if(object instanceof Throwable)
					{
						logger().catching((Throwable) object);
					}
					else
					{
						info(function.apply(object));
					}
				}
			}
		}
		reset();
	}
	public static void logCachedExceptions(String...informations)
	{
		for(String value : informations)
		{
			error("# " + value);
		}
		synchronized (cache)
		{
			for(Object object : cache)
			{
				if(object instanceof Throwable)
				{
					logger().catching((Throwable) object);
				}
				else
				{
					info(object.toString());
				}
			}
		}
		reset();
	}
	
	public static Logger logger;
	
	public static Logger logger()
	{
		return logger != null ? logger : logLogger;
	}
}