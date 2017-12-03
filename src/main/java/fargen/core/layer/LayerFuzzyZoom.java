/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.layer;

import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerFuzzyZoom extends LayerZoom
{
	public LayerFuzzyZoom(long seed, GenLayer layer)
	{
		super(seed, layer);
	}
	
	public LayerFuzzyZoom(long seed, int magnify, GenLayer layer)
	{
		super(seed, magnify > 1 ? new LayerFuzzyZoom(seed + 1L, magnify - 1, layer) : layer);
	}
	
	@Override
	protected int selectModeOrRandom(int a, int b, int c, int d)
	{
		return selectRandom(a, b, c, d);
	}
}
