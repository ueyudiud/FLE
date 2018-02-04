/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.capability;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.capabilities.Capability;

/**
 * @author ueyudiud
 */
public class CapabilityHelper
{
	private static final Map<Capability<?>, Class<?>> CAPABILITY_CLASS_MAP = new HashMap<>();
	
	//Internal method.
	public static void registerCapabilityType(Class<?> clazz, Capability<?> capability)
	{
		CAPABILITY_CLASS_MAP.put(capability, clazz);
	}
	
	public static <T> Class<T> getCapabilityType(Capability<T> capability)
	{
		return (Class<T>) CAPABILITY_CLASS_MAP.get(capability);
	}
}
