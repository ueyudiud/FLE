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
	/**
	 * Give a default mod compactor for loading mod.
	 * @return
	 */
	public static ModCompator newCompactor()
	{
		return newCompactor(Game.getActiveModID());
	}
	public static ModCompator newCompactor(String value)
	{
		return COMPATORS.computeIfAbsent(value, ModCompator::new);
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
		if (Game.isModLoaded(modid))
		{
			try
			{
				ICompatible compatible = (ICompatible) Class.forName(location).newInstance();
				this.compatibles.put(modid, compatible);
			}
			catch (Exception exception)
			{
				Log.error("Fail to put '{}' into compatibles map, this compatibility will be removed.", exception, location);
			}
		}
	}
	
	public final void call(String phase)
	{
		for(Entry<String, ICompatible> entry : this.compatibles.entrySet())
		{
			Log.info("The compatible from {} to {} start to invoke with phase {}.", this.modid, entry.getKey(), phase);
			try
			{
				entry.getValue().call(phase);
			}
			catch (Exception exception)
			{
				Log.warn("Fail to make compatible with {} and {} mod.", exception, this.modid, entry.getKey());
			}
		}
	}
	
	@Override
	public String toString()
	{
		return getClass().getName() + "|" + this.modid;
	}
	
	@FunctionalInterface
	public static interface ICompatible
	{
		void call(String phase) throws Exception;
	}
}