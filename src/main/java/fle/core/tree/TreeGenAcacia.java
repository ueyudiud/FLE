/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tree;

import java.util.Random;

import farcore.lib.tree.ITree;
import farcore.lib.tree.TreeGenAbstract;
import farcore.lib.tree.TreeInfo;
import nebula.common.util.Direction;
import nebula.common.util.L;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TreeGenAcacia extends TreeGenAbstract
{
	protected int	randHeightHlf1;
	protected int	randHeightHlf2;
	protected int	baseHeight;
	
	public TreeGenAcacia(ITree tree, float generateCoreLeavesChance)
	{
		super(tree, generateCoreLeavesChance);
	}
	
	@Override
	public void setHeight(int baseHeight, int randHeight)
	{
		this.baseHeight = baseHeight;
		this.randHeightHlf1 = randHeight / 2;
		this.randHeightHlf2 = randHeight - this.randHeightHlf1;
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		int l = L.nextInt(this.randHeightHlf1, random) + L.nextInt(this.randHeightHlf2, random) + this.baseHeight;
		
		if (y >= 1 && y + l + 1 <= 256)
		{
			int j1;
			int k1;
			
			if (!checkLogGrow(world, x, y + 1, z, 2, l - 2, 2, true)) return false;
			if (!checkLogGrow(world, x, y + l - 1, z, 1, 2, 1, true)) return false;
			BlockPos pos = new BlockPos(x, y - 1, z);
			IBlockState state = world.getBlockState(pos);
			
			boolean isSoil = state.getBlock().canSustainPlant(state, world, pos, EnumFacing.UP, (BlockSapling) Blocks.SAPLING);
			if (isSoil && y < 256 - l - 1)
			{
				state.getBlock().onPlantGrow(state, world, pos, pos.up());
				int j3 = random.nextInt(4);
				j1 = l - random.nextInt(4) - 1;
				k1 = 3 - random.nextInt(3);
				int k3 = x;
				int l1 = z;
				int i2 = 0;
				int j2;
				int k2;
				
				for (j2 = 0; j2 < l; ++j2)
				{
					k2 = y + j2;
					
					if (j2 >= j1 && k1 > 0)
					{
						k3 += Direction.DIRECTIONS_2D[j3].x;
						l1 += Direction.DIRECTIONS_2D[j3].z;
						--k1;
					}
					
					if (isLogReplaceable(world, k3, k2, l1))
					{
						generateLog(world, k3, k2, l1, 1);
						i2 = k2;
					}
				}
				
				for (j2 = -1; j2 <= 1; ++j2)
				{
					for (k2 = -1; k2 <= 1; ++k2)
						if (isLogReplaceable(world, k3 + j2, i2 + 1, l1 + k2))
						{
							generateTreeLeaves(world, k3 + j2, i2 + 1, l1 + k2, 0, random, info);
						}
				}
				
				generateTreeLeaves(world, k3 + 2, i2 + 1, l1, 0, random, info);
				generateTreeLeaves(world, k3 - 2, i2 + 1, l1, 0, random, info);
				generateTreeLeaves(world, k3, i2 + 1, l1 + 2, 0, random, info);
				generateTreeLeaves(world, k3, i2 + 1, l1 - 2, 0, random, info);
				for (j2 = -3; j2 <= 3; ++j2)
				{
					for (k2 = -3; k2 <= 3; ++k2)
						if (Math.abs(j2) != 3 || Math.abs(k2) != 3)
						{
							generateTreeLeaves(world, k3 + j2, i2, l1 + k2, 0, random, info);
						}
				}
				
				k3 = x;
				l1 = z;
				j2 = random.nextInt(4);
				
				if (j2 != j3)
				{
					k2 = j1 - random.nextInt(2) - 1;
					int l3 = 1 + random.nextInt(3);
					i2 = 0;
					int l2;
					int i3;
					
					for (l2 = k2; l2 < l && l3 > 0; --l3)
					{
						if (l2 >= 1)
						{
							i3 = y + l2;
							k3 += Direction.DIRECTIONS_2D[j2].x;
							l1 += Direction.DIRECTIONS_2D[j2].z;
							if (isLogReplaceable(world, k3, i3, l1))
							{
								generateLog(world, k3, i3, l1, 1);
								i2 = i3;
							}
						}
						
						++l2;
					}
					
					if (i2 > 0)
					{
						for (l2 = -1; l2 <= 1; ++l2)
						{
							for (i3 = -1; i3 <= 1; ++i3)
							{
								generateTreeLeaves(world, k3 + l2, i2 + 1, l1 + i3, 0, random, info);
							}
						}
						
						for (l2 = -2; l2 <= 2; ++l2)
						{
							for (i3 = -2; i3 <= 2; ++i3)
								if (Math.abs(l2) != 2 || Math.abs(i3) != 2)
								{
									generateTreeLeaves(world, k3 + l2, i2, l1 + i3, 0, random, info);
								}
						}
					}
				}
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
}
