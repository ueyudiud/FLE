package fle.core.init;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config
{
	private static Map<String, Object> configMap = new HashMap();
	
	public static void init(Configuration config)
	{
		configMap.put("pCharcoal", setInteger(config, "energy", "Charcoal Power", "High value will make charcoal heating faster.", 40000));
		configMap.put("pFirewood", setInteger(config, "energy", "Firewood Power", "High value will make firewood heating faster.", 50000));
		configMap.put("pLavaTransfer", setInteger(config, "energy", "Lava Heat Transfer Power", "High value will make lava heat transfer heating faster.", 30000));
		configMap.put("pCeramicFirebox", setInteger(config, "energy", "Ceramic Firebox Power", "High value will make cermaic firebox heating faster.", 40000));
		configMap.put("pBoilingHeater", setInteger(config, "energy", "Boiling Heater Power", "High value will make boiling heater heating faster.", 35000));
		
		configMap.put("pFarmlandConsume", setInteger(config, "logistics", "Farmland Water Consume", "High value will mark ditch consume more water to farmland, unit : L/tick.", 10));

		configMap.put("p@soybean", setInteger(config, "crop", "Soybean Grow Tick", "Get the grow tick require of this crop.", 2400));
		configMap.put("p@ramie", setInteger(config, "crop", "Ramie Grow Tick", "Get the grow tick require of this crop.", 2200));
		configMap.put("p@millet", setInteger(config, "crop", "Ramie Grow Tick", "Get the grow tick require of this crop.", 2200));
		configMap.put("p@wheat", setInteger(config, "crop", "Wheat Grow Tick", "Get the grow tick require of this crop.", 2000));
		configMap.put("p@suger_cances", setInteger(config, "crop", "Suger Cances Grow Tick", "Get the grow tick require of this crop.", 3000));
		configMap.put("p@cotton", setInteger(config, "crop", "Cotton Grow Tick", "Get the grow tick require of this crop.", 3000));
		configMap.put("p@sweetpotato", setInteger(config, "crop", "Cotton Grow Tick", "Get the grow tick require of this crop.", 3400));
		configMap.put("p@potato", setInteger(config, "crop", "Potato Grow Tick", "Get the grow tick require of this crop.", 2900));
	}
	
	public static boolean setBoolean(Configuration config, String type, String tag, String info, boolean defaultValue)
	{
		Property prop = config.get(type, tag, defaultValue);
		if(info != null)
			prop.comment = info;
		return Boolean.parseBoolean(prop.getString());
	}
	
	public static int setInteger(Configuration config, String type, String tag, String info, int defaultValue)
	{
		Property prop = config.get(type, tag, defaultValue);
		if(info != null)
			prop.comment = info;
		return Integer.parseInt(prop.getString());
	}
	
	public static double setDouble(Configuration config, String type, String tag, String info, double defaultValue)
	{
		Property prop = config.get(type, tag, defaultValue);
		if(info != null)
			prop.comment = info;
		return Double.parseDouble(prop.getString());
	}
	
	public static boolean getBoolean(String tag)
	{
		return getBoolean(tag, false);
	}
	
	public static boolean getBoolean(String tag, boolean defaultValue)
	{
		try
		{
			return !configMap.containsKey(tag) ? defaultValue : (Boolean) configMap.get(tag);
		}
		catch(Throwable e)
		{
			return defaultValue;
		}
	}
	
	public static int getInteger(String tag)
	{
		return getInteger(tag, 0);
	}
	
	public static int getInteger(String tag, int defaultValue)
	{
		try
		{
			return !configMap.containsKey(tag) ? defaultValue : (Integer) configMap.get(tag);
		}
		catch(Throwable e)
		{
			return defaultValue;
		}
	}
	
	public static double getDouble(String tag)
	{
		return getDouble(tag, 0.0D);
	}
	
	public static double getDouble(String tag, double defaultValue)
	{
		try
		{
			return !configMap.containsKey(tag) ? defaultValue : (Double) configMap.get(tag);
		}
		catch(Throwable e)
		{
			return defaultValue;
		}
	}
}