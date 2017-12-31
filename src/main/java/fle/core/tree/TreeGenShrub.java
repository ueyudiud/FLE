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
public class TreeGenShrub extends TreeGenAbstract
{
	public TreeGenShrub(ITree tree, float generateCoreLeavesChance)
	{
		super(tree, generateCoreLeavesChance);
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if (!checkLogGrow(world, x, y, z, 1, 1, 2, false)) return false;
		BlockPos pos = new BlockPos(x, y, z);
		IBlockState state = world.getBlockState(pos);
		
		if (state.getBlock().canSustainPlant(state, world, pos, EnumFacing.UP, ((net.minecraft.block.BlockSapling) Blocks.SAPLING)))
		{
			state.getBlock().onPlantGrow(state, world, pos, pos.up());
			
			generateLog(world, x, ++y, z, 0);
			
			for (int yOff = 0; yOff <= 2; ++yOff)
			{
				int y1 = yOff + y;
				int len = 2 - yOff;
				
				for (int x1 = x - len; x1 <= x + len; ++x1)
				{
					boolean flag1 = Math.abs(x1 - x) != len;
					
					for (int z1 = z - len; z1 <= z + len; ++z1)
					{
						boolean flag2 = Math.abs(z1 - z) != len;
						
						if (flag1 || flag2 || random.nextInt(2) != 0)
						{
							generateTreeLeaves(world, x1, y1, z1, 0, random, info);
						}
					}
				}
			}
			return true;
		}
		
		return false;
	}
}
