package fargen.core.util;

import java.util.Arrays;

import farcore.util.U.Maths;
import net.minecraft.world.biome.Biome.TempCategory;

public enum ClimaticZone
{
	tropical_desert(TempCategory.WARM, 1.2F, 0.1F, 3.5F, 0.2F, 0.01F, 0.005F),
	tropical_woodland(TempCategory.WARM, 1.1F, 0.1F, 3.0F, 0.3F, 0.1F, 0.04F),
	tropical_plain(TempCategory.WARM, 1.1F, 0.1F, 3.0F, 0.3F, 0.3F, 0.2F),
	tropical_monsoon(TempCategory.WARM,
			new float[]{0.98F, 0.996076952F, 1.04F, 1.1F, 1.16F, 1.203923048F, 1.22F, 1.203923048F, 1.16F, 1.1F, 1.04F, 0.996076952F},
			new float[]{2.7F, 2.740192379F, 2.85F, 3F, 3.15F, 3.259807621F, 3.3F, 3.259807621F, 3.15F, 3F, 2.85F, 2.740192379F},
			new float[]{0.05F, 0.127152269F, 0.60625F, 1.623312588F, 2.940359785F, 4.060433739F, 4.5F, 4.060433739F, 2.940359785F, 1.623312588F, 0.60625F, 0.127152269F}),
	tropical_forest(TempCategory.WARM, 1.0F, 0.1F, 2.8F, 0.3F, 2.0F, 1.2F),
	tropical_rainforest(TempCategory.WARM, 1.0F, 0.1F, 2.5F, 0.3F, 3.6F, 0.2F),
	subtropical_plain(TempCategory.WARM, 0.9F, 0.15F, 2.2F, 0.5F, 0.32F, 0.2F),
	subtropical_monson(TempCategory.WARM, 0.9F, 0.15F, 2.2F, 0.5F, 1.3F, 1.0F),
	subtropical_wet(TempCategory.WARM, 0.9F, 0.15F, 2.2F, 0.5F, 2.3F, 0.8F),
	temperate_desert(TempCategory.WARM, 0.8F, 0.15F, 1.2F, 0.6F, 0.06F, 0.02F),
	temperate_rockland(TempCategory.WARM, 0.78F, 0.18F, 1.2F, 0.6F, 0.15F, 0.5F),
	temperate_plain(TempCategory.MEDIUM, 0.75F, 0.2F, 1.0F, 0.9F, 0.6F, 0.3F),
	temperate_forest(TempCategory.MEDIUM, 0.75F, 0.2F, 1.2F, 0.9F, 1.3F, 0.5F),
	temperate_rainforest(TempCategory.MEDIUM, 0.75F, 0.2F, 1.1F, 0.8F, 1.8F, 0.9F),
	temperate_monsoon(TempCategory.MEDIUM, 0.7F, 0.2F, 1.1F, 1.0F, 1.1F, 0.9F),
	temperate_ocean(TempCategory.MEDIUM, 0.6F, 0.18F, 1.0F, 0.7F, 0.9F, 0.12F),
	mediterranean(TempCategory.MEDIUM, 0.7F, 0.2F, 1.3F, 0.9F, 0.5F, -0.4F),
	subfrigid_plain(TempCategory.COLD, 0.5F, 0.3F, 0.6F, 0.7F, 0.28F, 0.12F),
	subfrigid_forest(TempCategory.COLD, 0.5F, 0.3F, 0.6F, 0.7F, 0.7F, 0.6F),
	frigid_desert(TempCategory.COLD, 0.4F, 0.3F, -.1F, 0.7F, 0.008F, 0.02F),
	frigid_plain(TempCategory.COLD, 0.4F, 0.3F, -.2F, 0.9F, 0.25F, 0.1F),
	iceland(TempCategory.COLD, 0.2F, 0.1F, -.8F, 1.0F, 0.03F, 0.01F),
	vertical(TempCategory.COLD, 0.6F, 0.05F, 0.8F, 1.3F, 0.4F, 0.3F),
	ocean(TempCategory.OCEAN, 0.8F, 0.3F, 1.0F, 0.6F, 0.7F, 0.3F),
	ocean_icy(TempCategory.OCEAN, 0.6F, 0.4F, 0.5F, 0.7F, 0.6F, 0.3F);
	
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
	
	public final TempCategory category;

	public final float[] sunshine;
	public final float sunshineAverage;
	
	public final float[] temperature;
	public final float temperatureAverage;
	
	public final float[] rain;
	public final float rainAverage;

	ClimaticZone(TempCategory category, float sb, float sf, float tb, float tf, float rb, float rf)
	{
		this(category, makeCosineFloats(12, sb, sf), makeCosineFloats(12, tb, tf), makeCosineFloats(12, rb, rf));
	}
	ClimaticZone(TempCategory category, float[] sunshine, float[] temperature, float[] rain)
	{
		this.category = category;
		this.sunshine = sunshine;
		sunshineAverage = Maths.average(sunshine);
		this.temperature = temperature;
		temperatureAverage = Maths.average(temperature);
		this.rain = rain;
		rainAverage = Maths.average(rain);
	}
}