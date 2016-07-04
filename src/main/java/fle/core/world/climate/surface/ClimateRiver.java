package fle.core.world.climate.surface;

import java.util.Arrays;
import java.util.Random;

import fle.core.util.BlockInfo;
import fle.core.world.climate.Climate;
import net.minecraft.init.Blocks;

public class ClimateRiver extends Climate
{
	public ClimateRiver(int id, String name)
	{
		super(id, name);
	}
	
	@Override
	public BlockInfo[] genTopBlock(int x, int z, Random random, int height, boolean underWater, boolean isFirstTop, double layer)
	{
		BlockInfo[] infos = new BlockInfo[height];
		Arrays.fill(infos, new BlockInfo(Blocks.dirt));
		if(!underWater && height > 0)
		{
			infos[0] = new BlockInfo(Blocks.grass);
		}
		if(height > 1)
		{
			infos[height - 1] = new BlockInfo(Blocks.gravel);
		}
		return infos;
	}
}