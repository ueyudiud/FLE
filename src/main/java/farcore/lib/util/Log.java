package farcore.lib.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log
{
	private static Logger logLogger = LogManager.getLogger("Far Log");

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
	
	public static Logger logger;
	
	public static Logger logger()
	{
		return logger != null ? logger :
			logLogger;
	}
}