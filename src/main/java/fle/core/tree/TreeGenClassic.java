/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.tree;

import java.util.Random;

import farcore.lib.bio.BioData;
import farcore.lib.tree.ITree;
import farcore.lib.tree.TreeGenAbstract;
import nebula.common.util.L;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TreeGenClassic extends TreeGenAbstract
{
	protected int	minHeight;
	protected int	randHeight;
	
	public TreeGenClassic(ITree tree, float generateCoreLeavesChance)
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
	public boolean generateTreeAt(World world, int x, int y, int z, Random rand, BioData info)
	{
		int l = this.minHeight + L.nextInt(this.randHeight, rand);
		if (!checkLogGrow(world, x, y, z, 1, 0, 1, false) || !checkLogGrow(world, x, y + 1, z, 2, l, 2, true)) return false;
		
		BlockPos pos;
		IBlockState state = world.getBlockState(pos = new BlockPos(x, y - 1, z));
		byte b0;
		int k1;
		
		boolean isSoil = state.getBlock().canSustainPlant(state, world, pos, EnumFacing.UP, (BlockSapling) Blocks.SAPLING);
		if (isSoil && y < 256 - l - 1)
		{
			info = checkData(info, rand);
			
			state.getBlock().onPlantGrow(state, world, pos, pos.up());
			b0 = 3;
			byte b1 = 0;
			int l1;
			int i2;
			int j2;
			int i3;
			
			for (k1 = y - b0 + l; k1 <= y + l; ++k1)
			{
				i3 = k1 - (y + l);
				
				for (i2 = x - (l1 = b1 + 1 - i3 / 2); i2 <= x + l1; ++i2)
				{
					j2 = i2 - x;
					
					for (int k2 = z - l1; k2 <= z + l1; ++k2)
					{
						int l2 = k2 - z;
						
						if (Math.abs(j2) != l1 || Math.abs(l2) != l1 || rand.nextInt(2) != 0 && i3 != 0)
						{
							generateTreeLeaves(world, i2, k1, k2, 0, this.generateCoreLeavesChance, rand, info);
						}
					}
				}
			}
			
			for (k1 = 0; k1 < l; ++k1)
			{
				if (isLogReplaceable(world, x, y + k1, z))
				{
					generateLog(world, x, y + k1, z, 1);
				}
			}
			
			return true;
		}
		else
			return false;
	}
}
