package fargen.core.layer;

import farcore.lib.util.NoiseBase;

public abstract class LayerNoise extends Layer
{
	protected NoiseBase noise;
	
	public LayerNoise(long seed, NoiseBase noise)
	{
		super(seed);
		this.noise = noise;
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		seed ^= 8282391727483728173L;
		seed += 1442695040888963407L + baseSeed;
		seed ^= 3849172473827591721L;
		seed += 4735385037286738169L + baseSeed;
		noise.setSeed(seed);
	}
}
