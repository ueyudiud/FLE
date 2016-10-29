package farcore.lib.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import farcore.util.U;

public class ModCompator
{
	public static ModCompator newCompactor()
	{
		return newCompactor(U.Mod.getActiveModID());
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
		if(U.Mod.isModLoaded(modid))
		{
			try
			{
				ICompatible compatible = (ICompatible) Class.forName(location).newInstance();
				compatibles.put(modid, compatible);
			}
			catch (Exception exception)
			{
				Log.error("Fail to put '%s' into compatibles map, this compatibility will be removed.", exception, location);
			}
		}
	}
	
	public void call(String phase)
	{
		for(Entry<String, ICompatible> entry : compatibles.entrySet())
		{
			Log.info("The compatible from %s to %s start to invoke with phase %s.", modid, entry.getKey(), phase);
			try
			{
				entry.getValue().call(phase);
			}
			catch (Exception exception)
			{
				Log.warn("Fail to make compatible with %s and %s mod.", exception, modid, entry.getKey());
			}
		}
	}

	@Override
	public String toString()
	{
		return getClass().toString() + "|" + modid;
	}

	public static interface ICompatible
	{
		void call(String phase) throws Exception;
	}
}