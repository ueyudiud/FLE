package fle.core.world.layer;

import java.util.Random;

import farcore.util.noise.NoiseBasic;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public abstract class LayerNoise extends LayerBase
{
	protected int octave;
	protected NoiseBasic noise;
	
	public LayerNoise(NoiseBasic noise, long seed)
	{
		super(seed);
		this.noise = noise;;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		noise.setSeed(seed);
	}
}