package fargen.core.layer;

import net.minecraft.world.gen.layer.GenLayer;

public class LayerFuzzyZoom extends LayerZoom
{
	public LayerFuzzyZoom(int octave, long seed, GenLayer layer)
	{
		super(seed, null);
		for(int i = 0; i < octave - 1; ++i)
		{
			layer = new LayerFuzzyZoom(seed + i, layer);
		}
		parent = layer;
	}
	public LayerFuzzyZoom(long seed, GenLayer layer)
	{
		super(seed, layer);
	}
	
	@Override
	protected int selectModeOrRandom(int a, int b, int c, int d)
	{
		return selectRandom(a, b, c, d);
	}
}