package fle.core.world.climate.surface;

import java.util.Arrays;
import java.util.Random;

import fle.core.util.BlockInfo;
import fle.core.world.climate.Climate;
import net.minecraft.init.Blocks;

public class ClimateOcean extends Climate
{
	private static final BlockInfo INFO1 = new BlockInfo(Blocks.sand);
	private static final BlockInfo INFO2 = new BlockInfo(Blocks.gravel);
	
	public ClimateOcean(int id, String name)
	{
		super(id, name);
	}
	
	@Override
	public BlockInfo[] genTopBlock(int x, int z, Random random, int height, boolean underWater, boolean isFirstTop, double layer)
	{
		BlockInfo[] infos = new BlockInfo[height];
		Arrays.fill(infos, INFO2);
		if(!underWater && height > 0)
		{
			infos[0] = INFO1;
		}
		return infos;
	}
}