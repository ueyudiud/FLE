/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.api.util;

import java.util.List;

import com.google.common.collect.ImmutableList;

import nebula.base.function.WeightedRandomSelector;
import nebula.common.util.Maths;
import nebula.common.util.noise.NoiseBase;
import net.minecraft.util.math.MathHelper;

/**
 * A random selector for surface(overworld) generator provider.
 * 
 * @author ueyudiud
 */
public class SelectorEntryListProvider<T>
{
	static class EntryProvider<T>
	{
		T		value;
		int		baseMultiplier;
		float	tempMain;
		float	tempSigma;
		float	rainfallMain;
		float	rainfallSigma;
		int		noiseLayer;
		
		EntryProvider(T value, int layer, int base, float tMain, float tSigma, float rMain, float rSigma)
		{
			this.value = value;
			this.baseMultiplier = base;
			this.noiseLayer = layer;
			this.tempMain = tMain;
			this.tempSigma = tSigma;
			this.rainfallMain = rMain;
			this.rainfallSigma = rSigma;
		}
	}
	
	public static <T> Builder<T> builder()
	{
		return new Builder<>();
	}
	
	public static class Builder<T>
	{
		private ImmutableList.Builder<EntryProvider<T>> builder = ImmutableList.builder();
		
		private Builder()
		{
		}
		
		public void add1(T value, int layer, int base, float minT, float maxT, float minR, float maxR)
		{
			float m1 = 0.25F / (MathHelper.sqrt(base) - 1);
			add2(value, layer, base, (minT + maxT) / 2.0F, Maths.sq(maxT - minT) * m1, (minR + maxR) / 2.0F, Maths.sq(maxR - minR) * m1);
		}
		
		public void add2(T value, int layer, int base, float mT, float sT, float mR, float sR)
		{
			this.builder.add(new EntryProvider<>(value, layer, base, mT, sT, mR, sR));
		}
		
		public SelectorEntryListProvider<T> build()
		{
			return new SelectorEntryListProvider<>(this);
		}
	}
	
	private final List<EntryProvider<T>> providers;
	
	SelectorEntryListProvider(Builder<T> builder)
	{
		this.providers = builder.builder.build();
	}
	
	public void addToSelector(int x, int z, float temp, float rain, NoiseBase noise, WeightedRandomSelector<? super T> selector)
	{
		for (EntryProvider<T> provider : this.providers)
		{
			double result = noise.noise(x, provider.noiseLayer, z);
			result *= result;
			result *= provider.baseMultiplier;
			result *= provider.tempSigma / (provider.tempSigma + Maths.sq(temp - provider.tempMain));
			result *= provider.rainfallSigma / (provider.rainfallSigma + Maths.sq(rain - provider.rainfallMain));
			if ((int) result > 0)
			{
				selector.add(provider.value, (int) result);
			}
		}
	}
}
