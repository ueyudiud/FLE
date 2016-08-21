package fargen.core.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerZoom extends Layer
{
	public LayerZoom(int octave, long seed, GenLayer layer)
	{
		super(seed);
		for(int i = 0; i < octave - 1; ++i)
		{
			layer = new LayerZoom(seed + i, layer);
		}
		parent = layer;
		zoomLevel = 2;
	}

	public LayerZoom(long seed, GenLayer layer)
	{
		super(seed);
		parent = layer;
		zoomLevel = 2;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int i1 = x >> 1;
		int j1 = y >> 1;
		int k1 = (w >> 1) + 2;
		int l1 = (h >> 1) + 2;
		int[] aint = parent.getInts(i1, j1, k1, l1);
		int i2 = k1 - 1 << 1;
		int j2 = l1 - 1 << 1;
		int[] aint1 = IntCache.getIntCache(i2 * j2);
		int l2;
		
		for (int k2 = 0; k2 < l1 - 1; ++k2)
		{
			l2 = (k2 << 1) * i2;
			int i3 = 0;
			int j3 = aint[i3 + 0 + (k2 + 0) * k1];
			
			for (int k3 = aint[i3 + 0 + (k2 + 1) * k1]; i3 < k1 - 1; ++i3)
			{
				initChunkSeed(i3 + i1 << 1, k2 + j1 << 1);
				int l3 = aint[i3 + 1 + (k2 + 0) * k1];
				int i4 = aint[i3 + 1 + (k2 + 1) * k1];
				aint1[l2] = j3;
				aint1[l2++ + i2] = selectRandom(j3, k3);
				aint1[l2] = selectRandom(j3, l3);
				aint1[l2++ + i2] = selectModeOrRandom(j3, l3, k3, i4);
				j3 = l3;
				k3 = i4;
			}
		}
		
		int[] aint2 = IntCache.getIntCache(w * h);
		
		for (l2 = 0; l2 < h; ++l2)
		{
			System.arraycopy(aint1, (l2 + (y & 1)) * i2 + (x & 1), aint2, l2 * w, w);
		}
		
		return aint2;
	}
}