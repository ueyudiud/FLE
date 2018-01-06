/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tile.instance;

import static nebula.common.util.L.index8i;

import java.util.Arrays;

import farcore.data.Config;
import nebula.client.util.Lights;
import nebula.common.util.L;
import nebula.common.world.IBlockCoordQuarterProperties;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
class BrightnessProviderCarvedStone implements IBlockCoordQuarterProperties
{
	final TECustomCarvedStone T;
	
	private byte[] lightmapSky		= new byte[64];
	private byte[] lightmapBlock	= new byte[64];
	
	BrightnessProviderCarvedStone(TECustomCarvedStone tile)
	{
		this.T = tile;
	}
	
	void generateLightmap()
	{
		if (!Config.splitBrightnessOfSmallBlock)
		{
			return;
		}
		Arrays.fill(this.lightmapBlock, (byte) 0);
		Arrays.fill(this.lightmapSky, (byte) 0);
		int light = this.T.getLightValue(this.T.getBlockState());
		generateLightmap(EnumSkyBlock.SKY, this.lightmapSky, light);
		generateLightmap(EnumSkyBlock.BLOCK, this.lightmapBlock, light);
	}
	
	private int getLight(EnumFacing facing, EnumSkyBlock type)
	{
		return world().getLightFor(type, pos().offset(facing));
	}
	
	private void generateLightBySide(EnumSkyBlock type, byte[] lightmap, EnumFacing facing, int minLight)
	{
		byte l1 = (byte) (getLight(facing, type) << 4 & 0xFF);
		int idx;
		if (l1 > minLight)
		{
			for (int i = 0; i < 4; ++i)
			{
				for (int j = 0; j < 4; ++j)
				{
					switch (facing)
					{
					case UP    : idx = index8i(i, 3, j); break;
					case DOWN  : idx = index8i(i, 0, j); break;
					case SOUTH : idx = index8i(i, j, 3); break;
					case NORTH : idx = index8i(i, j, 0); break;
					case EAST  : idx = index8i(3, i, j); break;
					case WEST  : idx = index8i(0, i, j); break;
					default    : idx = 0; break;
					}
					if ((this.T.carvedState & idx) == 0 && lightmap[idx] < l1)
					{
						lightmap[idx] = l1;
					}
				}
			}
		}
	}
	
	private void generateLightmap(EnumSkyBlock type, byte[] lightmap, int minLight)
	{
		byte l1 = (byte) (getLight(EnumFacing.UP, type) << 4 & 0xFF);
		int idx;
		if (l1 > minLight)
		{
			for (int i = 0; i < 4; ++i)
			{
				for (int j = 0; j < 4; ++j)
				{
					if (type == EnumSkyBlock.SKY)
					{
						int k = 4;
						while (k > 0 && this.T.isCarved(i, --k, j))
						{
							lightmap[index8i(i, k, j)] = l1;
						}
					}
					else if (lightmap[idx = index8i(i, 3, j)] < l1)
					{
						lightmap[idx] = l1;
					}
				}
			}
		}
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			if (facing == EnumFacing.UP)
				continue;
			generateLightBySide(type, lightmap, facing, minLight);
		}
		for (int i = 0; i < 4; ++i)
		{
			for (int j = 0; j < 4; ++j)
			{
				for (int k = 0; k < 4; ++k)
				{
					scanLight(i, j, k, (byte) 0x2F, -1, lightmap);
				}
			}
		}
	}
	
	private void scanLight(int x, int y, int z, byte side, int light, byte[] lightmap)
	{
		int idx = index8i(x, y, z);
		if ((this.T.carvedState & idx) != 0) return;
		if (light < 0)
		{
			light = L.uint(lightmap[idx]);
		}
		else
		{
			if (lightmap[idx] >= light) return;
			lightmap[idx] = (byte) light;
		}
		if (light >= 4)
		{
			light -= 4;
			if ((side & 0x1) != 0 && x > 0 && L.uint(lightmap[idx - 0x1]) < light)
			{
				scanLight(x - 1, y, z, (byte) (side & ~0x2), light, lightmap);
			}
			if ((side & 0x2) != 0 && x < 3 && L.uint(lightmap[idx + 0x1]) < light)
			{
				scanLight(x + 1, y, z, (byte) (side & ~0x1), light, lightmap);
			}
			if ((side & 0x4) != 0 && y > 0 && L.uint(lightmap[idx - 0x4]) < light)
			{
				scanLight(x, y - 1, z, (byte) (side & ~0x8), light, lightmap);
			}
			if ((side & 0x8) != 0 && y < 3 && L.uint(lightmap[idx + 0x4]) < light)
			{
				scanLight(x, y + 1, z, (byte) (side & ~0x4), light, lightmap);
			}
			if ((side & 0x10) != 0 && z > 0 && L.uint(lightmap[idx - 0x10]) < light)
			{
				scanLight(x, y, z - 1, (byte) (side & ~0x20), light, lightmap);
			}
			if ((side & 0x20) != 0 && z < 3 && L.uint(lightmap[idx + 0x10]) < light)
			{
				scanLight(x, y, z + 1, (byte) (side & ~0x10), light, lightmap);
			}
		}
	}
	
	@Override
	public World world()
	{
		return this.T.world();
	}
	
	@Override
	public BlockPos pos()
	{
		return this.T.pos();
	}
	
	@Override
	public float getAmbientOcclusionLightValueLocal(int x, int y, int z)
	{
		return this.T.isCarved(x, y, z) ? 1.0F : 0.3F;
	}
	
	@Override
	public int getBrightnessLocal(int x, int y, int z)
	{
		int idx = index8i(x, y, z);
		return Config.splitBrightnessOfSmallBlock ?
				Lights.mixSkyBlockLight(this.lightmapSky[idx], this.lightmapBlock[idx]) :
					world().getCombinedLight(pos(), 0);
	}
	
	@Override
	public float getOpaquenessLocal(int x, int y, int z)
	{
		return this.T.isCarved(x, y, z) ? 0.0F : 1.0F;
	}
}
