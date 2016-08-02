package fargen.core.util;

import java.util.Arrays;

import farcore.util.U.Maths;

public enum ClimaticZone
{
	tropical_desert(1.2F, 0.1F, 3.5F, 0.2F, 0.01F, 0.005F),
	tropical_woodland(1.1F, 0.1F, 3.0F, 0.3F, 0.1F, 0.04F),
	tropical_plain(1.1F, 0.1F, 3.0F, 0.3F, 0.3F, 0.2F),
	tropical_monsoon(
			new float[]{0.98F, 0.996076952F, 1.04F, 1.1F, 1.16F, 1.203923048F, 1.22F, 1.203923048F, 1.16F, 1.1F, 1.04F, 0.996076952F},
			new float[]{2.7F, 2.740192379F, 2.85F, 3F, 3.15F, 3.259807621F, 3.3F, 3.259807621F, 3.15F, 3F, 2.85F, 2.740192379F},
			new float[]{0.05F, 0.127152269F, 0.60625F, 1.623312588F, 2.940359785F, 4.060433739F, 4.5F, 4.060433739F, 2.940359785F, 1.623312588F, 0.60625F, 0.127152269F}),
	tropical_rainforest(1.0F, 0.1F, 2.5F, 0.3F, 3.6F, 0.2F),
	subtropical_wet(0.9F, 0.15F, 2.2F, 0.5F, 2.3F, 0.8F),
	temperate_desert(0.8F, 0.15F, 1.2F, 0.6F, 0.06F, 0.02F),
	temperate_plain(0.75F, 0.2F, 1.0F, 0.9F, 0.6F, 0.3F),
	temperate_forest(0.75F, 0.2F, 1.2F, 0.9F, 1.3F, 0.5F),
	temperate_monsoon(0.7F, 0.2F, 1.1F, 1.0F, 1.1F, 0.9F),
	temperate_ocean(0.6F, 0.18F, 1.0F, 0.7F, 0.9F, 0.12F),
	mediterranean(0.7F, 0.2F, 1.3F, 0.9F, 0.5F, -0.4F),
	tundra(0.4F, 0.3F, -.2F, 0.9F, 0.3F, 0.1F),
	iceland(0.2F, 0.1F, -.8F, 1.0F, 0.03F, 0.01F),
	vertical(0.6F, 0.05F, 0.8F, 1.3F, 0.4F, 0.3F),
	ocean(0.8F, 0.3F, 1.0F, 0.6F, 0.7F, 0.3F);

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

	public final float[] sunshine;
	public final float sunshineAverage;

	public final float[] temperature;
	public final float temperatureAverage;

	public final float[] rain;
	public final float rainAverage;
	
	ClimaticZone(float sb, float sf, float tb, float tf, float rb, float rf)
	{
		this(makeCosineFloats(12, sb, sf), makeCosineFloats(12, tb, tf), makeCosineFloats(12, rb, rf));
	}
	ClimaticZone(float[] sunshine, float[] temperature, float[] rain)
	{
		this.sunshine = sunshine;
		sunshineAverage = Maths.average(sunshine);
		this.temperature = temperature;
		temperatureAverage = Maths.average(temperature);
		this.rain = rain;
		rainAverage = Maths.average(rain);
	}
}