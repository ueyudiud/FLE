/*
 * copyright 2016-2018 ueyudiud
 */
package fargen.api.terrain;

import static nebula.common.util.Maths.lerp;

/**
 * @author ueyudiud
 */
public class TerrainSimple extends Terrain
{
	public TerrainSimple(int id, float baseHeight, float heightVariation)
	{
		this.id = id;
		this.baseHeight = baseHeight;
		this.heightVariation = heightVariation;
	}
	
	@Override
	public void blendTerrainData(float[] total, float[] count, float weight,
			Terrain main, long seed, int x, int z)
	{
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
	public void mapTerrainData(double[] result, int offset, int height, float[] data,
			double randHeight, double[] main, double[] max, double[] min, long seed, int x, int z)
	{
		// Rescaled
		double d1 = (height - 1) * (data[BASE_HEIGHT] * 0.15625 + 0.125 + 0.03125);
		double d2 = 1.0 + data[HEIGHT_VARIATION] * VARIATION_SUPPLIER.applyAsDouble(randHeight);
		// Height calculate
		int i1 = offset;
		for (int y = 1; y <= height; y++)
		{
			double off = (d1 - y) / d2;
			if (off > 0.0)
			{
				off *= 4.0;
			}
			double output = off + lerp(min[i1], max[i1], main[i1]) * 8.0 - 4.0;
			if (y > height - 4)
			{
				output = lerp(- 4.0, output, (height - y) / 3.0F);
			}
			result[i1 ++] = output;
		}
	}
}