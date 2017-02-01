package nebula.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nebula.Log;

/**
 * To compact with other mods.
 * @author ueyudiud
 *
 */
public class ModCompator
{
	public static ModCompator newCompactor()
	{
		return newCompactor(Game.getActiveModID());
	}
	public static ModCompator newCompactor(String value)
	{
		ModCompator compator = COMPATORS.get(value);
		if(compator == null)
		{
			COMPATORS.put(value, compator = new ModCompator(value));
		}
		return compator;
	}
	
	private static final Map<String, ModCompator> COMPATORS = new HashMap();
	
	private Map<String, ICompatible> compatibles = new HashMap();
	private final String modid;
	
	ModCompator(String modid)
	{
		this.modid = modid;
	}
	
	public void addCompatible(String modid, String location)
	{
		if(Game.isModLoaded(modid))
		{
			try
			{
				ICompatible compatible = (ICompatible) Class.forName(location).newInstance();
				this.compatibles.put(modid, compatible);
			}
			catch (Exception exception)
			{
				Log.error("Fail to put '%s' into compatibles map, this compatibility will be removed.", exception, location);
			}
		}
	}
	
	public void call(String phase)
	{
		for(Entry<String, ICompatible> entry : this.compatibles.entrySet())
		{
			Log.info("The compatible from %s to %s start to invoke with phase %s.", this.modid, entry.getKey(), phase);
			try
			{
				entry.getValue().call(phase);
			}
			catch (Exception exception)
			{
				Log.warn("Fail to make compatible with %s and %s mod.", exception, this.modid, entry.getKey());
			}
		}
	}
	
	@Override
	public String toString()
	{
		return getClass().toString() + "|" + this.modid;
	}
	
	@FunctionalInterface
	public static interface ICompatible
	{
		void call(String phase) throws Exception;
	}
}