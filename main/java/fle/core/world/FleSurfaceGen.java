package fle.core.world;

import java.util.Random;

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
	
	private static double a = 1D / 3D;
	
	@Override
	public boolean generate(World aWorld, Random aRand,
			int x, int y, int z)
	{
		int size = (int) Math.floor(Math.pow(this.size, a));
		int count = this.count;
		for(int i = 0; i < this.count * 10; ++i)
		{
            int i1 = x + aRand.nextInt(size) - aRand.nextInt(size);
            int j1 = y + aRand.nextInt(size) - aRand.nextInt(size);
            int k1 = z + aRand.nextInt(size) - aRand.nextInt(size);
            while(Math.sqrt(i1 * i1 + j1 * j1 + k1 * k1) < size)
            {
            	i1 += i1 < 0 ? 1 : -1;
            	j1 += j1 < 0 ? 1 : -1;
            	k1 += k1 < 0 ? 1 : -1;
            }
            if(aRand.nextDouble() < 0.6D) continue;
			if(generateAt(aWorld, aRand, x, y, z)) count -= 1;
			if(count <= 0) break;
		}
		return count != this.count;
	}
	
	public abstract boolean generateAt(World aWorld, Random aRand, int x, int y, int z);
}