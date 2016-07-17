package farcore.lib.tree;

import java.util.Random;

import farcore.data.V;
import farcore.lib.tile.instance.TECoreLeaves;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TreeGenAbstract implements ITreeGenerator
{
	protected float generateCoreLeavesChance;
	protected TreeBase tree;

	public TreeGenAbstract(TreeBase tree, float generateCoreLeavesChance)
	{
		this.tree = tree;
		this.generateCoreLeavesChance = generateCoreLeavesChance;
	}
	
	protected boolean isReplaceable(World world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		return block.isAir(world, x, y, z) || block.isLeaves(world, x, y, z);
	}
	
	protected boolean checkEmpty(World world, int x, int y, int z, int l, int w, int h, boolean matchLocal)
	{
		if(!V.generateState && !world.checkChunksExist(x - l, y, z - h, x + l, y + w, z + h))
		{
			return false;
		}
		for(int i = x - l; i <= x + l; ++i)
			for(int j = y; j <= y + w; ++j)
				for(int k = z - h; k <= z + h; ++k)
				{
					if(!matchLocal && i == x && j == y && k == z) continue;
					Block block = world.getBlock(i, j, k);
					Material material;
					if(isReplaceable(world, i, j, k))
					{
						continue;
					}
					return false;
				}
		return true;
	}
	
	protected void generateLog(World world, int x, int y, int z, int meta)
	{
		world.setBlock(x, y, z, tree.logNat, meta, V.generateState ? 2 : 3);
	}

	protected void generateTreeLeaves(World world, int x, int y, int z, int meta, Random rand, TreeInfo info)
	{
		generateTreeLeaves(world, x, y, z, meta, generateCoreLeavesChance, rand, info);
	}
	
	protected void generateCloudlyLeaves(World world, int x, int y, int z, int size, int meta, Random rand, TreeInfo info, byte core)
	{
		switch (core)
		{
		case 1 :
			int sq = size * size;
	        for(int i = -size; i <= size; ++i)
	        	for(int j = -size; j <= size; ++j)
	        	{
	        		if(i * i + j * j <= sq)
	        		{
	        			generateTreeLeaves(world, x + i, y, z + j, meta, rand, info);
	        		}
	        	}
			break;
		case 2 : 
			sq = (int) ((size + .5F) * (size + .5F));
	        for(int i = -size; i <= size + 1; ++i)
	        	for(int j = -size; j <= size + 1; ++j)
	        	{
	        		if((i - .5F) * (i - .5F) + (j - .5F) * (j - .5F) <= sq)
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
		if(world.isAirBlock(x, y, z) || world.getBlock(x, y, z).canBeReplacedByLeaves(world, x, y, z))
		{
			meta &= 0x7;
			int state = V.generateState ? 2 : 3;
			if(rand.nextDouble() <= generateCoreLeavesChance)
			{
				world.setBlock(x, y, z, tree.leavesCore, meta, state);
				U.Worlds.setTileEntity(world, x, y, z, new TECoreLeaves(tree, info), !V.generateState);
			}
			else
			{
				world.setBlock(x, y, z, tree.leaves, meta, state);
			}
		}
	}
	
	public abstract boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info);
}