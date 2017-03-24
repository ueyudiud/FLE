/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.layer.biome;

import fargen.core.layer.abstracts.LayerReplace;
import nebula.common.util.noise.NoiseBase;
import nebula.common.util.noise.NoisePerlin;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerSurfaceRainfall extends LayerReplace
{
	private NoiseBase noise = new NoisePerlin(0l, 3, 1.0, 2.0, 2.0);
	
	public LayerSurfaceRainfall(long seed, GenLayer layer)
	{
		super(seed, layer);
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		this.noise.setSeed(seed);
	}
	
	@Override
	protected int getValue(int x, int y, int id)
	{
		initChunkSeed(x, y >> 1);
		return id | (int) (this.noise.noise(x, y) * 8) << 4;
	}
}