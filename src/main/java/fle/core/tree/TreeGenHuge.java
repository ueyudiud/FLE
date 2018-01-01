/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tree;

import java.util.Random;

import farcore.lib.tree.ITree;
import farcore.lib.tree.TreeGenAbstract;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TreeGenHuge extends TreeGenAbstract
{
	/** The base height of the tree */
	protected int	baseHeight;
	/** The random height of the tree */
	protected int	randHeight;
	
	public TreeGenHuge(ITree tree, float generateCoreLeavesChance)
	{
		super(tree, generateCoreLeavesChance);
	}
	
	@Override
	public void setHeight(int baseHeight, int randHeight)
	{
		this.baseHeight = baseHeight;
		this.randHeight = randHeight;
	}
	
	private boolean checkCanGrow(World world, Random rand, int x, int y, int z, int height)
	{
		if (y >= 1 && y + height + 1 <= 256)
		{
			if (!checkLogGrow(world, x, y, z, 1, 0, 1, false)) return false;
			if (!checkLogGrow(world, x, y + 1, z, 2, height, 2, true)) return false;
			return true;
		}
		else
			return false;
	}
	
	private boolean canHugeTreePlantAt(World world, Random rand, int x, int y, int z)
	{
		BlockPos pos;
		BlockPos pos1 = new BlockPos(x, y, z);
		IBlockState state = world.getBlockState(pos = new BlockPos(x, y - 1, z));
		
		boolean isSoil = state.getBlock().canSustainPlant(state, world, pos, EnumFacing.UP, (BlockSapling) Blocks.SAPLING);
		if (isSoil && y >= 2)
		{
			state.getBlock().onPlantGrow(state, world, pos, pos1);
			(state = world.getBlockState((pos = pos.add(1, 0, 0)))).getBlock().onPlantGrow(state, world, pos, pos1);
			(state = world.getBlockState((pos = pos.add(0, 0, 1)))).getBlock().onPlantGrow(state, world, pos, pos1);
			(state = world.getBlockState((pos = pos.add(-1, 0, 0)))).getBlock().onPlantGrow(state, world, pos, pos1);
			return true;
		}
		else
			return false;
	}
	
	protected boolean matchHugeTreeGrow(World world, Random rand, int x, int y, int z, int height)
	{
		return checkCanGrow(world, rand, x, y, z, height) && canHugeTreePlantAt(world, rand, x, y, z);
	}
}
