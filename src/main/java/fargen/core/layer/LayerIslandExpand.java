/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.layer;

import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerIslandExpand extends Layer
{
	private final int expand;
	
	public LayerIslandExpand(long seed, GenLayer layer, int expand)
	{
		super(seed);
		this.parent = layer;
		this.expand = expand;
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int var5 = x - 1;
		int var6 = y - 1;
		int var7 = w + 2;
		int var8 = h + 2;
		int[] par = this.parent.getInts(var5, var6, var7, var8);
		int[] result = array(w, h);
		for (int var11 = 0; var11 < h; var11++)
		{
			for (int var12 = 0; var12 < w; var12++)
			{
				int a1 = par[(var12 + 0 + (var11 + 0) * var7)];
				int a2 = par[(var12 + 2 + (var11 + 0) * var7)];
				int a3 = par[(var12 + 0 + (var11 + 2) * var7)];
				int a4 = par[(var12 + 2 + (var11 + 2) * var7)];
				int a = par[(var12 + 1 + (var11 + 1) * var7)];
				initChunkSeed(var12 + x, var11 + y);
				if ((a == 0) && ((a1 != 0) || (a2 != 0) || (a3 != 0) || (a4 != 0)))
				{
					if (nextInt(3) == 0)
					{
						int chance = 1;
						int id;
						if (this.expand == -1)
						{
							id = a1;
						}
						else
						{
							id = this.expand;
							if ((a1 != 0) && (nextInt(chance++) == 0))
							{
								id = a1;
							}
						}
						if ((a2 != 0) && (nextInt(chance++) == 0))
						{
							id = a2;
						}
						if ((a3 != 0) && (nextInt(chance++) == 0))
						{
							id = a3;
						}
						if ((a4 != 0) && (nextInt(chance++) == 0))
						{
							id = a4;
						}
						result[(var12 + var11 * w)] = id;
					}
					else
					{
						result[(var12 + var11 * w)] = 0;
					}
				}
				else if ((a > 0) && ((a1 == 0) || (a2 == 0) || (a3 == 0) || (a4 == 0)))
				{
					if (nextInt(5) == 0)
					{
						result[(var12 + var11 * w)] = 0;
					}
					else
					{
						result[(var12 + var11 * w)] = a;
					}
				}
				else
				{
					result[(var12 + var11 * w)] = a;
				}
			}
		}
		return result;
	}
}