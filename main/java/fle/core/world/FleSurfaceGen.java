package fle.core.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class FleSurfaceGen extends WorldGenerator
{
	int size;
	int count;
	
	protected FleSurfaceGen(int aSize, int aCount)
	{
		size = aSize;
		count = aCount;
	}
	

	public boolean generate(World aWorld, Random aRand, int x, int y, int z)
    {
        Block block;

        do
        {
            block = aWorld.getBlock(x, y, z);
            if (!(block.isLeaves(aWorld, x, y, z) || block.isAir(aWorld, x, y, z)))
            {
                break;
            }
            --y;
        } 
        while (y > 0);

        int i = 0;
        for (int l = 0; l < count * 2; ++l)
        {
            int i1 = x + aRand.nextInt(size) - aRand.nextInt(size);
            int j1 = y + aRand.nextInt(size / 2) - aRand.nextInt(size / 2);
            int k1 = z + aRand.nextInt(size) - aRand.nextInt(size);

            if (generateAt(aWorld, aRand, x, y, z))
            {
            	++i;
            }
            if(i >= count) break;
        }
        return i != 0;
    }
	
	public abstract boolean generateAt(World aWorld, Random aRand, int x, int y, int z);
}