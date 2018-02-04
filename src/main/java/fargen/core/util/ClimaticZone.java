/*
 * copyright© 2016-2018 ueyudiud
 */
package fargen.core.util;

import static farcore.data.EnumTempCategory.FRIGID;
import static farcore.data.EnumTempCategory.OCEAN;
import static farcore.data.EnumTempCategory.SUBFRIGID;
import static farcore.data.EnumTempCategory.SUBTROPICAL;
import static farcore.data.EnumTempCategory.TEMPERATE;
import static farcore.data.EnumTempCategory.TROPICAL;

import java.util.Arrays;

import farcore.data.EnumTempCategory;
import nebula.common.util.L;
import nebula.common.util.Maths;
import net.minecraft.world.biome.Biome.TempCategory;

public enum ClimaticZone
{
	// smax smin tmax tmin rmax rmin
	tropical_desert(TROPICAL, 1.30F, 1.10F, 4.2F, 3.7F, 0.01F, 0.005F),
	tropical_woodland(TROPICAL, 1.30F, 1.10F, 3.8F, 3.4F, 0.14F, 0.06F),
	tropical_plain(TROPICAL, 1.30F, 1.10F, 4.0F, 3.2F, 0.5F, 0.1F),
	tropical_monsoon(TROPICAL, makeCosineFloats(12, 11, 1.3F, 1.1F), makeCosineFloats(12, 11, 4.0F, 3.2F), new float[] { 0.05F, 0.127152269F, 0.60625F, 1.623312588F, 2.940359785F, 4.060433739F, 4.5F, 4.060433739F, 2.940359785F, 1.623312588F, 0.60625F, 0.127152269F }),
	tropical_forest(TROPICAL, 1.10F, 0.90F, 3.8F, 3.2F, 3.2F, 0.8F),
	tropical_rainforest(TROPICAL, 1.10F, 0.90F, 3.6F, 3.0F, 3.8F, 3.4F),
	subtropical_plain(SUBTROPICAL, 1.05F, 0.85F, 2.9F, 1.9F, 1.0F, 0.2F),
	subtropical_monson(SUBTROPICAL, 1.05F, 0.85F, 2.9F, 1.9F, 2.3F, 0.3F),
	subtropical_wet(SUBTROPICAL, 1.05F, 0.85F, 2.9F, 1.9F, 3.1F, 1.5F),
	temperate_desert(SUBTROPICAL, 0.95F, 0.75F, 2.6F, 0.8F, 0.06F, 0.02F),
	temperate_rockland(TEMPERATE, 1.02F, 0.93F, 1.8F, 0.2F, 0.3F, 0.02F),
	temperate_plain(TEMPERATE, 0.95F, 0.65F, 2.7F, 0.8F, 0.6F, 0.3F),
	temperate_forest(TEMPERATE, 0.95F, 0.65F, 2.6F, 0.5F, 1.3F, 0.5F),
	temperate_wet(TEMPERATE, 0.95F, 0.65F, 2.1F, 0.3F, 1.9F, 0.8F),
	temperate_rainforest(TEMPERATE, 0.95F, 0.65F, 3.0F, 1.9F, 1.8F, 0.9F),
	temperate_monsoon(TEMPERATE, 0.90F, 0.50F, 2.1F, 0.2F, 1.1F, 0.2F),
	temperate_ocean(TEMPERATE, 1.25F, 0.55F, 1.7F, 0.3F, 1.0F, 0.9F),
	mediterranean(TEMPERATE, 0.90F, 0.50F, 2.2F, 0.4F, 0.2F, 0.6F),
	subfrigid_plain(SUBFRIGID, 0.80F, 0.20F, 1.3F, -.4F, 0.28F, 0.12F),
	subfrigid_forest(SUBFRIGID, 0.80F, 0.20F, 1.4F, -.3F, 0.9F, 0.3F),
	frigid_desert(FRIGID, 0.70F, 0.10F, 0.9F, -.9F, 0.03F, 0.01F),
	frigid_plain(FRIGID, 0.70F, 0.10F, 1.0F, -.9F, 0.35F, 0.15F),
	iceland(FRIGID, 0.30F, 0.10F, 0.2F, -1.2F, 0.03F, 0.01F),
	vertical(FRIGID, 0.65F, 0.55F, 2.1F, -0.9F, 0.7F, 0.2F),
	ocean_tropical(OCEAN, 1.10F, 0.90F, 2.9F, 2.1F, 3.2F, 1.6F),
	ocean_subtropical(OCEAN, 1.00F, 0.70F, 2.6F, 1.4F, 2.1F, 1.1F),
	ocean_temperate(OCEAN, 0.90F, 0.60F, 2.0F, 0.8F, 1.8F, 1.0F),
	ocean_subfrigid(OCEAN, 0.80F, 0.40F, 1.5F, 0.1F, 1.5F, 0.9F),
	ocean_frigid(OCEAN, 0.60F, 0.20F, 1.3F, -.3F, 1.4F, 0.8F);
	
	private static float[] makeCosineFloats(int length, int off, float max, float min)
	{
		float[] list = new float[length];
		if (L.similar(max, min))
		{
			Arrays.fill(list, max);
		}
		else
		{
			for (int i = 0; i < length; ++i)
			{
				double r = 2D * Math.PI * (i - off) / length;
				float a = (float) Math.cos(r);
				list[i] = a * min + (1 - a) * max;
			}
		}
		return list;
	}
	
	public final EnumTempCategory	category1;
	public final TempCategory		category;
	
	public final float[]	sunshine;
	public final float		sunshineAverage;
	
	public final float[]	temperature;
	public final float		temperatureAverage;
	
	public final float[]	rain;
	public final float		rainAverage;
	
	ClimaticZone(EnumTempCategory category, float sb, float sf, float tb, float tf, float rb, float rf)
	{
		this(category, makeCosineFloats(12, 11, sb, sf), makeCosineFloats(12, 11, tb, tf), makeCosineFloats(12, 11, rb, rf));
	}
	
	ClimaticZone(EnumTempCategory category, float[] sunshine, float[] temperature, float[] rain)
	{
		this.category1 = category;
		this.category = category.category;
		this.sunshine = sunshine;
		this.sunshineAverage = Maths.average(sunshine);
		this.temperature = temperature;
		this.temperatureAverage = Maths.average(temperature);
		this.rain = rain;
		this.rainAverage = Maths.average(rain);
	}
}
