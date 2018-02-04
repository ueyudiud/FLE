/*
 * copyright© 2016-2018 ueyudiud
 */
package fargen.core.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

/**
 * @author ueyudiud
 */
public class LayerZoom extends Layer
{
	public LayerZoom(long seed, GenLayer layer)
	{
		super(seed);
		this.parent = layer;
	}
	
	public LayerZoom(long seed, int magnify, GenLayer layer)
	{
		super(seed);
		this.parent = magnify > 1 ? new LayerZoom(seed + 1L, magnify - 1, layer) : layer;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int i = x >> 1;
		int j = y >> 1;
		int k = (w >> 1) + 2;
		int l = (h >> 1) + 2;
		int[] aint = this.parent.getInts(i, j, k, l);
		int i1 = k - 1 << 1;
		int j1 = l - 1 << 1;
		int[] aint1 = IntCache.getIntCache(i1 * j1);
		
		for (int k1 = 0; k1 < l - 1; ++k1)
		{
			int l1 = (k1 << 1) * i1;
			int i2 = 0;
			int j2 = aint[i2 + 0 + (k1 + 0) * k];
			
			for (int k2 = aint[i2 + 0 + (k1 + 1) * k]; i2 < k - 1; ++i2)
			{
				initChunkSeed(i2 + i << 1, k1 + j << 1);
				int l2 = aint[i2 + 1 + (k1 + 0) * k];
				int i3 = aint[i2 + 1 + (k1 + 1) * k];
				aint1[l1] = j2;
				aint1[l1++ + i1] = selectRandom(j2, k2);
				aint1[l1] = selectRandom(j2, l2);
				aint1[l1++ + i1] = selectModeOrRandom(j2, l2, k2, i3);
				j2 = l2;
				k2 = i3;
			}
		}
		
		int[] aint2 = IntCache.getIntCache(w * h);
		
		for (int j3 = 0; j3 < h; ++j3)
		{
			System.arraycopy(aint1, (j3 + (y & 1)) * i1 + (x & 1), aint2, j3 * w, w);
		}
		
		return aint2;
	}
}
