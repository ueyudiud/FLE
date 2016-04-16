package fle.core.world.layer;

import java.util.Random;

import net.minecraft.world.gen.NoiseGeneratorOctaves;

public abstract class LayerNoise extends LayerBase
{
	protected int octave;
	protected NoiseGeneratorOctaves octaves;
	
	public LayerNoise(int octave, long seed)
	{
		super(seed);
		this.octave = octave;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		octaves = new NoiseGeneratorOctaves(new Random(seed), octave);
	}
}