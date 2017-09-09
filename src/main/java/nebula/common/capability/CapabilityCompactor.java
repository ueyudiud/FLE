/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.capability;

import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.ImmutableMap;

import nebula.base.function.Applicable;
import nebula.base.function.Applicable.AppliableCached;
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
	private Map<Capability<?>, Applicable<?>> map;
	
	public CapabilityCompactor(S source, Object...appliers)
	{
		this.source = source;
		if ((appliers.length & 0x1) != 0)
		{
			this.map = ImmutableMap.of();
			return;
		}
		ImmutableMap.Builder<Capability<?>, Applicable<?>> builder = ImmutableMap.builder();
		for (int i = 0; i < appliers.length; i += 2)
		{
			Capability<?> capability = (Capability<?>) appliers[i];
			Object appliable = appliers[i + 1];
			Applicable<?> cache;
			if (appliable instanceof Applicable)
			{
				cache = Applicable.wrapCached((Applicable<?>) appliable);
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
				cache = Applicable.to(appliable);
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