/*
 * copyright 2016-2018 ueyudiud
 */
package fargen.api.terrain;

import java.util.function.DoubleUnaryOperator;

/**
 * The terrain of world, used to provide terrain data.
 * 
 * @author ueyudiud
 */
public abstract class Terrain
{
	public static final int
	BASE_HEIGHT = 0,
	HEIGHT_VARIATION = 1,
	VALLEY_DEPTH = 2;
	
	public static final int DATA_COUNT = 3;
	
	protected static final DoubleUnaryOperator VARIATION_SUPPLIER = x -> {
		// [0.0, 1.0]
		x = x * 3.0 - 1.0;
		// [-1.0, 2.0]
		if (x < 0)
		{
			x *= -1.5;
		}
		else if (x > 1.0)
		{
			x = x * 2.0 - 1.0;
		}
		// [0.0, 3.0]
		return x * 3.0;
	};
	
	/** The index of terrain, it only use to mark in generation layer. */
	public int id;
	/** The base height of this terrain. */
	protected float baseHeight;
	/** The height variation of this terrain. */
	protected float heightVariation;
	
	/**
	 * Blend all terrain data.
	 * 
	 * @param total the all data total count.
	 * @param count the all weight total count.
	 * @param weight the weight allocated for this location.
	 * @param main the main terrain.
	 * @param seed the world seed.
	 * @param x the x position.
	 * @param z the z position.
	 */
	public abstract void blendTerrainData(float[] total, float[] count, float weight,
			Terrain main, long seed, int x, int z);
	
	/**
	 * Mapped terrain height data to result, with positive value for filled with rock
	 * and negative value for empty, the world provider will use linear interpolation
	 * to give value in middle.
	 * <p>
	 * The <tt>main</tt>, <tt>max</tt>, <tt>minimum</tt> and <tt>random</tt> height
	 * are all generated by each noise generator, for terrain to used.
	 * 
	 * @param result the result map.
	 * @param offset the offset stored in array.
	 * @param height the height of array.
	 * @param data the generated data in this position (Include base height, height variation, etc).
	 * @param randHeight the random height.
	 * @param main the main terrain height.
	 * @param max the max terrain height.
	 * @param min the minimum terrain height.
	 * @param seed the world seed.
	 * @param x the x position.
	 * @param z the z position.
	 */
	public abstract void mapTerrainData(double[] result, int offset, int height, float[] data,
			double randHeight, double[] main, double[] max, double[] min, long seed, int x, int z);
}