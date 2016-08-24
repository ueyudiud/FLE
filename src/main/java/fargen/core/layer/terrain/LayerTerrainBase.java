package fargen.core.layer.terrain;

import farcore.data.EnumTerrain;
import fargen.core.layer.Layer;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LayerTerrainBase extends Layer
{
	private GenLayer chunk;
	
	public LayerTerrainBase(long seed, GenLayer layer1, GenLayer layer2)
	{
		super(seed);
		parent = layer1;
		chunk = layer2;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] ret = IntCache.getIntCache(w * h);
		int[] par = parent.getInts(x, y, w, h);
		int[] chu = chunk.getInts(x, y, w, h);
		int l = w * h;
		for(int i = 0; i < l; ++i)
		{
			int a = par[i];
			int b = chu[i];
			if(a < 4)
			{
				if(b == 1 || b == 3)
				{
					ret[i] = EnumTerrain.tectogene.ordinal();
				}
				else
				{
					ret[i] = EnumTerrain.deep_ocean.ordinal();
				}
			}
			else if(a < 7)
			{
				if(b == 3)
				{
					ret[i] = EnumTerrain.tectogene.ordinal();
				}
				else if(b == 2)
				{
					ret[i] = EnumTerrain.ridge.ordinal();
				}
				else
				{
					ret[i] = EnumTerrain.ocean.ordinal();
				}
			}
			else if(a == 7)
			{
				if(b == 3)
				{
					ret[i] = EnumTerrain.ocean.ordinal();
				}
				else if(b == 2)
				{
					ret[i] = EnumTerrain.ridge_high.ordinal();
				}
				else if(b == 1)
				{
					ret[i] = EnumTerrain.ocean_valley.ordinal();
				}
				else
				{
					ret[i] = EnumTerrain.channel.ordinal();
				}
			}
			else if(a < 13)
			{
				if(b == 3)
				{
					ret[i] = EnumTerrain.mountain.ordinal();
				}
				else if(b == 2)
				{
					ret[i] = EnumTerrain.hills.ordinal();
				}
				else if(b == 1)
				{
					ret[i] = a == 8 ? EnumTerrain.depression.ordinal() :
						EnumTerrain.plain.ordinal();
				}
				else
				{
					ret[i] = a == 8 ? EnumTerrain.depression.ordinal() :
						a < 11 ? EnumTerrain.plain.ordinal() : EnumTerrain.hills.ordinal();;
				}
			}
			else if(a < 15)
			{
				if(b == 3)
				{
					ret[i] = EnumTerrain.ex_mountain.ordinal();
				}
				else if(b == 2 || b == 1)
				{
					ret[i] = EnumTerrain.hi_mountain.ordinal();
				}
				else
				{
					ret[i] = EnumTerrain.mountain.ordinal();
				}
			}
			else
			{
				if(b == 3)
				{
					ret[i] = EnumTerrain.ex_mountain.ordinal();
				}
				else if(b == 2 || b == 1)
				{
					ret[i] = EnumTerrain.hi_mountain.ordinal();
				}
				else
				{
					ret[i] = EnumTerrain.plateau.ordinal();
				}
			}
		}
		return ret;
	}

	@Override
	public void markZoom(int zoom)
	{
		super.markZoom(zoom);
		if(chunk instanceof Layer)
		{
			((Layer) chunk).markZoom(zoom * zoomLevel);
		}
	}
	
	@Override
	public void initWorldGenSeed(long seed)
	{
		super.initWorldGenSeed(seed);
		chunk.initWorldGenSeed(seed);
	}
}