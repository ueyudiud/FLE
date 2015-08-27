package fle.api.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fle.api.FleAPI;

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

	private static Logger logger;
}