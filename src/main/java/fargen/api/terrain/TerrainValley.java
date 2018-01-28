/*
 * copyright© 2016-2018 ueyudiud
 */
package fargen.api.terrain;

import static nebula.common.util.Maths.clampLerp;
import static nebula.common.util.Maths.lerp;

/**
 * @author ueyudiud
 */
public class TerrainValley extends Terrain
{
	protected float valleyWeight;
	
	public TerrainValley(int id, float baseHeight, float heightVariation, float valleyWeight)
	{
		this.id = id;
		this.baseHeight = baseHeight;
		this.heightVariation = heightVariation;
		this.valleyWeight = valleyWeight;
	}
	
	@Override
	public void blendTerrainData(float[] total, float[] count, float weight, Terrain main, long seed, int x, int z)
	{
		total[VALLEY_DEPTH] += weight * this.valleyWeight;
		count[VALLEY_DEPTH] += weight;
		if (this.baseHeight > main.baseHeight)
		{
			weight /= 2.0F;
		}
		total[BASE_HEIGHT] += this.baseHeight * weight;
		total[HEIGHT_VARIATION] += this.heightVariation * weight;
		count[BASE_HEIGHT] += weight;
		count[HEIGHT_VARIATION] += weight;
	}
	
	@Override
	public void mapTerrainData(double[] result, int offset, int height, float[] data, double randHeight, double[] main, double[] max, double[] min, long seed, int x, int z)
	{
		// Rescaled
		double d1 = (height - 1) * (data[BASE_HEIGHT] * 0.15625 + 0.125 + 0.03125);
		double d2 = 1.0 + data[HEIGHT_VARIATION] * VARIATION_SUPPLIER.applyAsDouble(randHeight);
		double d3 = data[VALLEY_DEPTH] * 2.0 / (1 + data[VALLEY_DEPTH]);
		if (d2 < 0.3) d2 = 1.0 + (d2 - 0.3) * 0.4;
		// Height calculate
		int i1 = offset;
		for (int y = 1; y <= height; y++)
		{
			double off = (d1 - y) / d2;
			if (off > 0.0)
			{
				off *= 4.0;
			}
			double d4 = lerp(min[i1], max[i1], main[i1]) * 8.0 - 4.0;
			double output = off + clampLerp(d4 - 0.5, -4.0 * d4, d3 * main[i1]);
			if (y > height - 4)
			{
				output = lerp(- 4.0, output, (height - y) / 3.0F);
			}
			result[i1 ++] = output;
		}
	}
}