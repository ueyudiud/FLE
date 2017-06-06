/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.capability;

import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.ImmutableMap;

import nebula.base.function.Appliable;
import nebula.base.function.Appliable.AppliableCached;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * Will ignore facing.
 * @author ueyudiud
 */
public class CapabilityCompactor<S> implements ICapabilityProvider
{
	private final S source;
	private Map<Capability<?>, Appliable<?>> map;
	
	public CapabilityCompactor(S source, Object...appliers)
	{
		this.source = source;
		if ((appliers.length & 0x1) != 0)
		{
			this.map = ImmutableMap.of();
			return;
		}
		ImmutableMap.Builder<Capability<?>, Appliable<?>> builder = ImmutableMap.builder();
		for (int i = 0; i < appliers.length; i += 2)
		{
			Capability<?> capability = (Capability<?>) appliers[i];
			Object appliable = appliers[i + 1];
			Appliable<?> cache;
			if (appliable instanceof Appliable)
			{
				cache = Appliable.wrapCached((Appliable<?>) appliable);
			}
			else if (appliable instanceof Function)
			{
				@SuppressWarnings("unchecked")
				final Function<S, ?> function = (Function<S, ?>) appliable;
				cache = new AppliableCached<Object>()
				{
					@Override
					protected Object apply$()
					{
						return function.apply(CapabilityCompactor.this.source);
					}
				};
			}
			else
			{
				cache = Appliable.to(appliable);
			}
			builder.put(capability, cache);
		}
		this.map = builder.build();
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return this.map.containsKey(capability);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return hasCapability(capability, facing) ? (T) this.map.get(capability).apply() : null;
	}
}