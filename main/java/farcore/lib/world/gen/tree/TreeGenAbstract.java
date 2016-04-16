package farcore.lib.world.gen.tree;

import java.util.Random;

import farcore.interfaces.ITreeGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class TreeGenAbstract extends WorldGenerator implements ITreeGenerator
{
    protected Block log;
    protected Block leaves;
    
    @Override
    public void initLogBlock(Block log, Block leaves)
    {
    	this.leaves = leaves;
    	this.log = log;
    }
    
	@Override
	public boolean generate(World world, Random random, int x, int y, int z)
	{
		return generate(world, random, x, y, z, true);
	}

    protected boolean isPlant(Block block)
    {
        return block.getMaterial() == Material.air ||
        		block.getMaterial() == Material.leaves || 
        		block == Blocks.grass || 
        		block == Blocks.dirt || 
        		block == Blocks.log || 
        		block == Blocks.log2 || 
        		block == Blocks.sapling || 
        		block == Blocks.vine;
    }
    
    protected boolean isReplaceable(World world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);
        return block.isAir(world, x, y, z) || block.isLeaves(world, x, y, z) || block.isWood(world, x, y, z) || isPlant(block);
    }
}