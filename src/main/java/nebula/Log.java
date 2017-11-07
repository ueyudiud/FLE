package nebula;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log
{
	private static Logger logLogger = LogManager.getLogger("Nebula Log");
	
	private static List<Object> cache = new ArrayList<>();
	
	/**
	 * Catching an <tt>Throwable</tt>.
	 * 
	 * @param throwable the catching throwable object.
	 * @throws RuntimeException if in debugging mode.
	 * @see nebula.Nebula#debug
	 */
	public static void catching(Throwable throwable)
	{
		if (Nebula.debug)
		{
			throw new RuntimeException(throwable);
		}
		else
		{
			logger().catching(throwable);
		}
	}
	
	public static void error(String message, Object...formats)
	{
		logger().error(message, formats);
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
	
	public static void trace(String message, Object...formats)
	{
		logger().trace(message, formats);
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
		logCachedInformations(Level.INFO, function, informations);
	}
	
	public static void logCachedInformations(Level level, Function<Object, String> function, String...informations)
	{
		synchronized (cache)
		{
			if (!cache.isEmpty())
			{
				for (String value : informations)
				{
					info(value);
				}
				for (Object object : cache)
				{
					if (object instanceof Throwable)
					{
						logger().catching((Throwable) object);
					}
					else
					{
						logger().log(level, function.apply(object));
					}
				}
			}
		}
		reset();
	}
	
	public static void logCachedExceptions(String...informations)
	{
		for (String value : informations)
		{
			error("# " + value);
		}
		synchronized (cache)
		{
			for (Object object : cache)
			{
				if (object instanceof Throwable)
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
	
	public static void logCachedInformations(PrintStream stream, Level level, Function<Object, String> function, String...informations)
	{
		synchronized (cache)
		{
			if (!cache.isEmpty())
			{
				for (String value : informations)
				{
					stream.println(value);
				}
				for (Object object : cache)
				{
					if (object instanceof Throwable)
					{
						((Throwable) object).printStackTrace(stream);
					}
					else
					{
						stream.println("[" + level.name() + "]" + function.apply(object));
					}
				}
			}
		}
		reset();
	}
	
	public static void logCachedExceptions(PrintStream stream, String...informations)
	{
		for (String value : informations)
		{
			stream.println("# " + value);
		}
		synchronized (cache)
		{
			for (Object object : cache)
			{
				if (object instanceof Throwable)
				{
					((Throwable) object).printStackTrace(stream);
				}
				else
				{
					stream.println(object.toString());
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
	
	public static void catchingIfDebug(Throwable throwable)
	{
		if (Nebula.debug)
		{
			catching(throwable);
		}
	}
}
