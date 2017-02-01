package fargen.core.util;

import static farcore.data.EnumTempCategory.FRIGID;
import static farcore.data.EnumTempCategory.OCEAN;
import static farcore.data.EnumTempCategory.SUBFRIGID;
import static farcore.data.EnumTempCategory.SUBTROPICAL;
import static farcore.data.EnumTempCategory.TEMPERATE;
import static farcore.data.EnumTempCategory.TROPICAL;

import java.util.Arrays;

import farcore.data.EnumTempCategory;
import nebula.common.util.Maths;
import net.minecraft.world.biome.Biome.TempCategory;

public enum ClimaticZone
{
	tropical_desert(TROPICAL, 1.2F, 0.1F, 3.5F, 0.2F, 0.01F, 0.005F),
	tropical_woodland(TROPICAL, 1.1F, 0.1F, 3.0F, 0.3F, 0.1F, 0.04F),
	tropical_plain(TROPICAL, 1.1F, 0.1F, 3.0F, 0.3F, 0.3F, 0.2F),
	tropical_monsoon(TROPICAL,
			new float[]{0.98F, 0.996076952F, 1.04F, 1.1F, 1.16F, 1.203923048F, 1.22F, 1.203923048F, 1.16F, 1.1F, 1.04F, 0.996076952F},
			new float[]{2.7F, 2.740192379F, 2.85F, 3F, 3.15F, 3.259807621F, 3.3F, 3.259807621F, 3.15F, 3F, 2.85F, 2.740192379F},
			new float[]{0.05F, 0.127152269F, 0.60625F, 1.623312588F, 2.940359785F, 4.060433739F, 4.5F, 4.060433739F, 2.940359785F, 1.623312588F, 0.60625F, 0.127152269F}),
	tropical_forest(TROPICAL, 1.0F, 0.1F, 2.8F, 0.3F, 2.0F, 1.2F),
	tropical_rainforest(TROPICAL, 1.0F, 0.1F, 2.5F, 0.3F, 3.6F, 0.2F),
	subtropical_plain(SUBTROPICAL, 0.9F, 0.15F, 2.2F, 0.5F, 0.32F, 0.2F),
	subtropical_monson(SUBTROPICAL, 0.9F, 0.15F, 2.2F, 0.5F, 1.3F, 1.0F),
	subtropical_wet(SUBTROPICAL, 0.9F, 0.15F, 2.2F, 0.5F, 2.3F, 0.8F),
	temperate_desert(SUBTROPICAL, 0.8F, 0.15F, 1.2F, 0.6F, 0.06F, 0.02F),
	temperate_rockland(TEMPERATE, 0.78F, 0.18F, 1.2F, 0.6F, 0.15F, 0.5F),
	temperate_plain(TEMPERATE, 0.75F, 0.2F, 1.0F, 0.9F, 0.6F, 0.3F),
	temperate_forest(TEMPERATE, 0.75F, 0.2F, 1.2F, 0.9F, 1.3F, 0.5F),
	temperate_wet(TEMPERATE, 0.75F, 0.2F, 1.2F, 0.9F, 1.9F, 0.8F),
	temperate_rainforest(TEMPERATE, 0.75F, 0.2F, 1.1F, 0.8F, 1.8F, 0.9F),
	temperate_monsoon(TEMPERATE, 0.7F, 0.2F, 1.1F, 1.0F, 1.1F, 0.9F),
	temperate_ocean(TEMPERATE, 0.6F, 0.18F, 1.0F, 0.7F, 0.9F, 0.12F),
	mediterranean(TEMPERATE, 0.7F, 0.2F, 1.3F, 0.9F, 0.5F, -0.4F),
	subfrigid_plain(SUBFRIGID, 0.5F, 0.3F, 0.6F, 0.7F, 0.28F, 0.12F),
	subfrigid_forest(SUBFRIGID, 0.5F, 0.3F, 0.6F, 0.7F, 0.7F, 0.6F),
	frigid_desert(FRIGID, 0.4F, 0.3F, -.1F, 0.7F, 0.008F, 0.02F),
	frigid_plain(FRIGID, 0.4F, 0.3F, -.2F, 0.9F, 0.25F, 0.1F),
	iceland(FRIGID, 0.2F, 0.1F, -.8F, 1.0F, 0.03F, 0.01F),
	vertical(FRIGID, 0.6F, 0.05F, 0.8F, 1.3F, 0.4F, 0.3F),
	ocean_tropical(OCEAN, 1.0F, 0.1F, 2.5F, 0.4F, 2.4F, 0.8F),
	ocean_subtropical(OCEAN, 0.8F, 0.2F, 2.0F, 0.6F, 1.6F, 0.5F),
	ocean_temperate(OCEAN, 0.8F, 0.3F, 1.4F, 0.6F, 1.4F, 0.4F),
	ocean_subfrigid(OCEAN, 0.7F, 0.35F, 0.8F, 0.7F, 1.2F, 0.3F),
	ocean_frigid(OCEAN, 0.6F, 0.4F, 0.5F, 0.8F, 1.1F, 0.3F);

	private static final float[] makeCosineFloats(int length, float base, float flot)
	{
		float[] list = new float[length];
		if(flot == 0)
		{
			Arrays.fill(list, base);
		}
		else
		{
			for(int i = 0; i < length; ++i)
			{
				double d = 2D * Math.PI * i / length;
				list[i] = -(float) Math.cos(d) * flot + base;
			}
		}
		return list;
	}
	
	public final EnumTempCategory category1;
	public final TempCategory category;
	
	public final float[] sunshine;
	public final float sunshineAverage;

	public final float[] temperature;
	public final float temperatureAverage;

	public final float[] rain;
	public final float rainAverage;
	
	ClimaticZone(EnumTempCategory category, float sb, float sf, float tb, float tf, float rb, float rf)
	{
		this(category, makeCosineFloats(12, sb, sf), makeCosineFloats(12, tb, tf), makeCosineFloats(12, rb, rf));
	}
	ClimaticZone(EnumTempCategory category, float[] sunshine, float[] temperature, float[] rain)
	{
		category1 = category;
		this.category = category.category;
		this.sunshine = sunshine;
		sunshineAverage = Maths.average(sunshine);
		this.temperature = temperature;
		temperatureAverage = Maths.average(temperature);
		this.rain = rain;
		rainAverage = Maths.average(rain);
	}
}