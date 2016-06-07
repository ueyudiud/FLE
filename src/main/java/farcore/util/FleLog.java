package farcore.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FleLog
{
	private static Logger logLogger = LogManager.getLogger("Far Log");
	public static Logger logger;
	public static Logger coreLogger;
	
	public static Logger getCoreLogger()
	{
		return coreLogger != null ? coreLogger :
			logLogger;
	}
	
	public static Logger getLogger()
	{
		return logger != null ? logger :
			logLogger;
	}
}