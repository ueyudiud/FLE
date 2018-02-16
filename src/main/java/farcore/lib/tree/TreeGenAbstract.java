/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tree;

import java.util.Random;

import farcore.FarCore;
import farcore.blocks.flora.BlockPlantVine;
import farcore.data.EnumBlock;
import farcore.data.V;
import farcore.lib.tile.instance.TECoreLeaves;
import farcore.lib.tree.ITree.BlockType;
import nebula.common.util.Worlds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TreeGenAbstract implements ITreeGenerator
{
	protected float	generateCoreLeavesChance;
	protected ITree	tree;
	
	public TreeGenAbstract(ITree tree, float generateCoreLeavesChance)
	{
		this.tree = tree;
		this.generateCoreLeavesChance = generateCoreLeavesChance;
	}
	
	/**
	 * Helper method.
	 * 
	 * @param minHeight
	 * @param randHeight
	 */
	public void setHeight(int minHeight, int randHeight)
	{
	}
	
	protected boolean isLogReplaceable(World world, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(x, y, z);
		IBlockState block;
		return world.isAirBlock(pos) || (block = world.getBlockState(pos)).getBlock().isLeaves(block, world, pos) || block.getBlock() == EnumBlock.sapling.block;
	}
	
	protected boolean isLeavesReplaceable(World world, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(x, y, z);
		IBlockState block;
		return world.isAirBlock(pos) || (block = world.getBlockState(pos)).getBlock().canBeReplacedByLeaves(block, world, pos);
	}
	
	protected boolean isAirOrVine(World world, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(x, y, z);
		IBlockState state;
		return (state = world.getBlockState(pos)).getBlock().isAir(state, world, pos) || state.getBlock() == EnumBlock.vine.block;
	}
	
	protected void generateVine(World world, int x, int y, int z, EnumFacing facing)
	{
		BlockPlantVine.addVineBlock(world, new BlockPos(x, y, z), facing);
	}
	
	protected boolean checkLeavesGrow(World world, int x, int y, int z, int l, int w, int h, boolean matchLocal)
	{
		BlockPos pos = new BlockPos(x, y, z);
		if (!FarCore.worldGenerationFlag && !world.isAreaLoaded(pos.add(-l, -w, -h), pos.add(l, w, h))) return false;
		for (int i = x - l; i <= x + l; ++i)
		{
			for (int j = y; j <= y + w; ++j)
			{
				for (int k = z - h; k <= z + h; ++k)
				{
					if (!matchLocal && i == x && j == y && k == z)
					{
						continue;
					}
					if (isLeavesReplaceable(world, i, j, k))
					{
						continue;
					}
					return false;
				}
			}
		}
		return true;
	}
	
	protected boolean checkLogGrow(World world, int x, int y, int z, int l, int w, int h, boolean matchLocal)
	{
		BlockPos pos = new BlockPos(x, y, z);
		if (!V.generateState && !world.isAreaLoaded(pos.add(-l, -w, -h), pos.add(l, w, h))) return false;
		for (int i = x - l; i <= x + l; ++i)
		{
			for (int j = y; j <= y + w; ++j)
			{
				for (int k = z - h; k <= z + h; ++k)
				{
					if (!matchLocal && i == x && j == y && k == z)
					{
						continue;
					}
					if (isLogReplaceable(world, i, j, k))
					{
						continue;
					}
					return false;
				}
			}
		}
		return true;
	}
	
	protected void generateLog(World world, int x, int y, int z, int meta)
	{
		Worlds.setBlock(world, new BlockPos(x, y, z), this.tree.getBlock(BlockType.LOG), meta, V.generateState ? 2 : 3);
	}
	
	protected void generateTreeLeaves(World world, int x, int y, int z, int meta, Random rand, TreeInfo info)
	{
		generateTreeLeaves(world, x, y, z, meta, this.generateCoreLeavesChance, rand, info);
	}
	
	protected void generateCloudlyLeaves(World world, int x, int y, int z, int size, int meta, Random rand, TreeInfo info, byte core)
	{
		switch (core)
		{
		case 1:
			int sq = size * size;
			for (int i = -size; i <= size; ++i)
			{
				for (int j = -size; j <= size; ++j)
					if (i * i + j * j <= sq)
					{
						generateTreeLeaves(world, x + i, y, z + j, meta, rand, info);
					}
			}
			break;
		case 2:
			sq = (int) ((size + .5F) * (size + .5F));
			for (int i = -size; i <= size + 1; ++i)
			{
				for (int j = -size; j <= size + 1; ++j)
					if ((i - .5F) * (i - .5F) + (j - .5F) * (j - .5F) <= sq)
					{
						generateTreeLeaves(world, x + i, y, z + j, meta, rand, info);
					}
			}
			break;
		default:
			break;
		}
	}
	
	protected void generateTreeLeaves(World world, int x, int y, int z, int meta, float generateCoreLeavesChance, Random rand, TreeInfo info)
	{
		BlockPos pos = new BlockPos(x, y, z);
		IBlockState state;
		if (world.isAirBlock(pos) || (state = world.getBlockState(pos)).getBlock().canBeReplacedByLeaves(state, world, pos))
		{
			meta &= 0x7;
			int flag = V.generateState ? 2 : 3;
			if (rand.nextDouble() <= generateCoreLeavesChance)
			{
				Worlds.setBlock(world, pos, this.tree.getBlock(BlockType.LEAVES_CORE), meta, flag);
				Worlds.setTileEntity(world, pos, new TECoreLeaves(this.tree, info), !V.generateState);
			}
			else
			{
				Worlds.setBlock(world, pos, this.tree.getBlock(BlockType.LEAVES), meta, flag);
			}
		}
	}
	
	@Override
	public abstract boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info);
}
