package fle.resource.world;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class FleDivergeMineableGen extends WorldGenerator
{
    /** The number of blocks to generate. */
    protected int numberOfBlocks;
    protected double size;
    protected float diverge;

    public FleDivergeMineableGen(float size, float div, int amount)
    {
    	diverge = div;
    	numberOfBlocks = amount;
    	this.size = Math.pow(amount, 1D / 3D) * (1 + size);
	}
    
    public FleDivergeMineableGen(float div, int amount)
    {
    	this(1.0F, div, amount);
	}
    
    public FleDivergeMineableGen(int amount)
    {
    	this(2F, amount);
    }

    public boolean generate(World aWorld, Random rand, int x, int y, int z)
    {
    	int x1, y1, z1;
    	int loop = 0;
    	int c = 0;
    	do
    	{
    		x1 = (int) ((Math.pow(rand.nextFloat(), diverge) - Math.pow(rand.nextFloat(), diverge)) * size);
    		y1 = (int) ((Math.pow(rand.nextFloat(), diverge) - Math.pow(rand.nextFloat(), diverge)) * size);
    		z1 = (int) ((Math.pow(rand.nextFloat(), diverge) - Math.pow(rand.nextFloat(), diverge)) * size);
    		int distanceSQ = x1 * x1 + y1 * y1 + z1 * z1;
    		if(matchCanGen(aWorld, rand, x + x1, y + y1, z + z1, distanceSQ))
    		{
    			genBlockAt(aWorld, rand, x + x1, y + y1, z + z1, distanceSQ);
    			++c;
    			if(c >= numberOfBlocks) break;
    		}
    	}
    	while(++loop < numberOfBlocks * 4);
        return true;
    }
    
    protected void genBlockAt(World world, Random rand, int x, int y, int z,
			int distanceSQ)
    {
		genBlockAt(world, rand, x, y, z);
	}

	protected abstract boolean matchCanGen(World world, Random rand, int x, int y, int z, int distanceSQ);
    
    protected abstract void genBlockAt(World world, Random rand, int x, int y, int z);
}