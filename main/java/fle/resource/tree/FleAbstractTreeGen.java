package fle.resource.tree;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.world.BlockPos;

public abstract class FleAbstractTreeGen extends WorldGenAbstractTree
{
	public FleAbstractTreeGen(boolean flag)
	{
		super(flag);
	}
	
	protected boolean isBlockPlant(Block block)
	{
		return func_150523_a(block);
	}
	
	public abstract boolean generate(World world, Random rand, int x, int y, int z);

	protected int getGrowHeight(World world, int x, int y, int z, final int width, final int height, final float growHardness)
	{
		BlockPos pos = new BlockPos(world, x, y, z);
		Block base = pos.toPos(ForgeDirection.DOWN).getBlock();
		if(!base.isSideSolid(world, x, y, z, ForgeDirection.UP))
			return 0;
		int height1;
		int c;
		BlockPos pos1 = pos.toPos(ForgeDirection.UP);
		for (height1 = 1; height1 < height; height1++)
		{
			if(pos1.getBlock().isAir(world, x, y, z))
			{
				pos1 = pos1.toPos(ForgeDirection.UP);
				c = 0;
				for(int i = -width; i <= width; ++i)
					for(int j = -width; j <= width; ++j)
						if(!pos1.toPos(i, 0, j).isAir()) ++c;
				if(c > 4 * width * width / growHardness) break;
				continue;
			}
			else
			{
				height1 -= 1;
				break;
			}
		}
		return height1;
	}
	
	@Override
	protected void setBlockAndNotifyAdequately(World world,
			int x, int y, int z,
			Block block, int meta)
	{
		super.setBlockAndNotifyAdequately(world, x, y, z, block, meta);
	}
}