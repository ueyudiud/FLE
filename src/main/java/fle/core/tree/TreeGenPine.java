/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tree;

import java.util.Random;

import farcore.lib.tree.ITree;
import farcore.lib.tree.TreeGenAbstract;
import farcore.lib.tree.TreeInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class TreeGenPine extends TreeGenAbstract
{
	protected int	minHeight;
	protected int	randHeight;
	
	public TreeGenPine(ITree tree, float generateCoreLeavesChance)
	{
		super(tree, generateCoreLeavesChance);
	}
	
	@Override
	public void setHeight(int minHeight, int randHeight)
	{
		this.minHeight = minHeight;
		this.randHeight = randHeight;
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		BlockPos pos = new BlockPos(x, y, z);
		int height = random.nextInt(this.randHeight) + this.minHeight;
		int leavesStart = height - random.nextInt(2) - 3;
		int leavesLength = height - leavesStart;
		int checkRange = 1 + random.nextInt(leavesLength + 1);
		
		if (!checkLogGrow(world, x, y + 1, z, checkRange, height, checkRange, true))
		{
			return false;
		}
		else
		{
			BlockPos down = pos.down();
			IBlockState state = world.getBlockState(down);
			boolean isSoil = state.getBlock().canSustainPlant(state, world, down, EnumFacing.UP, (net.minecraft.block.BlockSapling) Blocks.SAPLING);
			
			if (isSoil && y < 256 - height - 1)
			{
				state.getBlock().onPlantGrow(state, world, down, pos);
				int k2 = 0;
				
				for (int l2 = y + height; l2 >= y + leavesStart; --l2)
				{
					for (int j3 = x - k2; j3 <= x + k2; ++j3)
					{
						boolean flag1 = Math.abs(j3 - x) != k2;
						
						for (int i2 = z - k2; i2 <= z + k2; ++i2)
						{
							boolean flag2 = Math.abs(i2 - z) != k2;
							
							if (flag1 || flag2 || k2 <= 0)
							{
								generateTreeLeaves(world, j3, l2, i2, 0, random, info);
							}
						}
					}
					
					if (k2 >= 1 && l2 == y + leavesStart + 1)
					{
						--k2;
					}
					else if (k2 < checkRange)
					{
						++k2;
					}
				}
				
				for (int yOff = 0; yOff < height - 1; ++yOff)
				{
					generateLog(world, x, y + yOff, z, 0);
				}
				
				return true;
			}
			else
			{
				return false;
			}
		}
	}
}
